package view.screen;

import model.Character;
import service.PartyManager;
import service.PlayerProfile;
import view.UIButton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class PartyScreen {
    public static int selectedActiveSlot = 0;

    public static void render(Graphics2D g2, PartyManager partyManager, PlayerProfile profile, UIButton btnBack) {
        btnBack.draw(g2);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("COMBAT SQUAD FORMATION", 220, 45);
        g2.setColor(new Color(255, 204, 0));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.drawString("Pilih Slot di atas, lalu klik hero di Inventory bawah untuk menukar anggota tim.", 220, 68);

        List<Character> activeParty = partyManager.getActiveParty();
        for (int i = 0; i < 5; i++) {
            int x = 60 + (i * 170), y = 85;
            boolean isSelected = (i == selectedActiveSlot);
            g2.setColor(new Color(25, 30, 42));
            g2.fillRoundRect(x, y, 150, 180, 8, 8);
            g2.setColor(isSelected ? new Color(241, 196, 15) : Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(isSelected ? 2.5f : 1.0f));
            g2.drawRoundRect(x, y, 150, 180, 8, 8);

            g2.setColor(isSelected ? Color.YELLOW : Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("SLOT " + (i + 1) + (i == 0 ? " [LEADER]" : ""), x + 15, y + 20);

            if (i < activeParty.size()) {
                Character c = activeParty.get(i);
                g2.setColor(c.getRarity().equals("SSR") ? Color.ORANGE : Color.CYAN);
                g2.drawString("★ " + c.getRarity(), x + 15, y + 42);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString(c.getName(), x + 15, y + 65);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.drawString("Role: " + c.getRole(), x + 15, y + 88);
                g2.drawString("Lv." + c.getLevel() + " | HP:" + c.getCurrentHp(), x + 15, y + 108);
                g2.drawString("ATK:" + c.getAttack() + " DEF:" + c.getDefense(), x + 15, y + 128);
                g2.setColor(new Color(41, 128, 185));
                g2.fillRoundRect(x + 10, y + 145, 130, 24, 5, 5);
                g2.setColor(Color.WHITE);
                g2.drawString(isSelected ? "SELECTED SLOT" : "SELECT TO SWAP", x + 20, y + 161);
            } else {
                g2.setColor(Color.GRAY);
                g2.drawString("[ EMPTY SLOT ]", x + 25, y + 90);
            }
        }
        PartyInventoryRenderer.renderInventory(g2, profile.getOwnedHeroes(), activeParty, selectedActiveSlot);
    }

    public static void handleClick(Point p, PartyManager party, PlayerProfile profile) {
        if (p == null) return;
        for (int i = 0; i < 5; i++) {
            if (p.x >= 60 + (i * 170) && p.x <= 210 + (i * 170) && p.y >= 85 && p.y <= 265) {
                selectedActiveSlot = i; return;
            }
        }
        List<Character> owned = profile.getOwnedHeroes();
        for (int i = 0; i < owned.size() && i < 10; i++) {
            int x = 60 + ((i % 5) * 170), y = 320 + ((i / 5) * 130);
            if (p.x >= x && p.x <= x + 150 && p.y >= y && p.y <= y + 115) {
                party.replacePartyMember(selectedActiveSlot, owned.get(i)); return;
            }
        }
    }
}