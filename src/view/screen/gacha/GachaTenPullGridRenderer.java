package view.screen.gacha;

import engine.GameCanvas;
import model.characters.Character;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class GachaTenPullGridRenderer {

    public static void drawTenPullGrid(Graphics2D g2, List<Character> batch, int revealTick) {
        int cols = 5;
        
        // Ukuran Kartu & Spasi Ditingkatkan untuk Resolusi 1080p
        int cardW = 280, cardH = 320;
        int gapX = 30, gapY = 30;

        // Hitung Lebar & Tinggi Total Grid 5x2 untuk Mencari Titik Tengah Simetris
        int totalGridW = (cols * cardW) + ((cols - 1) * gapX); // 5 * 280 + 4 * 30 = 1520
        int totalGridH = (2 * cardH) + (1 * gapY);            // 2 * 320 + 1 * 30 = 670

        // Titik Awal Grid (Diposisikan Presisi di Tengah Layar 1920x1080)
        int startX = (GameCanvas.BASE_WIDTH - totalGridW) / 2;  // (1920 - 1520) / 2 = 200
        int startY = (GameCanvas.BASE_HEIGHT - totalGridH) / 2; // (1080 - 670) / 2 = 205

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
                int r = 60 + localTick * 10;
                g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), Math.max(0, 200 - localTick * 18)));
                g2.drawOval(-r, -r, r * 2, r * 2);
            }

            // Rarity Header
            g2.setColor(rarityColor);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            String badge = "★ " + c.getRarity() + " ★";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(badge, -fm.stringWidth(badge) / 2, y + 35);

            // Mini Character Sprite
            SpriteRenderer.drawCharacter(g2, c, 0, y + 115, 1.6f);

            // Name & SubRole
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            fm = g2.getFontMetrics();
            String name = c.getName();
            g2.drawString(name, -fm.stringWidth(name) / 2, y + 195);

            g2.setColor(SpriteRenderer.getRoleAccentColor(c.getRole(), c.getSubRole()));
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            String roleStr = c.getRole() + (c.isVanguardEligible() ? " (DPS)" : " (Auto)");
            fm = g2.getFontMetrics();
            g2.drawString(roleStr, -fm.stringWidth(roleStr) / 2, y + 225);

            // Compact Stats
            g2.setColor(new Color(200, 215, 230));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
            String stat1 = "HP: " + c.getMaxHp() + " | DEF: " + c.getDefense();
            String stat2 = "ATK: " + c.getAttack();
            fm = g2.getFontMetrics();
            g2.drawString(stat1, -fm.stringWidth(stat1) / 2, y + 260);
            g2.drawString(stat2, -fm.stringWidth(stat2) / 2, y + 288);

            g2.setTransform(old);
        }
    }
}