package view.screen.gacha;

import model.characters.Character;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class GachaTenPullGridRenderer {

    public static void drawTenPullGrid(Graphics2D g2, List<Character> batch, int revealTick) {
        int cols = 5;
        int cardW = 162, cardH = 182;
        int gapX = 16, gapY = 14;
        int startX = 40;
        int startY = 98;

        for (int i = 0; i < batch.size() && i < 10; i++) {
            int cardDelay = i * 4;
            int localTick = revealTick - cardDelay;
            if (localTick < 0) continue;

            float scale = Math.min(1.0f, 0.2f + localTick * 0.10f);
            Character c = batch.get(i);
            Color rarityColor = SpriteRenderer.getRarityColor(c.getRarity());

            int col = i % cols;
            int row = i / cols;
            int cx = startX + col * (cardW + gapX) + cardW / 2;
            int cy = startY + row * (cardH + gapY) + cardH / 2;

            AffineTransform old = g2.getTransform();
            g2.translate(cx, cy);
            g2.scale(scale, scale);

            int x = -cardW / 2;
            int y = -cardH / 2;

            boolean isHighRarity = "SSR".equals(c.getRarity()) || "SR".equals(c.getRarity());
            CyberUIEffects.drawGlassCard(g2, x, y, cardW, cardH, rarityColor, isHighRarity);

            // Pop-in shockwave ring
            if (localTick < 10) {
                int r = 35 + localTick * 6;
                g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), 200 - localTick * 18));
                g2.drawOval(-r, -r, r * 2, r * 2);
            }

            // Rarity Header
            g2.setColor(rarityColor);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            String badge = "★ " + c.getRarity() + " ★";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(badge, -fm.stringWidth(badge) / 2, y + 20);

            // Mini Character Sprite
            SpriteRenderer.drawCharacter(g2, c, 0, y + 66, 1.0f);

            // Name & SubRole
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            fm = g2.getFontMetrics();
            String name = c.getName();
            g2.drawString(name, -fm.stringWidth(name) / 2, y + 108);

            g2.setColor(SpriteRenderer.getRoleAccentColor(c.getRole(), c.getSubRole()));
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            String roleStr = c.getRole() + (c.isVanguardEligible() ? " (DPS)" : " (Auto)");
            fm = g2.getFontMetrics();
            g2.drawString(roleStr, -fm.stringWidth(roleStr) / 2, y + 124);

            // Compact Stats
            g2.setColor(new Color(200, 215, 230));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            String stat1 = "HP: " + c.getMaxHp() + " | DEF: " + c.getDefense();
            String stat2 = "ATK: " + c.getAttack();
            fm = g2.getFontMetrics();
            g2.drawString(stat1, -fm.stringWidth(stat1) / 2, y + 146);
            g2.drawString(stat2, -fm.stringWidth(stat2) / 2, y + 162);

            g2.setTransform(old);
        }
    }
}
