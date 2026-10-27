package view.component.cyber;

import engine.GameCanvas;
import java.awt.*;

public class CyberBackgroundRenderer {

    public static void drawCyberBackground(Graphics2D g2, boolean isBattleArena, boolean vfxEnabled) {
        long tick = System.currentTimeMillis() / 20;

        // Gunakan acuan batas Canvas resmi 1920x1080
        int width = GameCanvas.BASE_WIDTH;   // 1920
        int height = GameCanvas.BASE_HEIGHT; // 1080
        int centerX = width / 2;             // 960
        int centerY = height / 2;            // 540

        if (isBattleArena) {
            GradientPaint gp = new GradientPaint(0, 0, new Color(14, 18, 28), 0, height, new Color(22, 16, 34));
            g2.setPaint(gp);
            g2.fillRect(0, 0, width, height);

            // Tactical Arena Floor Grid
            g2.setColor(new Color(0, 210, 255, 18));
            g2.setStroke(new BasicStroke(1.0f));
            for (int x = 0; x < width; x += 50) {
                g2.drawLine(x, 0, x, height);
            }
            for (int y = 0; y < height; y += 50) {
                g2.drawLine(0, y, width, y);
            }

            // Center Arena Tactical Ring (Di tengah layar 1920x1080)
            g2.setColor(new Color(0, 210, 255, 28));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(centerX - 250, centerY - 250, 500, 500);
            g2.drawOval(centerX - 130, centerY - 130, 260, 260);
        } else {
            GradientPaint gp = new GradientPaint(0, 0, new Color(11, 14, 24), 0, height, new Color(26, 15, 42));
            g2.setPaint(gp);
            g2.fillRect(0, 0, width, height);

            // Moving Cyber Grid
            int offset = (int) (tick % 45);
            g2.setColor(new Color(0, 195, 255, 20));
            g2.setStroke(new BasicStroke(1.0f));
            for (int x = -45 + offset; x < width; x += 45) {
                g2.drawLine(x, 0, x, height);
            }
            for (int y = -45 + offset; y < height; y += 45) {
                g2.drawLine(0, y, width, y);
            }
        }

        // Floating Ambient Cyber Particles (Merentang penuh ke 1920x1080)
        if (vfxEnabled) {
            for (int i = 0; i < 35; i++) {
                float px = ((i * 137 + tick * (0.4f + (i % 3) * 0.2f)) % width);
                float py = height - ((i * 97 + tick * (0.6f + (i % 4) * 0.25f)) % height);
                int alpha = 40 + (int) (Math.sin(tick * 0.08 + i) * 30);
                int size = 2 + (i % 3);
                g2.setColor(i % 2 == 0 ? new Color(0, 215, 255, alpha) : new Color(185, 95, 255, alpha));
                g2.fillOval((int) px, (int) py, size, size);
            }
        }
    }

    public static void drawGlassCard(Graphics2D g2, int x, int y, int w, int h, Color borderColor, boolean highlighted) {
        Stroke oldStroke = g2.getStroke();
        g2.setColor(new Color(20, 25, 38, 215));
        g2.fillRoundRect(x, y, w, h, 12, 12);

        if (highlighted) {
            g2.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 45));
            g2.fillRoundRect(x - 3, y - 3, w + 6, h + 6, 14, 14);
        }

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(highlighted ? 2.2f : 1.2f));
        g2.drawRoundRect(x, y, w, h, 12, 12);

        // Top highlight bar
        g2.setColor(new Color(255, 255, 255, 25));
        g2.fillRoundRect(x + 4, y + 3, w - 8, 5, 4, 4);
        g2.setStroke(oldStroke);
    }
}