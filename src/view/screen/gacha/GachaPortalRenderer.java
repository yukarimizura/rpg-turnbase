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
        g2.translate(475, 275);

        g2.rotate(tick * 0.02);
        g2.setColor(new Color(142, 68, 173, 90));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{12, 8}, 0));
        g2.drawOval(-110, -110, 220, 220);

        g2.rotate(-tick * 0.045);
        g2.setColor(new Color(0, 215, 255, 120));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawOval(-75, -75, 150, 150);

        g2.setTransform(old);
        g2.setStroke(oldStroke);

        g2.setColor(new Color(200, 220, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString("PORTAL READY - PILIH 1x ATAU 10x SUMMON", 305, 280);
    }

    public static void drawPortalChargeAnimation(Graphics2D g2, int tick, int maxTick, Color auraColor) {
        float progress = (float) tick / maxTick;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();
        g2.translate(475, 275);

        // Converging energy particles
        for (int i = 0; i < 12; i++) {
            double ang = (i * Math.PI / 6.0) + tick * 0.18;
            float dist = (1.0f - progress) * 190f + 10f;
            int px = (int) (Math.cos(ang) * dist);
            int py = (int) (Math.sin(ang) * dist);
            g2.setColor(auraColor);
            g2.fillOval(px - 4, py - 4, 8, 8);
            g2.setColor(Color.WHITE);
            g2.drawLine(px, py, (int) (px * 0.7f), (int) (py * 0.7f));
        }

        // Expanding & spinning dimensional rings
        int r1 = (int) (40 + progress * 110);
        g2.rotate(tick * 0.22);
        g2.setColor(auraColor);
        g2.setStroke(new BasicStroke(3.5f));
        g2.drawOval(-r1, -r1, r1 * 2, r1 * 2);

        g2.rotate(-tick * 0.4);
        int r2 = (int) (95 - progress * 50);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRect(-r2, -r2, r2 * 2, r2 * 2);

        g2.setTransform(old);
        g2.setStroke(oldStroke);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("MATERIALIZING HERO DATA...", 360, 440);
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
