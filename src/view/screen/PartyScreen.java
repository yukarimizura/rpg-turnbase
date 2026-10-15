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
    public static int selectedActiveSlot = 0; // Slot aktif yang ingin diganti (0 - 4)

    public static void render(Graphics2D g2, PartyManager partyManager, PlayerProfile profile, UIButton btnBack) {
        btnBack.draw(g2);

        // Header Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("COMBAT SQUAD FORMATION", 220, 45);

        g2.setColor(new Color(255, 204, 0));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.drawString("Pilih Slot di atas, lalu klik hero di Inventory bawah untuk menukar anggota tim.", 220, 68);

        List<Character> activeParty = partyManager.getActiveParty();

        // 1. TOP SECTION: 5 ACTIVE COMBAT SLOTS
        for (int i = 0; i < 5; i++) {
            int x = 60 + (i * 170);
            int y = 85;
            int w = 150;
            int h = 180;

            boolean isSelectedSlot = (i == selectedActiveSlot);
            g2.setColor(new Color(25, 30, 42));
            g2.fillRoundRect(x, y, w, h, 8, 8);

            g2.setColor(isSelectedSlot ? new Color(241, 196, 15) : Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(isSelectedSlot ? 2.5f : 1.0f));
            g2.drawRoundRect(x, y, w, h, 8, 8);

            g2.setColor(isSelectedSlot ? Color.YELLOW : Color.GRAY);
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

                // Indikator klik
                g2.setColor(new Color(41, 128, 185));
                g2.fillRoundRect(x + 10, y + 145, 130, 24, 5, 5);
                g2.setColor(Color.WHITE);
                g2.drawString(isSelectedSlot ? "SELECTED SLOT" : "SELECT TO SWAP", x + 20, y + 161);
            } else {
                g2.setColor(Color.GRAY);
                g2.drawString("[ EMPTY SLOT ]", x + 25, y + 90);
            }
        }

        // 2. BOTTOM SECTION: OWNED HEROES INVENTORY
        g2.setColor(new Color(25, 28, 38));
        g2.fillRect(0, 280, 950, 320);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("HERO INVENTORY (" + profile.getOwnedHeroes().size() + " Heroes Owned)", 60, 305);

        List<Character> owned = profile.getOwnedHeroes();
        for (int i = 0; i < owned.size() && i < 10; i++) {
            int row = i / 5;
            int col = i % 5;
            int x = 60 + (col * 170);
            int y = 320 + (row * 130);
            int w = 150;
            int h = 115;

            Character hHero = owned.get(i);
            boolean inParty = activeParty.contains(hHero);

            g2.setColor(new Color(35, 40, 55));
            g2.fillRoundRect(x, y, w, h, 6, 6);
            g2.setColor(inParty ? new Color(46, 204, 113) : Color.GRAY);
            g2.drawRoundRect(x, y, w, h, 6, 6);

            g2.setColor(hHero.getRarity().equals("SSR") ? Color.ORANGE : Color.CYAN);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString("★ " + hHero.getRarity() + " " + hHero.getRole(), x + 10, y + 18);

            g2.setColor(Color.WHITE);
            g2.drawString(hHero.getName(), x + 10, y + 36);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString("Lv." + hHero.getLevel() + " | ATK:" + hHero.getAttack() + " | DEF:" + hHero.getDefense(), x + 10, y + 54);

            // Tombol Equip
            int btnY = y + 70;
            g2.setColor(inParty ? new Color(70, 75, 85) : new Color(39, 174, 96));
            g2.fillRoundRect(x + 10, btnY, 130, 26, 4, 4);
            g2.setColor(Color.WHITE);
            g2.drawString(inParty ? "[ ALREADY IN PARTY ]" : "EQUIP TO SLOT " + (selectedActiveSlot + 1), x + 14, btnY + 17);
        }
    }

    public static void handleClick(Point p, PartyManager party, PlayerProfile profile) {
        if (p == null) return;

        // Cek klik di 5 Slot Atas untuk menentukan selectedActiveSlot
        for (int i = 0; i < 5; i++) {
            int x = 60 + (i * 170);
            int y = 85;
            if (p.x >= x && p.x <= x + 150 && p.y >= y && p.y <= y + 180) {
                selectedActiveSlot = i;
                return;
            }
        }

        // Cek klik di daftar inventory bawah untuk equip
        List<Character> owned = profile.getOwnedHeroes();
        for (int i = 0; i < owned.size() && i < 10; i++) {
            int row = i / 5;
            int col = i % 5;
            int x = 60 + (col * 170);
            int y = 320 + (row * 130);
            int w = 150, h = 115;

            if (p.x >= x && p.x <= x + w && p.y >= y && p.y <= y + h) {
                Character chosen = owned.get(i);
                party.replacePartyMember(selectedActiveSlot, chosen);
                return;
            }
        }
    }
}