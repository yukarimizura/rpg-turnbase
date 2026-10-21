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
    public static final int BASE_WIDTH = 950, BASE_HEIGHT = 600;

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
        this.party = party; this.stage = stage; this.profile = profile; this.gacha = gacha; this.input = input;
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

    public void refreshProfiles() { this.savedProfiles = SaveManager.listSavedProfiles(); }
    public void spawnCreeps() { EnemyCombatProcessor.spawnCreepWave(enemies, stage.getCurrentStage()); }

    private Point getVirtualMousePoint() {
        if (input.mousePosition == null) return new Point(0, 0);
        return new Point((int) (input.mousePosition.x / ((double) getWidth() / BASE_WIDTH)),
                         (int) (input.mousePosition.y / ((double) getHeight() / BASE_HEIGHT)));
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform old = g2.getTransform();
        g2.scale((double) getWidth() / BASE_WIDTH, (double) getHeight() / BASE_HEIGHT);
        CanvasRenderer.renderCurrentScreen(g2, currentState, savedProfiles, profile, party, stage, gacha, gachaHandler, btn, combat, enemies);
        CyberUIEffects.drawTransitionOverlay(g2);
        g2.setTransform(old);
        Toolkit.getDefaultToolkit().sync();
    }
}