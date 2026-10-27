package view.screen.lobby;

import model.characters.Character;
import service.PartyManager;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.*;
import java.util.List;

public class LobbySquadSidePanelRenderer {

    public static void drawSquadSidePanels(Graphics2D g2, PartyManager party) {
        // Left Side Panel: Active DPS Squad Overview (5 Slots) - Layout 1080p (X: 180, Y: 130, Width: 420, Height: 680)
        CyberUIEffects.drawGlassCard(g2, 180, 130, 420, 680, new Color(231, 76, 60, 160), false);
        g2.setColor(new Color(255, 110, 100));
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("ACTIVE DPS SQUAD (" + party.getActiveParty().size() + "/5)", 210, 175);

        List<Character> actList = party.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int ry = 200 + (i * 115);
            g2.setColor(new Color(30, 36, 52));
            g2.fillRoundRect(210, ry, 360, 95, 12, 12);

            if (i < actList.size()) {
                Character c = actList.get(i);
                // Sprite Render Miniatur Hero di Slot Kiri
                SpriteRenderer.drawCharacter(g2, c, 255, ry + 56, 1.35f);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2.drawString("[" + (i + 1) + "] " + c.getName(), 300, ry + 40);

                g2.setColor(new Color(180, 200, 225));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
                g2.drawString("Lv." + c.getLevel() + "  •  ATK " + c.getAttack(), 300, ry + 70);
            } else {
                g2.setColor(new Color(110, 120, 140));
                g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
                g2.drawString("[" + (i + 1) + "] Empty DPS Slot", 260, ry + 55);
            }
        }

        // Right Side Panel: Inactive Auto-Support Squad Overview (5 Slots) - Layout 1080p (X: 1320, Y: 130, Width: 420, Height: 680)
        CyberUIEffects.drawGlassCard(g2, 1320, 130, 420, 680, new Color(46, 204, 113, 160), false);
        g2.setColor(new Color(90, 235, 155));
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("AUTO-SUPPORT SQUAD (" + party.getInactiveParty().size() + "/5)", 1350, 175);

        List<Character> supList = party.getInactiveParty();
        for (int i = 0; i < 5; i++) {
            int ry = 200 + (i * 115);
            g2.setColor(new Color(26, 38, 42));
            g2.fillRoundRect(1350, ry, 360, 95, 12, 12);

            if (i < supList.size()) {
                Character c = supList.get(i);
                // Sprite Render Miniatur Hero di Slot Kanan
                SpriteRenderer.drawCharacter(g2, c, 1395, ry + 56, 1.35f);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2.drawString("[S" + (i + 1) + "] " + c.getName(), 1440, ry + 40);

                g2.setColor(new Color(140, 230, 180));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
                g2.drawString(c.getRole() + "  •  Auto Ready", 1440, ry + 70);
            } else {
                g2.setColor(new Color(110, 120, 140));
                g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
                g2.drawString("[S" + (i + 1) + "] Empty Support Slot", 1390, ry + 55);
            }
        }
    }
}