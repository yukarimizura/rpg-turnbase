package view.component;

import service.PartyManager;
import service.StageManager;

import java.awt.*;
import java.util.List;

import model.characters.Character;
import model.npc.Enemy;

public class MainHUD {
    private static float displayedHpRatio = 1.0f;
    private static float displayedBossHpRatio = 1.0f;

    public static void draw(Graphics2D g2, Character activeHero, PartyManager partyManager, StageManager stageManager, List<Enemy> enemies) {
        // 1. Panel Kiri Atas: Stage & Active DPS Status
        CyberUIEffects.drawGlassCard(g2, 15, 12, 345, 104, new Color(0, 215, 255, 160), false);

        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.setColor(new Color(0, 225, 255));
        g2.drawString("STAGE " + stageManager.getCurrentStage() + "  |  CREEPS: "
                + stageManager.getCreepsDefeated() + " / " + stageManager.getCreepsRequired(), 28, 32);

        if (activeHero != null) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString(activeHero.getName() + " [" + activeHero.getRarity() + " Lv." + activeHero.getLevel() + "]", 28, 50);

            // Smooth HP Bar Interpolation
            float targetRatio = Math.max(0f, Math.min(1f, (float) activeHero.getCurrentHp() / activeHero.getMaxHp()));
            displayedHpRatio += (targetRatio - displayedHpRatio) * 0.2f;

            g2.setColor(new Color(30, 35, 48));
            g2.fillRoundRect(28, 58, 210, 13, 6, 6);
            g2.setColor(new Color(46, 204, 113));
            g2.fillRoundRect(28, 58, (int) (210 * displayedHpRatio), 13, 6, 6);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString(activeHero.getCurrentHp() + "/" + activeHero.getMaxHp(), 245, 69);

            // Active Buff Badges (from Inactive Support Party)
            int badgeX = 28;
            if (activeHero.bonusDefTimer > 0) {
                g2.setColor(new Color(52, 152, 219));
                g2.fillRoundRect(badgeX, 75, 102, 16, 4, 4);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.drawString(String.format("DEF +%d (%.1fs)", activeHero.bonusDefAmount, activeHero.bonusDefTimer / 60f), badgeX + 5, 87);
                badgeX += 108;
            }
            if (activeHero.bonusAtkTimer > 0) {
                g2.setColor(new Color(230, 150, 20));
                g2.fillRoundRect(badgeX, 75, 102, 16, 4, 4);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.drawString(String.format("ATK +%d (%.1fs)", activeHero.bonusAtkAmount, activeHero.bonusAtkTimer / 60f), badgeX + 5, 87);
            }

            // Cooldown Skill [Q] & Ultimate [R]
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            String qText = activeHero.skillCdCurrent == 0 ? "[Q] SKILL READY" : String.format("[Q] CD %.1fs", activeHero.skillCdCurrent / 60.0f);
            String rText = activeHero.ultCdCurrent == 0 ? "[R] ULT READY" : String.format("[R] CD %.1fs", activeHero.ultCdCurrent / 60.0f);

            g2.setColor(activeHero.skillCdCurrent == 0 ? Color.CYAN : new Color(150, 160, 175));
            g2.drawString(qText, 28, 106);
            g2.setColor(activeHero.ultCdCurrent == 0 ? new Color(255, 215, 0) : new Color(150, 160, 175));
            g2.drawString(rText, 165, 106);
        }

        // 2. Boss Health Bar (Jika Boss Muncul)
        for (Enemy en : enemies) {
            if (en.isBoss) {
                CyberUIEffects.drawGlassCard(g2, 375, 12, 385, 50, new Color(231, 76, 60), true);
                float bossTarget = Math.max(0f, Math.min(1f, (float) en.currentHp / en.maxHp));
                displayedBossHpRatio += (bossTarget - displayedBossHpRatio) * 0.2f;

                g2.setColor(new Color(35, 20, 25));
                g2.fillRoundRect(390, 38, 355, 14, 6, 6);
                g2.setColor(new Color(231, 50, 60));
                g2.fillRoundRect(390, 38, (int) (355 * displayedBossHpRatio), 14, 6, 6);

                g2.setColor(new Color(255, 220, 90));
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("⚠ OVERLORD STAGE BOSS  —  " + en.currentHp + " / " + en.maxHp, 395, 31);
                break;
            }
        }

        // 3. Boss Challenge Banner
        if (!stageManager.isBossActive()) {
            if (stageManager.canChallengeBoss(partyManager.getActiveParty())) {
                long pulse = (System.currentTimeMillis() / 200) % 2;
                g2.setColor(pulse == 0 ? new Color(255, 195, 0) : new Color(255, 110, 50));
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.drawString("★ TEKAN [B] UNTUK MEMANGGIL STAGE BOSS! ★", 390, 40);
            } else {
                g2.setColor(new Color(180, 195, 215));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Kalahkan " + stageManager.getCreepsRequired() + " Creeps untuk menantang Boss Stage [B]", 400, 38);
            }
        }

        // 4. Panel Bawah Kiri: 5 ACTIVE DPS SQUAD SLOTS [1 - 5]
        CyberUIEffects.drawGlassCard(g2, 15, 502, 455, 68, new Color(231, 76, 60, 170), false);
        g2.setColor(new Color(255, 120, 110));
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2.drawString("ACTIVE DPS SQUAD (Switch Manual: Tekan Angka [1] - [5])", 25, 516);

        List<Character> activeParty = partyManager.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int slotX = 24 + (i * 88);
            int slotY = 522;
            boolean isCurrent = (i == partyManager.getActiveIndex());
            g2.setColor(isCurrent ? new Color(75, 40, 45) : new Color(28, 32, 45));
            g2.fillRoundRect(slotX, slotY, 82, 42, 6, 6);
            g2.setColor(isCurrent ? new Color(255, 215, 0) : new Color(90, 100, 120));
            g2.drawRoundRect(slotX, slotY, 82, 42, 6, 6);

            if (i < activeParty.size()) {
                Character m = activeParty.get(i);
                g2.setColor(isCurrent ? Color.YELLOW : Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                String shortName = m.getSubRole().length() > 9 ? m.getSubRole().substring(0, 9) : m.getSubRole();
                g2.drawString("[" + (i + 1) + "] " + shortName, slotX + 5, slotY + 14);

                // Mini HP bar
                float hpR = Math.max(0f, (float) m.getCurrentHp() / m.getMaxHp());
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(slotX + 5, slotY + 20, 72, 5);
                g2.setColor(m.isAlive() ? new Color(46, 204, 113) : Color.RED);
                g2.fillRect(slotX + 5, slotY + 20, (int) (72 * hpR), 5);

                g2.setColor(new Color(190, 210, 230));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                g2.drawString("Lv." + m.getLevel() + (m.skillCdCurrent == 0 ? " Q:RDY" : " Q:" + (m.skillCdCurrent / 60 + 1) + "s"), slotX + 5, slotY + 36);
            } else {
                g2.setColor(new Color(100, 110, 125));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString("[" + (i + 1) + "] Empty", slotX + 14, slotY + 25);
            }
        }

        // 5. Panel Bawah Kanan: 5 INACTIVE AUTO-SUPPORT SQUAD SLOTS [S1 - S5]
        CyberUIEffects.drawGlassCard(g2, 480, 502, 455, 68, new Color(46, 204, 113, 170), false);
        g2.setColor(new Color(100, 240, 160));
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2.drawString("INACTIVE AUTO-SUPPORT SQUAD (Off-Field Auto Heal & Auto Buff)", 490, 516);

        List<Character> supportParty = partyManager.getInactiveParty();
        for (int i = 0; i < 5; i++) {
            int slotX = 489 + (i * 88);
            int slotY = 522;
            g2.setColor(new Color(25, 36, 38));
            g2.fillRoundRect(slotX, slotY, 82, 42, 6, 6);

            if (i < supportParty.size()) {
                Character s = supportParty.get(i);
                boolean ready = (s.skillCdCurrent == 0);
                g2.setColor(ready ? new Color(46, 204, 113) : new Color(80, 110, 130));
                g2.drawRoundRect(slotX, slotY, 82, 42, 6, 6);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.drawString("[S" + (i + 1) + "] " + s.getRole().substring(0, 3), slotX + 5, slotY + 14);

                // Cooldown progress bar
                float cdRatio = 1.0f - ((float) s.skillCdCurrent / Math.max(1, s.skillCdMax));
                g2.setColor(new Color(35, 40, 50));
                g2.fillRect(slotX + 5, slotY + 20, 72, 5);
                g2.setColor(ready ? new Color(0, 230, 180) : new Color(241, 196, 15));
                g2.fillRect(slotX + 5, slotY + 20, (int) (72 * cdRatio), 5);

                g2.setColor(ready ? new Color(110, 255, 170) : new Color(210, 210, 210));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                g2.drawString(ready ? "AUTO: STANDBY" : String.format("CD: %.1fs", s.skillCdCurrent / 60f), slotX + 5, slotY + 36);
            } else {
                g2.setColor(new Color(75, 85, 100));
                g2.drawRoundRect(slotX, slotY, 82, 42, 6, 6);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString("[S" + (i + 1) + "] Empty", slotX + 12, slotY + 25);
            }
        }
    }
}