package engine;

import model.Character;
import model.Enemy;
import service.GachaService;
import service.PartyManager;
import service.PlayerProfile;
import service.StageManager;
import view.MainHUD;
import view.UIButton;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameCanvas extends JPanel {
    private GameState currentState = GameState.TITLE;
    private PartyManager partyManager;
    private StageManager stageManager;
    private PlayerProfile playerProfile;
    private GachaService gachaService;
    private InputHandler input;
    private List<Enemy> enemies;

    // UI Buttons
    private UIButton btnStartGame;
    private UIButton btnQuitGame;
    private UIButton btnGoBattle;
    private UIButton btnGoGacha;
    private UIButton btnGoParty;
    private UIButton btnBackToLobby;
    private UIButton btnPull1x;
    private UIButton btnPull10x;
    private UIButton btnEndReturnHub;

    private String lastGachaNotice = "";
    private int victoryGemsGained = 0;
    private int victoryCreditsGained = 0;

    public GameCanvas(PartyManager partyManager, StageManager stageManager, PlayerProfile profile, GachaService gacha, InputHandler input) {
        this.partyManager = partyManager;
        this.stageManager = stageManager;
        this.playerProfile = profile;
        this.gachaService = gacha;
        this.input = input;
        this.enemies = new ArrayList<>();

        setPreferredSize(new Dimension(950, 600));
        setBackground(new Color(15, 17, 23));
        setFocusable(true);
        addKeyListener(input);

        MouseAdapter mouse = input.getMouseListener();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        initButtons();
        spawnCreeps();
    }

    private void initButtons() {
        btnStartGame    = new UIButton(375, 320, 200, 45, "ENTER GAME", new Color(41, 128, 185), new Color(52, 152, 219));
        btnQuitGame     = new UIButton(375, 380, 200, 45, "EXIT", new Color(192, 57, 43), new Color(231, 76, 60));

        btnGoBattle     = new UIButton(100, 460, 220, 50, "ENTER BATTLE ARENA", new Color(192, 57, 43), new Color(231, 76, 60));
        btnGoGacha      = new UIButton(365, 460, 220, 50, "GACHA PORTAL", new Color(142, 68, 173), new Color(155, 89, 182));
        btnGoParty      = new UIButton(630, 460, 220, 50, "PARTY FORMATION", new Color(39, 174, 96), new Color(46, 204, 113));

        btnBackToLobby  = new UIButton(780, 25, 140, 35, "< BACK TO HUB", new Color(52, 73, 94), new Color(44, 62, 80));
        btnEndReturnHub = new UIButton(375, 400, 200, 45, "RETURN TO HUB", new Color(41, 128, 185), new Color(52, 152, 219));

        btnPull1x       = new UIButton(280, 450, 180, 45, "PULL 1x (160 Gems)", new Color(142, 68, 173), new Color(155, 89, 182));
        btnPull10x      = new UIButton(490, 450, 180, 45, "PULL 10x (1600 Gems)", new Color(211, 84, 0), new Color(230, 126, 34));
    }

    private void spawnCreeps() {
        enemies.clear();
        for (int i = 0; i < 6; i++) {
            float rx = (float) (Math.random() * 800 + 70);
            float ry = (float) (Math.random() * 350 + 130);
            enemies.add(new Enemy(rx, ry, false, stageManager.getCurrentStage()));
        }
    }

    public void updateState() {
        Point mouse = input.mousePosition;

        switch (currentState) {
            case TITLE:
                btnStartGame.checkHover(mouse);
                btnQuitGame.checkHover(mouse);
                if (input.mouseClicked) {
                    if (btnStartGame.contains(mouse)) {
                        currentState = GameState.LOBBY;
                    } else if (btnQuitGame.contains(mouse)) {
                        System.exit(0);
                    }
                    input.mouseClicked = false;
                }
                break;

            case LOBBY:
                btnGoBattle.checkHover(mouse);
                btnGoGacha.checkHover(mouse);
                btnGoParty.checkHover(mouse);
                if (input.mouseClicked) {
                    if (btnGoBattle.contains(mouse)) {
                        currentState = GameState.BATTLE;
                        requestFocusInWindow();
                    } else if (btnGoGacha.contains(mouse)) {
                        currentState = GameState.GACHA;
                    } else if (btnGoParty.contains(mouse)) {
                        currentState = GameState.PARTY;
                    }
                    input.mouseClicked = false;
                }
                break;

            case GACHA:
                btnBackToLobby.checkHover(mouse);
                btnPull1x.checkHover(mouse);
                btnPull10x.checkHover(mouse);
                if (input.mouseClicked) {
                    if (btnBackToLobby.contains(mouse)) {
                        currentState = GameState.LOBBY;
                    } else if (btnPull1x.contains(mouse)) {
                        performPull(1, 160);
                    } else if (btnPull10x.contains(mouse)) {
                        performPull(10, 1600);
                    }
                    input.mouseClicked = false;
                }
                break;

            case PARTY:
                btnBackToLobby.checkHover(mouse);
                if (input.mouseClicked) {
                    if (btnBackToLobby.contains(mouse)) {
                        currentState = GameState.LOBBY;
                    }
                    input.mouseClicked = false;
                }
                break;

            case BATTLE:
                btnBackToLobby.checkHover(mouse);
                if (input.mouseClicked) {
                    if (btnBackToLobby.contains(mouse)) {
                        currentState = GameState.LOBBY;
                        input.mouseClicked = false;
                        return;
                    }
                    input.mouseClicked = false;
                }

                if (input.escPressed) {
                    currentState = GameState.LOBBY;
                    input.escPressed = false;
                    return;
                }

                updateBattlePhysics();
                break;

            case VICTORY:
            case GAME_OVER:
                btnEndReturnHub.checkHover(mouse);
                if (input.mouseClicked) {
                    if (btnEndReturnHub.contains(mouse)) {
                        partyManager.reviveAllParty();
                        spawnCreeps();
                        currentState = GameState.LOBBY;
                    }
                    input.mouseClicked = false;
                }
                break;

            default:
                break;
        }
    }

    private void performPull(int count, int cost) {
        if (!playerProfile.spendGems(cost)) {
            lastGachaNotice = "Cyber Gems tidak mencukupi!";
            return;
        }
        Character last = null;
        for (int i = 0; i < count; i++) {
            last = gachaService.roll();
            playerProfile.addHero(last);
            if (!partyManager.isPartyFull()) {
                partyManager.addHeroToParty(last);
            }
        }
        if (last != null) {
            lastGachaNotice = "Berhasil Summon: " + last.getName() + " [" + last.getRarity() + " " + last.getRole() + "]!";
        }
    }

    private void updateBattlePhysics() {
        Character activeHero = partyManager.getActiveHero();

        // 1. Cek jika hero aktif mati -> Otomatis alihkan ke anggota party lain yang masih hidup
        if (activeHero == null || !activeHero.isAlive()) {
            boolean foundReplacement = false;
            for (int i = 0; i < partyManager.getActiveParty().size(); i++) {
                if (partyManager.getActiveParty().get(i).isAlive()) {
                    partyManager.switchHero(i);
                    activeHero = partyManager.getActiveHero();
                    foundReplacement = true;
                    break;
                }
            }

            // Jika tidak ada lagi hero yang hidup -> GAME OVER
            if (!foundReplacement || partyManager.isAllPartyDead()) {
                currentState = GameState.GAME_OVER;
                return;
            }
        }

        // Switch Hero 1 - 5 manual
        if (input.selectedHeroSlot != -1) {
            partyManager.switchHero(input.selectedHeroSlot);
            input.selectedHeroSlot = -1;
        }

        // Pergerakan WASD
        activeHero.vx = 0;
        activeHero.vy = 0;
        if (input.up) activeHero.vy -= activeHero.speed;
        if (input.down) activeHero.vy += activeHero.speed;
        if (input.left) activeHero.vx -= activeHero.speed;
        if (input.right) activeHero.vx += activeHero.speed;
        activeHero.updatePhysics();

        // Skill Q & R
        if (input.skillCast) {
            activeHero.castSkill(enemies);
            input.skillCast = false;
        }
        if (input.ultCast) {
            activeHero.castUltimate(enemies);
            input.ultCast = false;
        }

        // Tantang Boss (Tombol B)
        if (input.bossChallengeKey && stageManager.canChallengeBoss(partyManager.getActiveParty()) && !stageManager.isBossActive()) {
            enemies.clear();
            enemies.add(new Enemy(475, 200, true, stageManager.getCurrentStage()));
            stageManager.startBossBattle();
            input.bossChallengeKey = false;
        }

        // AI Musuh & Damage Collision
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy en = it.next();
            en.updateAI(activeHero.x, activeHero.y);

            float dist = (float) Math.hypot(en.x - activeHero.x, en.y - activeHero.y);
            if (dist < (en.size / 2.0f + activeHero.size / 2.0f)) {
                activeHero.takeDamage(en.attack);
            }

            if (en.currentHp <= 0) {
                activeHero.gainExp(en.expReward);
                int gGained = en.isBoss ? 300 : 20;
                int cGained = en.isBoss ? 1500 : 120;
                playerProfile.addGems(gGained);
                playerProfile.addCredits(cGained);

                if (en.isBoss) {
                    victoryGemsGained = gGained;
                    victoryCreditsGained = cGained;
                    stageManager.onBossDefeated();
                    it.remove();
                    currentState = GameState.VICTORY; // Transisi ke Layar Menang
                    return;
                } else {
                    stageManager.onCreepKilled();
                }
                it.remove();
            }
        }

        if (enemies.isEmpty() && !stageManager.isBossActive()) {
            spawnCreeps();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (currentState) {
            case TITLE:
                renderTitleScreen(g2);
                break;
            case LOBBY:
                renderLobbyScreen(g2);
                break;
            case GACHA:
                renderGachaScreen(g2);
                break;
            case PARTY:
                renderPartyScreen(g2);
                break;
            case BATTLE:
                renderBattleScreen(g2);
                break;
            case VICTORY:
                renderEndScreen(g2, true);
                break;
            case GAME_OVER:
                renderEndScreen(g2, false);
                break;
            default:
                break;
        }
    }

    // --- RENDER SCREENS ---

    private void renderTitleScreen(Graphics2D g2) {
        GradientPaint gp = new GradientPaint(0, 0, new Color(15, 17, 28), 0, 600, new Color(30, 10, 45));
        g2.setPaint(gp);
        g2.fillRect(0, 0, 950, 600);

        g2.setColor(new Color(0, 210, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 42));
        g2.drawString("CYBER - CHRONO", 285, 190);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("2D REALTIME ARPG GACHA ENGINE", 305, 230);

        btnStartGame.draw(g2);
        btnQuitGame.draw(g2);
    }

    private void renderLobbyScreen(Graphics2D g2) {
        g2.setColor(new Color(25, 28, 38));
        g2.fillRect(0, 0, 950, 70);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("COMMANDER: " + playerProfile.getPlayerName(), 30, 40);

        g2.setColor(new Color(0, 210, 255));
        g2.drawString("CYBER GEMS: " + playerProfile.getCyberGems(), 560, 40);

        g2.setColor(new Color(255, 204, 0));
        g2.drawString("CREDITS: " + playerProfile.getCredits(), 760, 40);

        Character leader = partyManager.getActiveHero();
        if (leader != null) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g2.drawString("Active Vanguard:", 380, 160);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            g2.drawString(leader.getName() + " [" + leader.getRarity() + " " + leader.getRole() + "]", 340, 195);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g2.drawString(String.format("Level: %d | HP: %d/%d | ATK: %d | DEF: %d", 
                    leader.getLevel(), leader.getCurrentHp(), leader.getMaxHp(), leader.getAttack(), leader.getDefense()), 300, 230);

            leader.x = 475; leader.y = 320;
            leader.draw(g2);
        }

        btnGoBattle.draw(g2);
        btnGoGacha.draw(g2);
        btnGoParty.draw(g2);
    }

    private void renderGachaScreen(Graphics2D g2) {
        btnBackToLobby.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("SUMMON PORTAL: STELLAR CHRONO", 220, 80);

        g2.setColor(new Color(0, 210, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("Gems Kamu: " + playerProfile.getCyberGems(), 400, 120);

        g2.setColor(new Color(30, 20, 45));
        g2.fillRoundRect(220, 150, 510, 250, 15, 15);
        g2.setColor(new Color(155, 89, 182));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(220, 150, 510, 250, 15, 15);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("SSR RATE UP: DAWN PRIESTESS & BLADE DANCER", 235, 230);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Rates: SSR 2% | SR 13% | R 85%", 370, 270);

        if (!lastGachaNotice.isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.drawString(lastGachaNotice, 310, 340);
        }

        btnPull1x.draw(g2);
        btnPull10x.draw(g2);
    }

    private void renderPartyScreen(Graphics2D g2) {
        btnBackToLobby.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 26));
        g2.drawString("ACTIVE COMBAT FORMATION (MAX 5)", 250, 70);

        List<Character> party = partyManager.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int cardX = 60 + (i * 170);
            int cardY = 140;

            g2.setColor(new Color(25, 30, 42));
            g2.fillRoundRect(cardX, cardY, 150, 240, 10, 10);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(cardX, cardY, 150, 240, 10, 10);

            if (i < party.size()) {
                Character c = party.get(i);
                g2.setColor(c.getRarity().equals("SSR") ? Color.ORANGE : Color.CYAN);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString("★ " + c.getRarity() + " ★", cardX + 45, cardY + 30);

                g2.setColor(Color.WHITE);
                g2.drawString(c.getName(), cardX + 15, cardY + 70);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Role: " + c.getRole(), cardX + 15, cardY + 100);
                g2.drawString("Level: " + c.getLevel(), cardX + 15, cardY + 125);
                g2.drawString("HP: " + c.getCurrentHp() + "/" + c.getMaxHp(), cardX + 15, cardY + 150);
                g2.drawString("ATK: " + c.getAttack(), cardX + 15, cardY + 175);
                g2.drawString("DEF: " + c.getDefense(), cardX + 15, cardY + 200);
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("[ EMPTY SLOT ]", cardX + 25, cardY + 120);
            }
        }

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g2.drawString("Karakter hasil gacha otomatis mengisi slot party yang kosong hingga 5 hero.", 200, 440);
    }

    private void renderBattleScreen(Graphics2D g2) {
        for (Enemy en : enemies) {
            en.draw(g2);
        }

        Character activeHero = partyManager.getActiveHero();
        if (activeHero != null && activeHero.isAlive()) {
            // Efek kedip saat terkena damage (I-Frame active)
            if (activeHero.iFrameTimer % 4 < 2) {
                activeHero.draw(g2);
            }
        }

        MainHUD.draw(g2, activeHero, partyManager, stageManager, enemies);
        btnBackToLobby.draw(g2);

        g2.setColor(new Color(255, 255, 255, 120));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("Tekan [ESC] atau klik tombol di atas untuk kembali ke Hub", 20, 580);
    }

    private void renderEndScreen(Graphics2D g2, boolean victory) {
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(0, 0, 950, 600);

        g2.setFont(new Font("SansSerif", Font.BOLD, 40));
        if (victory) {
            g2.setColor(new Color(46, 204, 113));
            g2.drawString("STAGE CLEARED!", 310, 200);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g2.drawString("Stage Boss Berhasil Ditumbangkan!", 335, 245);

            g2.setColor(new Color(0, 210, 255));
            g2.drawString("+ " + victoryGemsGained + " Cyber Gems", 400, 290);
            g2.setColor(new Color(255, 204, 0));
            g2.drawString("+ " + victoryCreditsGained + " Credits", 420, 325);
            g2.setColor(Color.GREEN);
            g2.drawString("Stage Berikutnya Terbuka: Stage " + stageManager.getCurrentStage(), 310, 360);
        } else {
            g2.setColor(new Color(231, 76, 60));
            g2.drawString("ALL PARTY DEFEATED!", 260, 220);

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g2.drawString("Seluruh anggota regu tempur telah gugur.", 315, 270);
            g2.drawString("Lakukan grinding EXP atau summon karakter baru!", 285, 305);
        }

        btnEndReturnHub.draw(g2);
    }
}