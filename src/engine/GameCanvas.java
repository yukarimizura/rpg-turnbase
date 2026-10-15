package engine;

import engine.canvas.*;
import model.Attacker;
import model.Character;
import model.Enemy;
import service.*;
import view.screen.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends JPanel {
    public static final int BASE_WIDTH = 950;
    public static final int BASE_HEIGHT = 600;

    private GameState currentState = GameState.PROFILE_SELECT;
    private PartyManager party;
    private StageManager stage;
    private PlayerProfile profile;
    private GachaService gacha;
    private InputHandler input;
    private List<Enemy> enemies = new ArrayList<>();
    private List<String> savedProfiles = new ArrayList<>();

    private CanvasButtonManager btn = new CanvasButtonManager();
    private CombatEngine combat = new CombatEngine();
    private GachaHandler gachaHandler = new GachaHandler();

    public GameCanvas(PartyManager party, StageManager stage, PlayerProfile profile, GachaService gacha, InputHandler input) {
        this.party = party;
        this.stage = stage;
        this.profile = profile;
        this.gacha = gacha;
        this.input = input;

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
        enemies.clear();
        for (int i = 0; i < 6; i++) {
            float rx = (float) (Math.random() * 800 + 70);
            float ry = (float) (Math.random() * 350 + 130);
            enemies.add(new Enemy(rx, ry, false, stage.getCurrentStage()));
        }
    }

    private Point getVirtualMousePoint() {
        if (input.mousePosition == null) return new Point(0, 0);
        double scaleX = (double) getWidth() / BASE_WIDTH;
        double scaleY = (double) getHeight() / BASE_HEIGHT;
        return new Point((int) (input.mousePosition.x / scaleX), (int) (input.mousePosition.y / scaleY));
    }

    public void updateState() {
        Point m = getVirtualMousePoint();
        btn.updateHover(m);

        if (input.mouseClicked) {
            handleMenuClicks(m);
            input.mouseClicked = false;
        }

        if (currentState == GameState.BATTLE) {
            if (input.escPressed) {
                currentState = GameState.LOBBY;
                input.escPressed = false;
                return;
            }
            currentState = combat.updateCombat(party, stage, profile, input, enemies, this::spawnCreeps);
            if (currentState == GameState.VICTORY) {
                SaveManager.saveGame(profile, party, stage, gacha); // Auto-save saat menang
            }
        }
    }

    private void handleMenuClicks(Point m) {
        if (currentState == GameState.PROFILE_SELECT) {
            int clickedIdx = ProfileSelectScreen.getClickedProfileIndex(m, savedProfiles.size());
            if (clickedIdx != -1) {
                String chosenUser = savedProfiles.get(clickedIdx);
                SaveManager.loadGame(chosenUser, profile, party, stage, gacha);
                currentState = GameState.TITLE;
            } else if (btn.btnNewProfile.contains(m)) {
                String name = JOptionPane.showInputDialog(this, "Enter New Commander Name:", "Create Profile", JOptionPane.PLAIN_MESSAGE);
                if (name != null && !name.trim().isEmpty()) {
                    name = name.trim();
                    profile.setPlayerName(name);
                    profile.setGems(1600);
                    profile.setCredits(2000);
                    profile.getOwnedHeroes().clear();
                    party.getActiveParty().clear();

                    // Guaranteed Starter
                    Attacker starter = new Attacker("HERO_" + System.currentTimeMillis() % 1000, "Dawn Blade Dancer", "SR", "Blade Dancer", 1600, 180, 50);
                    profile.addHero(starter);
                    party.addHeroToParty(starter);

                    SaveManager.saveGame(profile, party, stage, gacha);
                    refreshProfiles();
                    currentState = GameState.TITLE;
                }
            } else if (btn.btnQuitProfile.contains(m)) {
                System.exit(0);
            }
        } else if (currentState == GameState.TITLE) {
            if (btn.btnStart.contains(m)) currentState = GameState.LOBBY;
            else if (btn.btnQuit.contains(m)) System.exit(0);
        } else if (currentState == GameState.LOBBY) {
            if (btn.btnBattle.contains(m)) {
                currentState = GameState.BATTLE;
                requestFocusInWindow();
            } else if (btn.btnGacha.contains(m)) currentState = GameState.GACHA;
            else if (btn.btnParty.contains(m)) currentState = GameState.PARTY;
            else if (btn.btnSettings.contains(m)) currentState = GameState.SETTINGS;
            else if (btn.btnTrainHero.contains(m)) {
                Character leader = party.getActiveHero();
                if (leader != null) {
                    int cost = leader.getUpgradeCost();
                    if (profile.getCredits() >= cost) {
                        profile.addCredits(-cost);
                        leader.levelUpManual();
                        SaveManager.saveGame(profile, party, stage, gacha); // Auto save
                    }
                }
            }
        } else if (currentState == GameState.GACHA) {
            if (btn.btnBack.contains(m)) currentState = GameState.LOBBY;
            else if (btn.btn1x.contains(m)) {
                gachaHandler.handlePull(1, 160, profile, gacha, party);
                SaveManager.saveGame(profile, party, stage, gacha);
            } else if (btn.btn10x.contains(m)) {
                gachaHandler.handlePull(10, 1600, profile, gacha, party);
                SaveManager.saveGame(profile, party, stage, gacha);
            }
        } else if (currentState == GameState.PARTY) {
            if (btn.btnBack.contains(m)) currentState = GameState.LOBBY;
            else {
                PartyScreen.handleClick(m, party, profile);
                SaveManager.saveGame(profile, party, stage, gacha);
            }
        } else if (currentState == GameState.SETTINGS) {
            if (btn.btnBack.contains(m)) currentState = GameState.LOBBY;
            else if (btn.btnSaveGame.contains(m)) {
                SaveManager.saveGame(profile, party, stage, gacha);
                JOptionPane.showMessageDialog(this, "Progress successfully saved!", "Save Game", JOptionPane.INFORMATION_MESSAGE);
            } else if (btn.btnResStandard.contains(m)) {
                Container top = SwingUtilities.getWindowAncestor(this);
                if (top != null) top.setSize(950, 630);
            } else if (btn.btnResHD.contains(m)) {
                Container top = SwingUtilities.getWindowAncestor(this);
                if (top != null) top.setSize(1280, 750);
            }
        } else if (currentState == GameState.BATTLE) {
            if (btn.btnBack.contains(m)) currentState = GameState.LOBBY;
        } else if (currentState == GameState.VICTORY || currentState == GameState.GAME_OVER) {
            if (btn.btnReturn.contains(m)) {
                party.reviveAllParty();
                spawnCreeps();
                currentState = GameState.LOBBY;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform old = g2.getTransform();
        double scaleX = (double) getWidth() / BASE_WIDTH;
        double scaleY = (double) getHeight() / BASE_HEIGHT;
        g2.scale(scaleX, scaleY);

        switch (currentState) {
            case PROFILE_SELECT:
                ProfileSelectScreen.render(g2, savedProfiles, btn.btnNewProfile, btn.btnQuitProfile);
                break;
            case TITLE:
                TitleScreen.render(g2, btn.btnStart, btn.btnQuit);
                break;
            case LOBBY:
                LobbyScreen.render(g2, profile, party, btn.btnBattle, btn.btnGacha, btn.btnParty, btn.btnTrainHero, btn.btnSettings);
                break;
            case GACHA:
                GachaScreen.render(g2, profile, gacha, gachaHandler.getLastNotice(), btn.btnBack, btn.btn1x, btn.btn10x);
                break;
            case PARTY:
                PartyScreen.render(g2, party, profile, btn.btnBack);
                break;
            case SETTINGS:
                SettingsScreen.render(g2, btn.btnBack, btn.btnResStandard, btn.btnResHD, btn.btnToggleVfx, btn.btnSaveGame);
                break;
            case BATTLE:
                BattleScreen.render(g2, enemies, party, stage, btn.btnBack, combat.damageTexts, combat.vfxList);
                break;
            case VICTORY:
                ResultScreen.render(g2, true, stage, combat.victoryGems, combat.victoryCredits, btn.btnReturn);
                break;
            case GAME_OVER:
                ResultScreen.render(g2, false, stage, 0, 0, btn.btnReturn);
                break;
            default:
                break;
        }

        g2.setTransform(old);
    }
}