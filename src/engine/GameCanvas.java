package engine;

import model.Character;
import model.Enemy;
import service.PartyManager;
import service.StageManager;
import view.MainHUD;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameCanvas extends JPanel {
    private PartyManager partyManager;
    private StageManager stageManager;
    private InputHandler input;
    private List<Enemy> enemies;

    public GameCanvas(PartyManager partyManager, StageManager stageManager, InputHandler input) {
        this.partyManager = partyManager;
        this.stageManager = stageManager;
        this.input = input;
        this.enemies = new ArrayList<>();

        setPreferredSize(new Dimension(950, 600));
        setBackground(new Color(20, 22, 28));
        setFocusable(true);
        addKeyListener(input);

        spawnCreeps();
    }

    private void spawnCreeps() {
        enemies.clear();
        for (int i = 0; i < 6; i++) {
            float rx = (float) (Math.random() * 800 + 70);
            float ry = (float) (Math.random() * 400 + 120);
            enemies.add(new Enemy(rx, ry, false, stageManager.getCurrentStage()));
        }
    }

    public void updateState() {
        Character activeHero = partyManager.getActiveHero();
        if (activeHero == null) return;

        // 1. Tangani Switch Hero 1 - 5
        if (input.selectedHeroSlot != -1) {
            partyManager.switchHero(input.selectedHeroSlot);
            input.selectedHeroSlot = -1;
        }

        // 2. Input Gerakan WASD
        activeHero.vx = 0;
        activeHero.vy = 0;
        if (input.up) activeHero.vy -= activeHero.speed;
        if (input.down) activeHero.vy += activeHero.speed;
        if (input.left) activeHero.vx -= activeHero.speed;
        if (input.right) activeHero.vx += activeHero.speed;
        activeHero.updatePhysics();

        // 3. Tombol Skill & Ultimate
        if (input.skillCast) {
            activeHero.castSkill(enemies);
            input.skillCast = false;
        }
        if (input.ultCast) {
            activeHero.castUltimate(enemies);
            input.ultCast = false;
        }

        // 4. Tantang Boss jika siap
        if (input.bossChallengeKey && stageManager.canChallengeBoss(partyManager.getActiveParty()) && !stageManager.isBossActive()) {
            enemies.clear();
            enemies.add(new Enemy(475, 200, true, stageManager.getCurrentStage()));
            stageManager.startBossBattle();
            input.bossChallengeKey = false;
        }

        // 5. Update Musuh & Hitbox
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy en = it.next();
            en.updateAI(activeHero.x, activeHero.y);

            // Tabrakan kontak musuh memberi damage
            float dist = (float) Math.hypot(en.x - activeHero.x, en.y - activeHero.y);
            if (dist < (en.size / 2 + activeHero.size / 2)) {
                activeHero.takeDamage(en.attack / 8);
            }

            // Musuh mati
            if (en.currentHp <= 0) {
                activeHero.gainExp(en.expReward);
                if (en.isBoss) {
                    stageManager.onBossDefeated();
                    spawnCreeps();
                    break;
                } else {
                    stageManager.onCreepKilled();
                }
                it.remove();
            }
        }

        // Respawn creep jika habis saat grinding
        if (enemies.isEmpty() && !stageManager.isBossActive()) {
            spawnCreeps();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Render musuh
        for (Enemy en : enemies) {
            en.draw(g2);
        }

        // Render hero aktif
        Character activeHero = partyManager.getActiveHero();
        if (activeHero != null && activeHero.isAlive()) {
            activeHero.draw(g2);
        }

        // Render HUD overlay
        MainHUD.draw(g2, activeHero, partyManager, stageManager, enemies);
    }
}