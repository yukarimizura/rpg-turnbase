package view.screen;

import model.Character;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

public class PartyInventoryRenderer {
    public static void renderInventory(Graphics2D g2, List<Character> owned, List<Character> activeParty, int selectedSlot) {
        g2.setColor(new Color(25, 28, 38));
        g2.fillRect(0, 280, 950, 320);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("HERO INVENTORY (" + owned.size() + " Heroes Owned)", 60, 305);

        for (int i = 0; i < owned.size() && i < 10; i++) {
            int x = 60 + ((i % 5) * 170);
            int y = 320 + ((i / 5) * 130);
            Character hHero = owned.get(i);
            boolean inParty = activeParty.contains(hHero);

            g2.setColor(new Color(35, 40, 55));
            g2.fillRoundRect(x, y, 150, 115, 6, 6);
            g2.setColor(inParty ? new Color(46, 204, 113) : Color.GRAY);
            g2.drawRoundRect(x, y, 150, 115, 6, 6);

            g2.setColor(hHero.getRarity().equals("SSR") ? Color.ORANGE : Color.CYAN);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString("★ " + hHero.getRarity() + " " + hHero.getRole(), x + 10, y + 18);

            g2.setColor(Color.WHITE);
            g2.drawString(hHero.getName(), x + 10, y + 36);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString("Lv." + hHero.getLevel() + " | ATK:" + hHero.getAttack() + " | DEF:" + hHero.getDefense(), x + 10, y + 54);

            int btnY = y + 70;
            g2.setColor(inParty ? new Color(70, 75, 85) : new Color(39, 174, 96));
            g2.fillRoundRect(x + 10, btnY, 130, 26, 4, 4);
            g2.setColor(Color.WHITE);
            g2.drawString(inParty ? "[ ALREADY IN PARTY ]" : "EQUIP TO SLOT " + (selectedSlot + 1), x + 14, btnY + 17);
        }
    }
}