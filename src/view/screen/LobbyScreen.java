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

        // 1. Top Economy Bar (Pusat Atas Layar 1920x1080)
        CyberUIEffects.drawGlassCard(g2, 260, 30, 1400, 75, new Color(0, 215, 255, 150), false);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("COMMANDER: " + profile.getPlayerName(), 300, 76);

        g2.setColor(new Color(0, 225, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("◈ CYBER GEMS: " + profile.getCyberGems(), 1020, 76);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("⬡ CREDITS: " + profile.getCredits(), 1380, 76);

        // 2. Center Showcase Card: Active Vanguard Hero (Pusat Tengah 1920x1080)
        Character leader = party.getActiveHero();
        if (leader != null) {
            Color rarCol = SpriteRenderer.getRarityColor(leader.getRarity());
            // Position: X: 640, Y: 130, Width: 640, Height: 680 (Pusat X: 960)
            CyberUIEffects.drawGlassCard(g2, 640, 130, 640, 680, rarCol, true);

            g2.setColor(new Color(0, 225, 255));
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            FontMetrics fm = g2.getFontMetrics();
            String subRoleText = "ACTIVE VANGUARD • " + leader.getSubRole().toUpperCase();
            g2.drawString(subRoleText, 960 - fm.stringWidth(subRoleText) / 2, 175);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 32));
            fm = g2.getFontMetrics();
            String title = leader.getName() + " [" + leader.getRarity() + "]";
            g2.drawString(title, 960 - fm.stringWidth(title) / 2, 220);

            g2.setColor(new Color(200, 220, 240));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            String stats = String.format("Lv.%d  |  HP: %d  |  ATK: %d  |  DEF: %d",
                    leader.getLevel(), leader.getMaxHp(), leader.getAttack(), leader.getDefense());
            fm = g2.getFontMetrics();
            g2.drawString(stats, 960 - fm.stringWidth(stats) / 2, 260);

            // Holographic Pedestal
            g2.setColor(new Color(0, 215, 255, 50));
            g2.fillOval(840, 560, 240, 48);
            g2.setColor(new Color(0, 215, 255, 160));
            g2.drawOval(840, 560, 240, 48);

            // Render Sprite Karakter Skala Besar di Tengah
            SpriteRenderer.drawCharacter(g2, leader, 960, 520, 3.2f);

            g2.setColor(new Color(255, 215, 0));
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            String upg = "Upgrade Cost: " + leader.getUpgradeCost() + " Credits";
            fm = g2.getFontMetrics();
            g2.drawString(upg, 960 - fm.stringWidth(upg) / 2, 650);

            btnTrain.draw(g2);
        }

        // 3. Side Panels (DPS & Support Squad)
        LobbySquadSidePanelRenderer.drawSquadSidePanels(g2, party);

        // 4. Bottom Navigation Buttons
        btnBattle.draw(g2);
        btnGacha.draw(g2);
        btnParty.draw(g2);
        btnSettings.draw(g2);
    }
}