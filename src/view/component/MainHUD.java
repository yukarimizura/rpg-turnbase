package view;

import model.Character;
import model.Enemy;
import service.PartyManager;
import service.StageManager;

import java.awt.*;
import java.util.List;

public class MainHUD {

    public static void draw(Graphics2D g2, Character activeHero, PartyManager partyManager, StageManager stageManager, List<Enemy> enemies) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));

        // 1. Panel Atas: Stage Info
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(15, 15, 340, 95, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawString("STAGE: " + stageManager.getCurrentStage() + " | Grinding Creeps: " 
                + stageManager.getCreepsDefeated() + "/" + stageManager.getCreepsRequired(), 25, 35);

        // 2. Info Active Hero
        if (activeHero != null) {
            g2.drawString(activeHero.getName() + " [" + activeHero.getRarity() + " " + activeHero.getRole() + "] - Lv." + activeHero.getLevel(), 25, 55);

            // Bar HP Hero
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(25, 65, 200, 12);
            g2.setColor(new Color(46, 204, 113));
            int hpWidth = (int) (((float) activeHero.getCurrentHp() / activeHero.getMaxHp()) * 200);
            g2.fillRect(25, 65, Math.max(0, hpWidth), 12);
            g2.setColor(Color.WHITE);
            g2.drawString(activeHero.getCurrentHp() + "/" + activeHero.getMaxHp(), 235, 76);

            // Cooldown Skill & Ultimate
            String qText = activeHero.skillCdCurrent == 0 ? "[Q] SKILL READY" : String.format("[Q] %.1fs", activeHero.skillCdCurrent / 60.0f);
            String rText = activeHero.ultCdCurrent == 0 ? "[R] ULT READY" : String.format("[R] %.1fs", activeHero.ultCdCurrent / 60.0f);

            g2.setColor(activeHero.skillCdCurrent == 0 ? Color.CYAN : Color.GRAY);
            g2.drawString(qText, 25, 100);
            g2.setColor(activeHero.ultCdCurrent == 0 ? Color.YELLOW : Color.GRAY);
            g2.drawString(rText, 140, 100);
        }

        // 3. Boss Health Bar (Jika Boss Muncul)
        for (Enemy en : enemies) {
            if (en.isBoss) {
                g2.setColor(new Color(0, 0, 0, 200));
                g2.fillRect(250, 20, 420, 35);
                g2.setColor(Color.RED);
                int bossHpBar = (int) (((float) en.currentHp / en.maxHp) * 400);
                g2.fillRect(260, 35, Math.max(0, bossHpBar), 14);
                g2.setColor(Color.WHITE);
                g2.drawString("STAGE BOSS - " + en.currentHp + " / " + en.maxHp, 400, 32);
                break;
            }
        }

        // 4. Panel Bawah: Party Hero Slots [1 - 5]
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(15, 515, 550, 50, 10, 10);
        List<Character> party = partyManager.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int slotX = 25 + (i * 105);
            if (i < party.size()) {
                Character member = party.get(i);
                boolean isActive = (i == partyManager.getActiveIndex());
                g2.setColor(isActive ? Color.YELLOW : Color.LIGHT_GRAY);
                g2.drawString("[" + (i + 1) + "] " + member.getName(), slotX, 535);
                g2.drawString("Lv." + member.getLevel() + " (" + member.getRole().substring(0, 3) + ")", slotX, 552);
            } else {
                g2.setColor(Color.GRAY);
                g2.drawString("[" + (i + 1) + "] Empty", slotX, 545);
            }
        }

        // 5. Boss Status / Warning
        if (!stageManager.isBossActive()) {
            if (stageManager.canChallengeBoss(partyManager.getActiveParty())) {
                g2.setColor(Color.ORANGE);
                g2.drawString("TEKAN [B] UNTUK MEMANGGIL STAGE BOSS!", 600, 550);
            } else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawString("Syarat Boss: 5 Hero Party & 8 Creeps", 620, 550);
            }
        }
    }
}