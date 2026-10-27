package view.screen;

import engine.GameCanvas;
import model.characters.Character;
import service.PartyManager;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;

public class PartyInventoryRenderer {

    public static void renderInventory(Graphics2D g2, List<Character> owned, PartyManager partyManager, int selectedSlot, int page) {
        int width = GameCanvas.BASE_WIDTH;   // 1920
        int height = GameCanvas.BASE_HEIGHT; // 1080

        // Background Panel Inventory (Bawah layar Y: 430 s/d 1080)
        g2.setColor(new Color(14, 18, 28, 235));
        g2.fillRect(0, 430, width, height - 430);
        
        g2.setColor(new Color(0, 215, 255, 90));
        g2.drawLine(0, 430, width, 430);

        int maxPage = Math.max(0, (owned.size() - 1) / 10);

        // Header Title Inventory
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("HERO INVENTORY (" + owned.size() + " Owned) - Klik Hero untuk Memasang ke Slot Sesuai Role", 60, 462);

        // Pagination Controls (Kanan Atas Panel Inventory)
        int btnW = 110, btnH = 32;
        
        // Button PREV
        g2.setColor(page > 0 ? new Color(41, 128, 185) : new Color(45, 50, 65));
        g2.fillRoundRect(1420, 442, btnW, btnH, 8, 8);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("< PREV", 1445, 463);

        // Button NEXT
        g2.setColor(page < maxPage ? new Color(41, 128, 185) : new Color(45, 50, 65));
        g2.fillRoundRect(1550, 442, btnW, btnH, 8, 8);
        g2.setColor(Color.WHITE);
        g2.drawString("NEXT >", 1575, 463);

        // Page Counter Indicator
        g2.setColor(new Color(200, 215, 230));
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString("Page " + (page + 1) + " / " + (maxPage + 1), 1310, 463);

        // Grid Kartu Hero Inventory (5 Kolom x 2 Baris)
        int slotW = 330;
        int slotH = 125;
        int gapX = 30;
        int gapY = 20;
        int startX = 60;
        int startY = 485;

        int startIdx = page * 10;
        for (int i = 0; i < 10 && (startIdx + i) < owned.size(); i++) {
            int col = i % 5;
            int row = i / 5;

            int x = startX + col * (slotW + gapX);
            int y = startY + row * (slotH + gapY);

            Character hHero = owned.get(startIdx + i);
            boolean inActive = partyManager.getActiveParty().contains(hHero);
            boolean inInactive = partyManager.getInactiveParty().contains(hHero);
            boolean inParty = inActive || inInactive;

            Color rarCol = SpriteRenderer.getRarityColor(hHero.getRarity());
            Color borderCol = inParty ? new Color(46, 204, 113) : rarCol;
            CyberUIEffects.drawGlassCard(g2, x, y, slotW, slotH, borderCol, inParty);

            // Character Mini Sprite
            SpriteRenderer.drawCharacter(g2, hHero, x + 50, y + 68, 1.4f);

            // Rarity Badge & Role
            g2.setColor(rarCol);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.drawString("★ " + hHero.getRarity() + "  " + hHero.getRole(), x + 105, y + 28);

            // Hero Name
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            String name = hHero.getName();
            if (name.length() > 18) name = name.substring(0, 17) + "…";
            g2.drawString(name, x + 105, y + 50);

            // Stats Information
            g2.setColor(new Color(190, 205, 225));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2.drawString("Lv." + hHero.getLevel() + "  |  ATK: " + hHero.getAttack(), x + 105, y + 70);
            g2.drawString("HP: " + hHero.getMaxHp() + "  |  DEF: " + hHero.getDefense(), x + 105, y + 88);

            // Action Button / Status Badge
            int btnY = y + 96;
            int btnWidth = slotW - 30;
            
            if (inParty) {
                g2.setColor(new Color(45, 65, 60));
                g2.fillRoundRect(x + 15, btnY, btnWidth, 24, 6, 6);
                g2.setColor(new Color(100, 255, 160));
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                
                String statusMsg = inActive ? "✓ IN ACTIVE DPS SQUAD" : "✓ IN AUTO-SUPPORT";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(statusMsg, x + 15 + (btnWidth - fm.stringWidth(statusMsg)) / 2, btnY + 16);
            } else {
                boolean isDps = hHero.isVanguardEligible();
                g2.setColor(isDps ? new Color(192, 57, 43) : new Color(39, 174, 96));
                g2.fillRoundRect(x + 15, btnY, btnWidth, 24, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                
                String targetLabel = isDps
                        ? ("EQUIP DPS SLOT " + ((selectedSlot < 5 ? selectedSlot : 0) + 1))
                        : ("EQUIP SUPPORT S" + ((selectedSlot >= 5 ? selectedSlot - 5 : 0) + 1));
                
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(targetLabel, x + 15 + (btnWidth - fm.stringWidth(targetLabel)) / 2, btnY + 16);
            }
        }
    }
}