package view.screen;

import model.Character;
import service.PartyManager;
import view.UIButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

public class PartyScreen {
    public static void render(Graphics2D g2, PartyManager partyManager, UIButton btnBack) {
        btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 26));
        g2.drawString("ACTIVE COMBAT FORMATION (MAX 5)", 250, 70);

        List<Character> party = partyManager.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int cardX = 60 + (i * 170);
            int cardY = 140;

            g2.setColor(new Color(25, 30, 42));
            g2.fillRoundRect(cardX, cardY, 150, 240, 10, 10);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(cardX, cardY, 150, 240, 10, 10);

            if (i < party.size()) {
                Character c = party.get(i);
                g2.setColor(c.getRarity().equals("SSR") ? Color.ORANGE : Color.CYAN);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString("★ " + c.getRarity() + " ★", cardX + 45, cardY + 30);

                g2.setColor(Color.WHITE);
                g2.drawString(c.getName(), cardX + 15, cardY + 70);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Role: " + c.getRole(), cardX + 15, cardY + 100);
                g2.drawString("Level: " + c.getLevel(), cardX + 15, cardY + 125);
                g2.drawString("HP: " + c.getCurrentHp() + "/" + c.getMaxHp(), cardX + 15, cardY + 150);
                g2.drawString("ATK: " + c.getAttack(), cardX + 15, cardY + 175);
                g2.drawString("DEF: " + c.getDefense(), cardX + 15, cardY + 200);
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("[ EMPTY SLOT ]", cardX + 25, cardY + 120);
            }
        }

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g2.drawString("Karakter hasil gacha otomatis mengisi slot party yang kosong hingga 5 hero.", 200, 440);
    }
}