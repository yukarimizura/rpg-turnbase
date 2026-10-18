package view.screen;

import service.PartyManager;
import service.PlayerProfile;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;
import view.component.UIButton;

import java.awt.*;
import java.util.List;

import model.characters.Character;

public class LobbyScreen {
    public static void render(Graphics2D g2, PlayerProfile profile, PartyManager party, 
                             UIButton btnBattle, UIButton btnGacha, UIButton btnParty, UIButton btnTrain, UIButton btnSettings) {
        CyberUIEffects.drawCyberBackground(g2, false);

        // Top Economy Bar
        CyberUIEffects.drawGlassCard(g2, 20, 14, 910, 56, new Color(0, 215, 255, 150), false);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("COMMANDER: " + profile.getPlayerName(), 40, 48);

        g2.setColor(new Color(0, 225, 255));
        g2.drawString("◈ CYBER GEMS: " + profile.getCyberGems(), 510, 48);

        g2.setColor(new Color(255, 215, 0));
        g2.drawString("⬡ CREDITS: " + profile.getCredits(), 735, 48);

        // Center Showcase Card: Active Vanguard Hero
        Character leader = party.getActiveHero();
        if (leader != null) {
            Color rarCol = SpriteRenderer.getRarityColor(leader.getRarity());
            CyberUIEffects.drawGlassCard(g2, 290, 90, 370, 345, rarCol, true);

            g2.setColor(new Color(0, 225, 255));
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("ACTIVE VANGUARD • " + leader.getSubRole().toUpperCase(), 355, 118);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            String title = leader.getName() + " [" + leader.getRarity() + "]";
            g2.drawString(title, 475 - fm.stringWidth(title) / 2, 145);

            g2.setColor(new Color(200, 220, 240));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            String stats = String.format("Lv.%d  |  HP: %d  |  ATK: %d  |  DEF: %d",
                    leader.getLevel(), leader.getMaxHp(), leader.getAttack(), leader.getDefense());
            fm = g2.getFontMetrics();
            g2.drawString(stats, 475 - fm.stringWidth(stats) / 2, 170);

            // Holographic Pedestal
            g2.setColor(new Color(0, 215, 255, 50));
            g2.fillOval(415, 305, 120, 26);
            g2.setColor(new Color(0, 215, 255, 160));
            g2.drawOval(415, 305, 120, 26);

            SpriteRenderer.drawCharacter(g2, leader, 475, 282, 1.85f);

            g2.setColor(new Color(255, 215, 0));
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            String upg = "Upgrade Cost: " + leader.getUpgradeCost() + " Credits";
            fm = g2.getFontMetrics();
            g2.drawString(upg, 475 - fm.stringWidth(upg) / 2, 362);

            btnTrain.draw(g2);
        }

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

        btnBattle.draw(g2);
        btnGacha.draw(g2);
        btnParty.draw(g2);
        btnSettings.draw(g2);
    }
}