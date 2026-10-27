package view.screen;

import engine.GameCanvas;
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

        int width = GameCanvas.BASE_WIDTH;   // 1920
        int height = GameCanvas.BASE_HEIGHT; // 1080

        // 1. Header Banner Top Bar
        CyberUIEffects.drawGlassCard(g2, 60, 20, 1800, 85, new Color(41, 128, 185, 180), false);
        if (btnBack != null) btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.drawString("DUAL SQUAD FORMATION: ACTIVE DPS & INACTIVE SUPPORT", 90, 58);

        g2.setColor(new Color(0, 215, 255));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.drawString("Active Squad (Tombol 1-5): Khusus Attacker/DPS  |  Inactive Squad (Auto-Cast): Khusus Defender, Healer, Support", 90, 86);

        List<Character> activeParty = partyManager.getActiveParty();
        List<Character> inactiveParty = partyManager.getInactiveParty();

        int slotW = 330;
        int slotH = 110;
        int gapX = 30;

        // 2. ROW ATAS: 5 ACTIVE DPS SLOTS (0..4)
        g2.setColor(new Color(255, 120, 110));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("▶ ACTIVE DPS SQUAD (Dikendalikan Manual - Pilih dengan Angka [1] - [5] di Battle)", 60, 132);

        for (int i = 0; i < 5; i++) {
            int x = 60 + (i * (slotW + gapX)); // X: 60, 420, 780, 1140, 1500
            int y = 148;
            Character c = (i < activeParty.size()) ? activeParty.get(i) : null;
            SquadSlotCardRenderer.drawSquadSlotCard(g2, x, y, slotW, slotH, "ACTIVE [" + (i + 1) + "]", c, selectedActiveSlot == i, new Color(231, 76, 60));
        }

        // 3. ROW TENGAH: 5 INACTIVE SUPPORT SLOTS (5..9)
        g2.setColor(new Color(100, 240, 160));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("▶ INACTIVE AUTO-SUPPORT SQUAD (Otomatis Heal saat HP turun & Auto Re-Buff saat Buff habis)", 60, 292);

        for (int i = 0; i < 5; i++) {
            int slotIdx = 5 + i;
            int x = 60 + (i * (slotW + gapX));
            int y = 308;
            Character c = (i < inactiveParty.size()) ? inactiveParty.get(i) : null;
            SquadSlotCardRenderer.drawSquadSlotCard(g2, x, y, slotW, slotH, "SUPPORT [S" + (i + 1) + "]", c, selectedActiveSlot == slotIdx, new Color(46, 204, 113));
        }

        // 4. PANEL BAWAH: HERO INVENTORY (Y = 440+)
        PartyInventoryRenderer.renderInventory(g2, profile.getOwnedHeroes(), partyManager, selectedActiveSlot, inventoryPage);
    }

    public static void handleClick(Point p, PartyManager party, PlayerProfile profile) {
        if (p == null) return;

        int slotW = 330;
        int slotH = 110;
        int gapX = 30;

        // 1. Check Active DPS Slots (0..4) and Inactive Support Slots (5..9) Hitbox
        for (int i = 0; i < 5; i++) {
            int x = 60 + (i * (slotW + gapX));
            
            // Active DPS Slots (Y: 148 s/d 258)
            if (p.x >= x && p.x <= x + slotW && p.y >= 148 && p.y <= 148 + slotH) { 
                selectedActiveSlot = i; 
                return; 
            }
            
            // Inactive Support Slots (Y: 308 s/d 418)
            if (p.x >= x && p.x <= x + slotW && p.y >= 308 && p.y <= 308 + slotH) { 
                selectedActiveSlot = 5 + i; 
                return; 
            }
        }

        List<Character> owned = profile.getOwnedHeroes();
        int maxPage = Math.max(0, (owned.size() - 1) / 10);

        // 2. Check Pagination Buttons in Inventory Header (Y: 440 s/d 465)
        if (p.y >= 435 && p.y <= 468) {
            if (p.x >= 1420 && p.x <= 1530 && inventoryPage > 0) { 
                inventoryPage--; 
                return; 
            }
            if (p.x >= 1550 && p.x <= 1660 && inventoryPage < maxPage) { 
                inventoryPage++; 
                return; 
            }
        }

        // 3. Check Inventory Hero Cards (10 cards per page, Grid 5x2)
        int invSlotW = 330;
        int invSlotH = 125;
        int invGapY = 20;

        int startIdx = inventoryPage * 10;
        for (int i = 0; i < 10 && (startIdx + i) < owned.size(); i++) {
            int col = i % 5;
            int row = i / 5;

            int x = 60 + col * (invSlotW + gapX);
            int y = 475 + row * (invSlotH + invGapY);

            if (p.x >= x && p.x <= x + invSlotW && p.y >= y && p.y <= y + invSlotH) {
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