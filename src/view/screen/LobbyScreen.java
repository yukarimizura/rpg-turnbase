package view.screen;

import service.PartyManager;
import service.PlayerProfile;
import view.UIButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import model.characters.Character;

public class LobbyScreen {
    public static void render(Graphics2D g2, PlayerProfile profile, PartyManager party, 
                             UIButton btnBattle, UIButton btnGacha, UIButton btnParty, UIButton btnTrain, UIButton btnSettings) {
        // Top Economy Bar
        g2.setColor(new Color(25, 28, 38));
        g2.fillRect(0, 0, 950, 70);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("COMMANDER: " + profile.getPlayerName(), 30, 40);

        g2.setColor(new Color(0, 210, 255));
        g2.drawString("CYBER GEMS: " + profile.getCyberGems(), 560, 40);

        g2.setColor(new Color(255, 204, 0));
        g2.drawString("CREDITS: " + profile.getCredits(), 760, 40);

        // Hero Vanguard Display
        Character leader = party.getActiveHero();
        if (leader != null) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g2.drawString("Active Vanguard (" + leader.getSubRole() + "):", 360, 150);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            g2.drawString(leader.getName() + " [" + leader.getRarity() + "]", 340, 185);
            
            g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g2.drawString(String.format("Level: %d | HP: %d/%d | ATK: %d | DEF: %d", 
                    leader.getLevel(), leader.getCurrentHp(), leader.getMaxHp(), leader.getAttack(), leader.getDefense()), 300, 215);

            g2.setColor(new Color(255, 204, 0));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2.drawString("Upgrade Cost: " + leader.getUpgradeCost() + " Credits", 390, 245);

            leader.x = 475; 
            leader.y = 310;
            leader.draw(g2);

            btnTrain.draw(g2);
        }

        btnBattle.draw(g2);
        btnGacha.draw(g2);
        btnParty.draw(g2);
        btnSettings.draw(g2);
    }
}