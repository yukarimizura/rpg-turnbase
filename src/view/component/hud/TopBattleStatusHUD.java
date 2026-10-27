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
        // 1. Panel Kiri Atas: Stage & Active DPS Status (Diperbesar untuk resolusi 1080p)
        CyberUIEffects.drawGlassCard(g2, 30, 25, 580, 180, new Color(0, 215, 255, 160), false);

        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(new Color(0, 225, 255));
        g2.drawString("STAGE " + stageManager.getCurrentStage() + "  |  CREEPS: "
                + stageManager.getCreepsDefeated() + " / " + stageManager.getCreepsRequired(), 55, 60);

        if (activeHero != null) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            g2.drawString(activeHero.getName() + " [" + activeHero.getRarity() + " Lv." + activeHero.getLevel() + "]", 55, 92);

            // Smooth HP Bar Interpolation
            float targetRatio = Math.max(0f, Math.min(1f, (float) activeHero.getCurrentHp() / activeHero.getMaxHp()));
            displayedHpRatio += (targetRatio - displayedHpRatio) * 0.2f;

            g2.setColor(new Color(30, 35, 48));
            g2.fillRoundRect(55, 106, 360, 20, 8, 8);
            g2.setColor(new Color(46, 204, 113));
            g2.fillRoundRect(55, 106, (int) (360 * displayedHpRatio), 20, 8, 8);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString(activeHero.getCurrentHp() + "/" + activeHero.getMaxHp(), 430, 122);

            // Active Buff Badges (from Inactive Support Party)
            int badgeX = 55;
            if (activeHero.bonusDefTimer > 0) {
                g2.setColor(new Color(52, 152, 219));
                g2.fillRoundRect(badgeX, 134, 160, 24, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString(String.format("DEF +%d (%.1fs)", activeHero.bonusDefAmount, activeHero.bonusDefTimer / 60f), badgeX + 10, 151);
                badgeX += 170;
            }
            if (activeHero.bonusAtkTimer > 0) {
                g2.setColor(new Color(230, 150, 20));
                g2.fillRoundRect(badgeX, 134, 160, 24, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString(String.format("ATK +%d (%.1fs)", activeHero.bonusAtkAmount, activeHero.bonusAtkTimer / 60f), badgeX + 10, 151);
            }

            // Cooldown Skill [Q] & Ultimate [R]
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            String qText = activeHero.skillCdCurrent == 0 ? "[Q] SKILL READY" : String.format("[Q] CD %.1fs", activeHero.skillCdCurrent / 60.0f);
            String rText = activeHero.ultCdCurrent == 0 ? "[R] ULT READY" : String.format("[R] CD %.1fs", activeHero.ultCdCurrent / 60.0f);
            
            g2.setColor(activeHero.skillCdCurrent == 0 ? Color.CYAN : new Color(150, 160, 175));
            g2.drawString(qText, 55, 186);
            
            g2.setColor(activeHero.ultCdCurrent == 0 ? new Color(255, 215, 0) : new Color(150, 160, 175));
            g2.drawString(rText, 280, 186);
        }

        // 2. Boss Health Bar (Jika Boss Muncul -> Di Tengah Atas 1920x1080)
        for (Enemy en : enemies) {
            if (en.isBoss) {
                // Center X: 960 (660 s/d 1260)
                CyberUIEffects.drawGlassCard(g2, 660, 25, 600, 80, new Color(231, 76, 60), true);
                
                float bossTarget = Math.max(0f, Math.min(1f, (float) en.currentHp / en.maxHp));
                displayedBossHpRatio += (bossTarget - displayedBossHpRatio) * 0.2f;
                
                g2.setColor(new Color(35, 20, 25));
                g2.fillRoundRect(685, 62, 550, 22, 8, 8);
                
                g2.setColor(new Color(231, 50, 60));
                g2.fillRoundRect(685, 62, (int) (550 * displayedBossHpRatio), 22, 8, 8);
                
                g2.setColor(new Color(255, 220, 90));
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2.drawString("⚠ OVERLORD STAGE BOSS  —  " + en.currentHp + " / " + en.maxHp, 690, 52);
                break;
            }
        }

        // 3. Boss Challenge Banner (Di Tengah Atas Layar)
        if (!stageManager.isBossActive()) {
            boolean ready = stageManager.canChallengeBoss(partyManager.getActiveParty());
            long pulse = (System.currentTimeMillis() / 200) % 2;
            
            g2.setColor(ready ? (pulse == 0 ? new Color(255, 195, 0) : new Color(255, 110, 50)) : new Color(180, 195, 215));
            g2.setFont(new Font("SansSerif", ready ? Font.BOLD : Font.PLAIN, ready ? 20 : 18));
            
            String msg = ready ? "★ TEKAN [B] UNTUK MEMANGGIL STAGE BOSS! ★"
                    : "Kalahkan " + stageManager.getCreepsRequired() + " Creeps untuk menantang Boss Stage [B]";
            
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, 960 - fm.stringWidth(msg) / 2, 65);
        }
    }
}