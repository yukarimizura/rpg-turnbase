package view.screen;

import engine.GameCanvas;
import view.component.CyberUIEffects;
import view.component.UIButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class SettingsScreen {

    public static void render(Graphics2D g2, UIButton btnBack, UIButton btnRes1, UIButton btnRes2, UIButton btnVfx, UIButton btnSave) {
        CyberUIEffects.drawCyberBackground(g2, false);

        int centerX = GameCanvas.BASE_WIDTH / 2;   // 960
        int centerY = GameCanvas.BASE_HEIGHT / 2;  // 540

        // 1. Tombol Back to Hub (Pojok Kanan Atas)
        if (btnBack != null) {
            btnBack.draw(g2);
        }

        // 2. Main Glass Card Container (X: 560 s/d 1360, Lebar: 800px, Tinggi: 720px)
        int cardW = 800;
        int cardH = 720;
        int cardX = centerX - cardW / 2; // 560
        int cardY = centerY - cardH / 2 - 20; // 170

        CyberUIEffects.drawGlassCard(g2, cardX, cardY, cardW, cardH, new Color(0, 215, 255, 160), false);

        // 3. Title Header
        g2.setColor(new Color(0, 225, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 30));
        FontMetrics fm = g2.getFontMetrics();
        String title = "ENGINE GRAPHICS & SAVE SYSTEM";
        g2.drawString(title, centerX - fm.stringWidth(title) / 2, cardY + 70);

        // Subtitle / Virtual Resolution Info
        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fm = g2.getFontMetrics();
        String subtitle = "Window mendukung live-scaling otomatis (AffineTransform Virtual Resolution 1080p).";
        g2.drawString(subtitle, centerX - fm.stringWidth(subtitle) / 2, cardY + 115);

        // Line Separator
        g2.setColor(new Color(0, 215, 255, 80));
        g2.drawLine(cardX + 60, cardY + 140, cardX + cardW - 60, cardY + 140);

        // 4. Action Option Buttons
        if (btnRes1 != null) btnRes1.draw(g2);
        if (btnRes2 != null) btnRes2.draw(g2);
        if (btnVfx != null) btnVfx.draw(g2);
        if (btnSave != null) btnSave.draw(g2);

        // 5. Footer Information Note
        g2.setColor(new Color(180, 200, 225));
        g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
        fm = g2.getFontMetrics();
        String footerMsg = "Auto-Save aktif setiap selesai Summon, Upgrade Hero, atau mengalahkan Boss.";
        g2.drawString(footerMsg, centerX - fm.stringWidth(footerMsg) / 2, cardY + cardH - 45);
    }
}