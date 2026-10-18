package view.screen;

import service.PartyManager;
import service.PlayerProfile;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;
import view.component.UIButton;

import java.awt.*;
import java.util.List;

import model.characters.Character;

public class PartyScreen {
    // 0..4 = Active DPS Slots, 5..9 = Inactive Support Slots
    public static int selectedActiveSlot = 0;
    public static int inventoryPage = 0;

    public static void render(Graphics2D g2, PartyManager partyManager, PlayerProfile profile, UIButton btnBack) {
        CyberUIEffects.drawCyberBackground(g2, false);
        btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("DUAL SQUAD FORMATION: ACTIVE DPS & INACTIVE SUPPORT", 40, 36);
        g2.setColor(new Color(0, 215, 255));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("Active Squad (Tombol 1-5): Khusus Attacker/DPS  |  Inactive Squad (Auto-Cast): Khusus Defender, Healer, Support", 40, 56);

        List<Character> activeParty = partyManager.getActiveParty();
        List<Character> inactiveParty = partyManager.getInactiveParty();

        // 1. ROW ATAS: 5 ACTIVE DPS SLOTS (0..4)
        g2.setColor(new Color(255, 100, 90));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("▶ ACTIVE DPS SQUAD (Dikendalikan Manual - Pilih dengan Angka [1] - [5] di Battle)", 40, 74);

        for (int i = 0; i < 5; i++) {
            int x = 40 + (i * 176), y = 80, w = 164, h = 100;
            boolean isSelected = (selectedActiveSlot == i);
            Character c = (i < activeParty.size()) ? activeParty.get(i) : null;
            drawSquadSlotCard(g2, x, y, w, h, "ACTIVE [" + (i + 1) + "]", c, isSelected, new Color(231, 76, 60));
        }

        // 2. ROW TENGAH: 5 INACTIVE SUPPORT SLOTS (5..9)
        g2.setColor(new Color(80, 230, 150));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("▶ INACTIVE AUTO-SUPPORT SQUAD (Otomatis Heal saat HP turun & Auto Re-Buff saat Buff habis)", 40, 196);

        for (int i = 0; i < 5; i++) {
            int slotIdx = 5 + i;
            int x = 40 + (i * 176), y = 202, w = 164, h = 100;
            boolean isSelected = (selectedActiveSlot == slotIdx);
            Character c = (i < inactiveParty.size()) ? inactiveParty.get(i) : null;
            drawSquadSlotCard(g2, x, y, w, h, "SUPPORT [S" + (i + 1) + "]", c, isSelected, new Color(46, 204, 113));
        }

        // 3. PANEL BAWAH: HERO INVENTORY
        PartyInventoryRenderer.renderInventory(g2, profile.getOwnedHeroes(), partyManager, selectedActiveSlot, inventoryPage);
    }

    private static void drawSquadSlotCard(Graphics2D g2, int x, int y, int w, int h,
                                          String slotTitle, Character c, boolean isSelected, Color themeColor) {
        Color border = isSelected ? new Color(255, 215, 0) : (c != null ? themeColor : new Color(70, 80, 100));
        CyberUIEffects.drawGlassCard(g2, x, y, w, h, border, isSelected);

        g2.setColor(isSelected ? new Color(255, 225, 80) : new Color(180, 195, 215));
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2.drawString(slotTitle, x + 10, y + 16);

        if (c != null) {
            Color rarCol = SpriteRenderer.getRarityColor(c.getRarity());
            g2.setColor(rarCol);
            g2.drawString("★" + c.getRarity(), x + w - 42, y + 16);

            // Mini Character Sprite on Left
            SpriteRenderer.drawCharacter(g2, c, x + 32, y + 58, 0.9f);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString(trimText(c.getName(), 15), x + 62, y + 36);

            g2.setColor(SpriteRenderer.getRoleAccentColor(c.getRole(), c.getSubRole()));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString(c.getSubRole(), x + 62, y + 51);

            g2.setColor(new Color(200, 215, 230));
            g2.drawString("Lv." + c.getLevel() + " HP:" + c.getMaxHp(), x + 62, y + 67);
            g2.drawString("ATK:" + c.getAttack() + " DEF:" + c.getDefense(), x + 62, y + 82);
        } else {
            g2.setColor(new Color(110, 120, 140));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
            g2.drawString("[ EMPTY SLOT ]", x + 38, y + 55);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g2.drawString("Klik untuk memilih", x + 40, y + 73);
        }
    }

    private static String trimText(String s, int maxLen) {
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }

    public static void handleClick(Point p, PartyManager party, PlayerProfile profile) {
        if (p == null) return;

        // Check Active DPS Slots (0..4)
        for (int i = 0; i < 5; i++) {
            int x = 40 + (i * 176), y = 80;
            if (p.x >= x && p.x <= x + 164 && p.y >= y && p.y <= y + 100) {
                selectedActiveSlot = i;
                return;
            }
        }
        // Check Inactive Support Slots (5..9)
        for (int i = 0; i < 5; i++) {
            int x = 40 + (i * 176), y = 202;
            if (p.x >= x && p.x <= x + 164 && p.y >= y && p.y <= y + 100) {
                selectedActiveSlot = 5 + i;
                return;
            }
        }

        List<Character> owned = profile.getOwnedHeroes();
        int maxPage = Math.max(0, (owned.size() - 1) / 10);

        // Check Pagination Buttons in Inventory Header
        if (p.y >= 312 && p.y <= 336) {
            if (p.x >= 720 && p.x <= 805 && inventoryPage > 0) {
                inventoryPage--;
                return;
            }
            if (p.x >= 815 && p.x <= 900 && inventoryPage < maxPage) {
                inventoryPage++;
                return;
            }
        }

        // Check Inventory Hero Cards (10 per page)
        int startIdx = inventoryPage * 10;
        for (int i = 0; i < 10 && (startIdx + i) < owned.size(); i++) {
            int x = 40 + ((i % 5) * 176);
            int y = 344 + ((i / 5) * 122);
            if (p.x >= x && p.x <= x + 164 && p.y >= y && p.y <= y + 114) {
                Character chosen = owned.get(startIdx + i);
                // Auto-focus matching slot type so equipping is seamless
                if (chosen.isVanguardEligible() && selectedActiveSlot >= 5) {
                    selectedActiveSlot = Math.min(4, party.getActiveParty().size());
                } else if (!chosen.isVanguardEligible() && selectedActiveSlot < 5) {
                    selectedActiveSlot = 5 + Math.min(4, party.getInactiveParty().size());
                }
                party.replacePartyMember(selectedActiveSlot, chosen);
                return;
            }
        }
    }
}