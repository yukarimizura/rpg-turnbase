package engine;

import engine.canvas.CombatEngine;
import engine.canvas.combat.EnemyCombatProcessor;
import engine.handlers.*;
import model.npc.Enemy;
import service.*;
import view.component.CyberUIEffects;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameCanvas extends JPanel {
    private static final long serialVersionUID = 1L;
    public static final int BASE_WIDTH = 1920, BASE_HEIGHT = 1080;

    private GameState currentState = GameState.PROFILE_SELECT;
    private PartyManager party;
    private StageManager stage;
    private PlayerProfile profile;
    private GachaService gacha;
    private InputHandler input;
    private List<Enemy> enemies = new CopyOnWriteArrayList<>();
    private List<String> savedProfiles = new CopyOnWriteArrayList<>();

    private CanvasButtonManager btn = new CanvasButtonManager();
    private CombatEngine combat = new CombatEngine();
    private GachaHandler gachaHandler = new GachaHandler();

    public GameCanvas(PartyManager party, StageManager stage, PlayerProfile profile, GachaService gacha, InputHandler input) {
        this.party = party; 
        this.stage = stage; 
        this.profile = profile; 
        this.gacha = gacha; 
        this.input = input;

        // Set ukuran awal canvas sesuai target rasio 16:9 (1080p virtual)
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setBackground(new Color(15, 17, 23));
        setFocusable(true);
        addKeyListener(input);
        
        MouseAdapter mouse = input.getMouseListener();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        
        refreshProfiles();
        spawnCreeps();
    }

    public void refreshProfiles() { 
        this.savedProfiles = SaveManager.listSavedProfiles(); 
    }
    
    public void spawnCreeps() { 
        EnemyCombatProcessor.spawnCreepWave(enemies, stage.getCurrentStage()); 
    }

    private Point getVirtualMousePoint() {
        if (input.mousePosition == null) return new Point(0, 0);
        
        int currentWidth = getWidth();
        int currentHeight = getHeight();

        if (currentWidth <= 0) currentWidth = BASE_WIDTH;
        if (currentHeight <= 0) currentHeight = BASE_HEIGHT;

        double scaleX = (double) currentWidth / BASE_WIDTH;
        double scaleY = (double) currentHeight / BASE_HEIGHT;

        return new Point(
            (int) (input.mousePosition.x / scaleX),
            (int) (input.mousePosition.y / scaleY)
        );
    }

    public void updateState() {
        currentState = CyberUIEffects.updateTransition(currentState);
        Point m = getVirtualMousePoint();
        btn.updateHover(m);

        if (input.mouseClicked) {
            if (!CyberUIEffects.isTransitioning()) {
                currentState = CanvasClickHandler.handleClick(currentState, m, btn, profile, party, stage, gacha, gachaHandler, savedProfiles, this, this::refreshProfiles);
                if ((currentState == GameState.VICTORY || currentState == GameState.GAME_OVER) && btn.btnReturn.contains(m)) {
                    party.reviveAllParty();
                    spawnCreeps();
                    CyberUIEffects.startTransition(GameState.LOBBY, "RETURNING TO COMMAND HUB...");
                }
            }
            input.mouseClicked = false;
        }

        if (currentState == GameState.BATTLE && !CyberUIEffects.isTransitioning()) {
            if (input.escPressed) {
                CyberUIEffects.startTransition(GameState.LOBBY, "RETREATING TO COMMAND HUB...");
                input.escPressed = false;
                return;
            }
            GameState next = combat.updateCombat(party, stage, profile, input, enemies, this::spawnCreeps);
            if (next != GameState.BATTLE) {
                if (next == GameState.VICTORY) SaveManager.saveGame(profile, party, stage, gacha);
                CyberUIEffects.startTransition(next, next == GameState.VICTORY ? "VICTORY! SYNCHRONIZING REWARDS..." : "SQUAD FALLEN...");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Anti-aliasing & Rendering quality hints
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        AffineTransform oldTx = g2.getTransform();
        
        int currentWidth = getWidth();
        int currentHeight = getHeight();

        if (currentWidth <= 0) currentWidth = BASE_WIDTH;
        if (currentHeight <= 0) currentHeight = BASE_HEIGHT;
        
        // Kalkulasi rasio scaling aktual terhadap canvas fisik window
        double scaleX = (double) currentWidth / BASE_WIDTH;
        double scaleY = (double) currentHeight / BASE_HEIGHT;
        
        g2.scale(scaleX, scaleY);
        
        // Render seluruh UI & Game Screen dalam koordinat virtual 1920x1080
        CanvasRenderer.renderCurrentScreen(g2, currentState, savedProfiles, profile, party, stage, gacha, gachaHandler, btn, combat, enemies);
        CyberUIEffects.drawTransitionOverlay(g2);
        
        g2.setTransform(oldTx);
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Panggil metode ini saat berganti preset resolusi (Windowed vs Fullscreen Native)
     * untuk memaksa JFrame dan Canvas merefresh boundary viewport-nya.
     */
    public void updateCanvasBounds(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setSize(width, height);
        revalidate();
        repaint();
    }
}