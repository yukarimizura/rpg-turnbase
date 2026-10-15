package view.screen;

import model.Character;
import service.PartyManager;
import view.UIButton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class PartyScreen {
    public static void render(Graphics2D g2, PartyManager partyManager, UIButton btnBack) {
        btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 26));
        g2.drawString("ACTIVE COMBAT FORMATION (MAX 5)", 250, 70);

        List<Character> party = partyManager.getActiveParty();
        int activeIdx = partyManager.getActiveIndex();

        for (int i = 0; i < 5; i++) {
            int cardX = 60 + (i * 170);
            int cardY = 130;
            int cardW = 150;
            int cardH = 280;

            boolean isCurrentLeader = (i == activeIdx);

            // Card Background
            g2.setColor(new Color(25, 30, 42));
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 10, 10);

            // Highlight border jika dia Active Leader
            if (isCurrentLeader) {
                g2.setColor(new Color(241, 196, 15));
                g2.setStroke(new BasicStroke(2.5f));
            } else {
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1.0f));
            }
            g2.drawRoundRect(cardX, cardY, cardW, cardH, 10, 10);

            if (i < party.size()) {
                Character c = party.get(i);
                g2.setColor(c.getRarity().equals("SSR") ? Color.ORANGE : Color.CYAN);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2.drawString("★ " + c.getRarity() + " ★", cardX + 45, cardY + 28);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.drawString(c.getName(), cardX + 12, cardY + 60);

                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Role: " + c.getRole(), cardX + 12, cardY + 90);
                g2.drawString("Level: " + c.getLevel(), cardX + 12, cardY + 115);
                g2.drawString("HP: " + c.getCurrentHp() + "/" + c.getMaxHp(), cardX + 12, cardY + 140);
                g2.drawString("ATK: " + c.getAttack(), cardX + 12, cardY + 165);
                g2.drawString("DEF: " + c.getDefense(), cardX + 12, cardY + 190);

                // Tombol / Badge Leader
                int btnY = cardY + 225;
                if (isCurrentLeader) {
                    g2.setColor(new Color(39, 174, 96));
                    g2.fillRoundRect(cardX + 15, btnY, 120, 32, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    g2.drawString("VANGUARD (1)", cardX + 27, btnY + 20);
                } else {
                    g2.setColor(new Color(52, 73, 94));
                    g2.fillRoundRect(cardX + 15, btnY, 120, 32, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    g2.drawString("SET VANGUARD", cardX + 25, btnY + 20);
                }
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("[ EMPTY SLOT ]", cardX + 25, cardY + 140);
            }
        }

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g2.drawString("Klik tombol [SET VANGUARD] pada kartu untuk menjadikan hero sebagai karakter utama.", 180, 460);
    }

    // Helper pendeteksi slot kartu yang diklik
    public static int getClickedCardIndex(Point p, int partySize) {
        if (p == null) return -1;
        for (int i = 0; i < partySize; i++) {
            int cardX = 60 + (i * 170);
            int cardY = 130;
            int cardW = 150;
            int cardH = 280;
            if (p.x >= cardX && p.x <= cardX + cardW && p.y >= cardY && p.y <= cardY + cardH) {
                return i;
            }
        }
        return -1;
    }
}