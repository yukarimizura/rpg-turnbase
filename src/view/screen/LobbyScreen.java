package view.screen;

import model.characters.Character;
import service.PartyManager;
import service.PlayerProfile;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;
import view.component.UIButton;
import view.screen.lobby.LobbySquadSidePanelRenderer;

import java.awt.*;

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

        LobbySquadSidePanelRenderer.drawSquadSidePanels(g2, party);

        btnBattle.draw(g2);
        btnGacha.draw(g2);
        btnParty.draw(g2);
        btnSettings.draw(g2);
    }
}