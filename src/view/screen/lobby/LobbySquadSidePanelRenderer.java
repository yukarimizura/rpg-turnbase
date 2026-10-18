package view.screen.lobby;

import model.characters.Character;
import service.PartyManager;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.*;
import java.util.List;

public class LobbySquadSidePanelRenderer {

    public static void drawSquadSidePanels(Graphics2D g2, PartyManager party) {
        // Left Side Panel: Active DPS Squad Overview (5 Slots)
        CyberUIEffects.drawGlassCard(g2, 25, 90, 245, 345, new Color(231, 76, 60, 160), false);
        g2.setColor(new Color(255, 110, 100));
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString("ACTIVE DPS SQUAD (" + party.getActiveParty().size() + "/5)", 42, 118);
        List<Character> actList = party.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int ry = 135 + i * 56;
            g2.setColor(new Color(30, 36, 52));
            g2.fillRoundRect(38, ry, 218, 46, 8, 8);
            if (i < actList.size()) {
                Character c = actList.get(i);
                SpriteRenderer.drawCharacter(g2, c, 62, ry + 28, 0.7f);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                g2.drawString("[" + (i + 1) + "] " + c.getName(), 86, ry + 19);
                g2.setColor(new Color(180, 200, 225));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString("Lv." + c.getLevel() + " • ATK " + c.getAttack(), 86, ry + 36);
            } else {
                g2.setColor(new Color(110, 120, 140));
                g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
                g2.drawString("[" + (i + 1) + "] Empty DPS Slot", 65, ry + 27);
            }
        }

        // Right Side Panel: Inactive Auto-Support Squad Overview (5 Slots)
        CyberUIEffects.drawGlassCard(g2, 680, 90, 245, 345, new Color(46, 204, 113, 160), false);
        g2.setColor(new Color(90, 235, 155));
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString("AUTO-SUPPORT SQUAD (" + party.getInactiveParty().size() + "/5)", 695, 118);
        List<Character> supList = party.getInactiveParty();
        for (int i = 0; i < 5; i++) {
            int ry = 135 + i * 56;
            g2.setColor(new Color(26, 38, 42));
            g2.fillRoundRect(693, ry, 218, 46, 8, 8);
            if (i < supList.size()) {
                Character c = supList.get(i);
                SpriteRenderer.drawCharacter(g2, c, 717, ry + 28, 0.7f);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                g2.drawString("[S" + (i + 1) + "] " + c.getName(), 741, ry + 19);
                g2.setColor(new Color(140, 230, 180));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString(c.getRole() + " • Auto Ready", 741, ry + 36);
            } else {
                g2.setColor(new Color(110, 120, 140));
                g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
                g2.drawString("[S" + (i + 1) + "] Empty Support Slot", 715, ry + 27);
            }
        }
    }
}
