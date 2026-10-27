package view.screen.gacha;

import model.characters.Character;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class GachaPortalRenderer {

    public static void drawIdlePortal(Graphics2D g2) {
        long tick = System.currentTimeMillis() / 16;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();

        // Translasi ke pusat layar 1920x1080 -> X: 960, Y: 480
        g2.translate(960, 480);

        // Lingkaran Luar Portal
        g2.rotate(tick * 0.02);
        g2.setColor(new Color(142, 68, 173, 90));
        g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{16, 10}, 0));
        g2.drawOval(-180, -180, 360, 360);

        // Lingkaran Dalam Portal
        g2.rotate(-tick * 0.045);
        g2.setColor(new Color(0, 215, 255, 120));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawOval(-120, -120, 240, 240);

        g2.setTransform(old);
        g2.setStroke(oldStroke);

        // Teks Petunjuk Idle Portal (Terpusat Simetris)
        g2.setColor(new Color(200, 220, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        String text = "PORTAL READY - PILIH 1x ATAU 10x SUMMON";
        g2.drawString(text, 960 - fm.stringWidth(text) / 2, 720);
    }

    public static void drawPortalChargeAnimation(Graphics2D g2, int tick, int maxTick, Color auraColor) {
        float progress = (float) tick / maxTick;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();

        // Translasi ke pusat layar 1920x1080 -> X: 960, Y: 480
        g2.translate(960, 480);

        // Converging energy particles
        for (int i = 0; i < 12; i++) {
            double ang = (i * Math.PI / 6.0) + tick * 0.18;
            float dist = (1.0f - progress) * 320f + 15f;
            int px = (int) (Math.cos(ang) * dist);
            int py = (int) (Math.sin(ang) * dist);
            g2.setColor(auraColor);
            g2.fillOval(px - 6, py - 6, 12, 12);
            g2.setColor(Color.WHITE);
            g2.drawLine(px, py, (int) (px * 0.7f), (int) (py * 0.7f));
        }

        // Expanding & spinning dimensional rings
        int r1 = (int) (70 + progress * 180);
        g2.rotate(tick * 0.22);
        g2.setColor(auraColor);
        g2.setStroke(new BasicStroke(5.0f));
        g2.drawOval(-r1, -r1, r1 * 2, r1 * 2);

        g2.rotate(-tick * 0.4);
        int r2 = (int) (150 - progress * 80);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3.5f));
        g2.drawRect(-r2, -r2, r2 * 2, r2 * 2);

        g2.setTransform(old);
        g2.setStroke(oldStroke);

        // Teks Charging Materializing Data
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        String text = "MATERIALIZING HERO DATA...";
        g2.drawString(text, 960 - fm.stringWidth(text) / 2, 720);
    }

    public static Color getHighestRarityColor(List<Character> batch) {
        boolean hasSSR = false, hasSR = false;
        for (Character c : batch) {
            if ("SSR".equals(c.getRarity())) hasSSR = true;
            else if ("SR".equals(c.getRarity())) hasSR = true;
        }
        if (hasSSR) return new Color(255, 215, 0);
        if (hasSR) return new Color(185, 95, 255);
        return new Color(52, 162, 235);
    }
}