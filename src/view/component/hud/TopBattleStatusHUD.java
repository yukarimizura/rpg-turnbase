package view.component.hud;

import model.characters.Character;
import model.npc.Enemy;
import service.PartyManager;
import service.StageManager;
import view.component.CyberUIEffects;

import java.awt.*;
import java.util.List;

public class TopBattleStatusHUD {
    private static float displayedHpRatio = 1.0f;
    private static float displayedBossHpRatio = 1.0f;

    public static void drawTopPanels(Graphics2D g2, Character activeHero, PartyManager partyManager, StageManager stageManager, List<Enemy> enemies) {
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
            boolean ready = stageManager.canChallengeBoss(partyManager.getActiveParty());
            long pulse = (System.currentTimeMillis() / 200) % 2;
            g2.setColor(ready ? (pulse == 0 ? new Color(255, 195, 0) : new Color(255, 110, 50)) : new Color(180, 195, 215));
            g2.setFont(new Font("SansSerif", ready ? Font.BOLD : Font.PLAIN, ready ? 13 : 12));
            String msg = ready ? "★ TEKAN [B] UNTUK MEMANGGIL STAGE BOSS! ★"
                    : "Kalahkan " + stageManager.getCreepsRequired() + " Creeps untuk menantang Boss Stage [B]";
            g2.drawString(msg, ready ? 390 : 400, ready ? 40 : 38);
        }
    }
}
