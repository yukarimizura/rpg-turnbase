package view.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import model.characters.Character;
import service.PartyManager;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

public class PartyInventoryRenderer {
    public static void renderInventory(Graphics2D g2, List<Character> owned, PartyManager partyManager, int selectedSlot, int page) {
        g2.setColor(new Color(14, 18, 28, 235));
        g2.fillRect(0, 308, 950, 292);
        g2.setColor(new Color(0, 215, 255, 90));
        g2.drawLine(0, 308, 950, 308);

        int maxPage = Math.max(0, (owned.size() - 1) / 10);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("HERO INVENTORY (" + owned.size() + " Owned) - Klik Hero untuk Memasang ke Slot Sesuai Role", 40, 330);

        // Pagination Controls
        g2.setColor(page > 0 ? new Color(41, 128, 185) : new Color(45, 50, 65));
        g2.fillRoundRect(720, 313, 85, 22, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2.drawString("< PREV", 742, 328);

        g2.setColor(page < maxPage ? new Color(41, 128, 185) : new Color(45, 50, 65));
        g2.fillRoundRect(815, 313, 85, 22, 6, 6);
        g2.setColor(Color.WHITE);
        g2.drawString("NEXT >", 837, 328);

        g2.setColor(new Color(200, 215, 230));
        g2.drawString("Page " + (page + 1) + "/" + (maxPage + 1), 645, 328);

        int startIdx = page * 10;
        for (int i = 0; i < 10 && (startIdx + i) < owned.size(); i++) {
            int x = 40 + ((i % 5) * 176);
            int y = 344 + ((i / 5) * 122);
            int w = 164, h = 114;
            Character hHero = owned.get(startIdx + i);
            boolean inActive = partyManager.getActiveParty().contains(hHero);
            boolean inInactive = partyManager.getInactiveParty().contains(hHero);
            boolean inParty = inActive || inInactive;

            Color rarCol = SpriteRenderer.getRarityColor(hHero.getRarity());
            Color borderCol = inParty ? new Color(46, 204, 113) : rarCol;
            CyberUIEffects.drawGlassCard(g2, x, y, w, h, borderCol, inParty);

            // Mini Character Sprite
            SpriteRenderer.drawCharacter(g2, hHero, x + 30, y + 54, 0.85f);

            g2.setColor(rarCol);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString("★" + hHero.getRarity() + " " + hHero.getRole(), x + 58, y + 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            String name = hHero.getName();
            if (name.length() > 15) name = name.substring(0, 14) + "…";
            g2.drawString(name, x + 58, y + 38);

            g2.setColor(new Color(190, 205, 225));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString("Lv." + hHero.getLevel() + " | ATK:" + hHero.getAttack(), x + 58, y + 54);
            g2.drawString("HP:" + hHero.getMaxHp() + " | DEF:" + hHero.getDefense(), x + 58, y + 69);

            int btnY = y + 82;
            if (inParty) {
                g2.setColor(new Color(45, 65, 60));
                g2.fillRoundRect(x + 10, btnY, w - 20, 22, 5, 5);
                g2.setColor(new Color(100, 255, 160));
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                g2.drawString(inActive ? "✓ IN ACTIVE DPS SQUAD" : "✓ IN AUTO-SUPPORT", x + 22, btnY + 15);
            } else {
                boolean isDps = hHero.isVanguardEligible();
                g2.setColor(isDps ? new Color(192, 57, 43) : new Color(39, 174, 96));
                g2.fillRoundRect(x + 10, btnY, w - 20, 22, 5, 5);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                String targetLabel = isDps
                        ? ("EQUIP DPS SLOT " + ((selectedSlot < 5 ? selectedSlot : 0) + 1))
                        : ("EQUIP SUPPORT S" + ((selectedSlot >= 5 ? selectedSlot - 5 : 0) + 1));
                g2.drawString(targetLabel, x + 22, btnY + 15);
            }
        }
    }
}