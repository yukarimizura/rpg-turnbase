package view.component.hud;

import model.characters.Character;
import service.PartyManager;
import view.component.CyberUIEffects;

import java.awt.*;
import java.util.List;

public class BottomSquadSlotsHUD {

    public static void drawBottomSquadPanels(Graphics2D g2, PartyManager partyManager) {
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
