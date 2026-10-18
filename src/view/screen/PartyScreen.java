package view.screen;

import model.characters.Character;
import service.PartyManager;
import service.PlayerProfile;
import view.component.CyberUIEffects;
import view.component.UIButton;
import view.screen.party.SquadSlotCardRenderer;

import java.awt.*;
import java.util.List;

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
            Character c = (i < activeParty.size()) ? activeParty.get(i) : null;
            SquadSlotCardRenderer.drawSquadSlotCard(g2, x, y, w, h, "ACTIVE [" + (i + 1) + "]", c, selectedActiveSlot == i, new Color(231, 76, 60));
        }

        // 2. ROW TENGAH: 5 INACTIVE SUPPORT SLOTS (5..9)
        g2.setColor(new Color(80, 230, 150));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("▶ INACTIVE AUTO-SUPPORT SQUAD (Otomatis Heal saat HP turun & Auto Re-Buff saat Buff habis)", 40, 196);

        for (int i = 0; i < 5; i++) {
            int slotIdx = 5 + i;
            int x = 40 + (i * 176), y = 202, w = 164, h = 100;
            Character c = (i < inactiveParty.size()) ? inactiveParty.get(i) : null;
            SquadSlotCardRenderer.drawSquadSlotCard(g2, x, y, w, h, "SUPPORT [S" + (i + 1) + "]", c, selectedActiveSlot == slotIdx, new Color(46, 204, 113));
        }

        // 3. PANEL BAWAH: HERO INVENTORY
        PartyInventoryRenderer.renderInventory(g2, profile.getOwnedHeroes(), partyManager, selectedActiveSlot, inventoryPage);
    }

    public static void handleClick(Point p, PartyManager party, PlayerProfile profile) {
        if (p == null) return;

        // Check Active DPS Slots (0..4) and Inactive Support Slots (5..9)
        for (int i = 0; i < 5; i++) {
            int x = 40 + (i * 176);
            if (p.x >= x && p.x <= x + 164 && p.y >= 80 && p.y <= 180) { selectedActiveSlot = i; return; }
            if (p.x >= x && p.x <= x + 164 && p.y >= 202 && p.y <= 302) { selectedActiveSlot = 5 + i; return; }
        }

        List<Character> owned = profile.getOwnedHeroes();
        int maxPage = Math.max(0, (owned.size() - 1) / 10);

        // Check Pagination Buttons in Inventory Header
        if (p.y >= 312 && p.y <= 336) {
            if (p.x >= 720 && p.x <= 805 && inventoryPage > 0) { inventoryPage--; return; }
            if (p.x >= 815 && p.x <= 900 && inventoryPage < maxPage) { inventoryPage++; return; }
        }

        // Check Inventory Hero Cards (10 per page)
        int startIdx = inventoryPage * 10;
        for (int i = 0; i < 10 && (startIdx + i) < owned.size(); i++) {
            int x = 40 + ((i % 5) * 176), y = 344 + ((i / 5) * 122);
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