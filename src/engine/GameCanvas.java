package engine;

import engine.canvas.*;
import model.Character;
import model.Enemy;
import service.*;
import view.screen.*;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends JPanel {
    private GameState currentState = GameState.TITLE;
    private PartyManager party;
    private StageManager stage;
    private PlayerProfile profile;
    private GachaService gacha;
    private InputHandler input;
    private List<Enemy> enemies = new ArrayList<>();

    private CanvasButtonManager btn = new CanvasButtonManager();
    private CombatEngine combat = new CombatEngine();
    private GachaHandler gachaHandler = new GachaHandler();

    public GameCanvas(PartyManager party, StageManager stage, PlayerProfile profile, GachaService gacha, InputHandler input) {
        this.party = party;
        this.stage = stage;
        this.profile = profile;
        this.gacha = gacha;
        this.input = input;

        setPreferredSize(new Dimension(950, 600));
        setBackground(new Color(15, 17, 23));
        setFocusable(true);
        addKeyListener(input);

        MouseAdapter mouse = input.getMouseListener();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        spawnCreeps();
    }

    public void spawnCreeps() {
        enemies.clear();
        for (int i = 0; i < 6; i++) {
            float rx = (float) (Math.random() * 800 + 70);
            float ry = (float) (Math.random() * 350 + 130);
            enemies.add(new Enemy(rx, ry, false, stage.getCurrentStage()));
        }
    }

    public void updateState() {
        Point m = input.mousePosition;
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
        }
    }

    private void handleMenuClicks(Point m) {
        if (currentState == GameState.TITLE) {
            if (btn.btnStart.contains(m)) currentState = GameState.LOBBY;
            else if (btn.btnQuit.contains(m)) System.exit(0);
        } else if (currentState == GameState.LOBBY) {
            if (btn.btnBattle.contains(m)) {
                currentState = GameState.BATTLE;
                requestFocusInWindow();
            } else if (btn.btnGacha.contains(m)) {
                currentState = GameState.GACHA;
            } else if (btn.btnParty.contains(m)) {
                currentState = GameState.PARTY;
            } else if (btn.btnTrainHero.contains(m)) {
                Character leader = party.getActiveHero();
                if (leader != null) {
                    int cost = leader.getUpgradeCost();
                    if (profile.getCredits() >= cost) {
                        profile.addCredits(-cost);
                        leader.levelUpManual();
                    }
                }
            }
        } else if (currentState == GameState.GACHA) {
            if (btn.btnBack.contains(m)) currentState = GameState.LOBBY;
            else if (btn.btn1x.contains(m)) gachaHandler.handlePull(1, 160, profile, gacha, party);
            else if (btn.btn10x.contains(m)) gachaHandler.handlePull(10, 1600, profile, gacha, party);
        } else if (currentState == GameState.PARTY) {
            if (btn.btnBack.contains(m)) {
                currentState = GameState.LOBBY;
            } else {
                PartyScreen.handleClick(m, party, profile);
            }
        } else if (currentState == GameState.BATTLE) {
            if (btn.btnBack.contains(m)) {
                currentState = GameState.LOBBY;
            }
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

        switch (currentState) {
            case TITLE:
                TitleScreen.render(g2, btn.btnStart, btn.btnQuit);
                break;
            case LOBBY:
                LobbyScreen.render(g2, profile, party, btn.btnBattle, btn.btnGacha, btn.btnParty, btn.btnTrainHero);
                break;
            case GACHA:
                GachaScreen.render(g2, profile, gacha, gachaHandler.getLastNotice(), btn.btnBack, btn.btn1x, btn.btn10x);
                break;
            case PARTY:
                PartyScreen.render(g2, party, profile, btn.btnBack);
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
    }
}