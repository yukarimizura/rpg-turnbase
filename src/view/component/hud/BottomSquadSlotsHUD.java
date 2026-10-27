package view.component.hud;

import model.characters.Character;
import service.PartyManager;
import view.component.CyberUIEffects;

import java.awt.*;
import java.util.List;

public class BottomSquadSlotsHUD {

    public static void drawBottomSquadPanels(Graphics2D g2, PartyManager partyManager) {
        // 1. Panel Bawah Kiri: 5 ACTIVE DPS SQUAD SLOTS [1 - 5] (Dimensi 1080p)
        CyberUIEffects.drawGlassCard(g2, 30, 890, 910, 150, new Color(231, 76, 60, 170), false);
        g2.setColor(new Color(255, 120, 110));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("ACTIVE DPS SQUAD (Switch Manual: Tekan Angka [1] - [5])", 50, 922);

        List<Character> activeParty = partyManager.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int slotX = 50 + (i * 172);
            int slotY = 938;
            boolean isCurrent = (i == partyManager.getActiveIndex());
            
            g2.setColor(isCurrent ? new Color(75, 40, 45) : new Color(28, 32, 45));
            g2.fillRoundRect(slotX, slotY, 162, 88, 10, 10);
            
            g2.setColor(isCurrent ? new Color(255, 215, 0) : new Color(90, 100, 120));
            g2.setStroke(new BasicStroke(isCurrent ? 2.5f : 1.2f));
            g2.drawRoundRect(slotX, slotY, 162, 88, 10, 10);

            if (i < activeParty.size()) {
                Character m = activeParty.get(i);
                g2.setColor(isCurrent ? Color.YELLOW : Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                String shortName = m.getSubRole().length() > 11 ? m.getSubRole().substring(0, 11) : m.getSubRole();
                g2.drawString("[" + (i + 1) + "] " + shortName, slotX + 12, slotY + 28);

                // Mini HP bar
                float hpR = Math.max(0f, (float) m.getCurrentHp() / m.getMaxHp());
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(slotX + 12, slotY + 38, 138, 10);
                g2.setColor(m.isAlive() ? new Color(46, 204, 113) : Color.RED);
                g2.fillRect(slotX + 12, slotY + 38, (int) (138 * hpR), 10);
                g2.setColor(Color.GRAY);
                g2.drawRect(slotX + 12, slotY + 38, 138, 10);

                g2.setColor(new Color(190, 210, 230));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.drawString("Lv." + m.getLevel() + (m.skillCdCurrent == 0 ? " Q:RDY" : " Q:" + (m.skillCdCurrent / 60 + 1) + "s"), slotX + 12, slotY + 72);
            } else {
                g2.setColor(new Color(100, 110, 125));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
                g2.drawString("[" + (i + 1) + "] Empty", slotX + 42, slotY + 52);
            }
        }

        // 2. Panel Bawah Kanan: 5 INACTIVE AUTO-SUPPORT SQUAD SLOTS [S1 - S5] (Dimensi 1080p)
        CyberUIEffects.drawGlassCard(g2, 980, 890, 910, 150, new Color(46, 204, 113, 170), false);
        g2.setColor(new Color(100, 240, 160));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("INACTIVE AUTO-SUPPORT SQUAD (Off-Field Auto Heal & Auto Buff)", 1000, 922);

        List<Character> supportParty = partyManager.getInactiveParty();
        for (int i = 0; i < 5; i++) {
            int slotX = 1000 + (i * 172);
            int slotY = 938;
            
            g2.setColor(new Color(25, 36, 38));
            g2.fillRoundRect(slotX, slotY, 162, 88, 10, 10);

            if (i < supportParty.size()) {
                Character s = supportParty.get(i);
                boolean ready = (s.skillCdCurrent == 0);
                
                g2.setColor(ready ? new Color(46, 204, 113) : new Color(80, 110, 130));
                g2.setStroke(new BasicStroke(ready ? 2.0f : 1.2f));
                g2.drawRoundRect(slotX, slotY, 162, 88, 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                String roleText = s.getRole().length() > 8 ? s.getRole().substring(0, 8) : s.getRole();
                g2.drawString("[S" + (i + 1) + "] " + roleText, slotX + 12, slotY + 28);

                // Cooldown progress bar
                float cdRatio = 1.0f - ((float) s.skillCdCurrent / Math.max(1, s.skillCdMax));
                g2.setColor(new Color(35, 40, 50));
                g2.fillRect(slotX + 12, slotY + 38, 138, 10);
                g2.setColor(ready ? new Color(0, 230, 180) : new Color(241, 196, 15));
                g2.fillRect(slotX + 12, slotY + 38, (int) (138 * cdRatio), 10);
                g2.setColor(Color.GRAY);
                g2.drawRect(slotX + 12, slotY + 38, 138, 10);

                g2.setColor(ready ? new Color(110, 255, 170) : new Color(210, 210, 210));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.drawString(ready ? "AUTO: STANDBY" : String.format("CD: %.1fs", s.skillCdCurrent / 60f), slotX + 12, slotY + 72);
            } else {
                g2.setColor(new Color(75, 85, 100));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(slotX, slotY, 162, 88, 10, 10);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
                g2.drawString("[S" + (i + 1) + "] Empty", slotX + 42, slotY + 52);
            }
        }
    }
}