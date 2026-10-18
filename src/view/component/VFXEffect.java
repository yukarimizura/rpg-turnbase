package view.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class VFXEffect {
    public float x, y;
    public float radius;
    private float startRadius;
    private Color color;
    private int lifetime;
    private final int MAX_LIFETIME = 24;

    public VFXEffect(float x, float y, float radius, Color color) {
        this.x = x;
        this.y = y;
        this.startRadius = Math.max(15f, radius * 0.35f);
        this.radius = this.startRadius;
        this.color = color;
        this.lifetime = MAX_LIFETIME;
    }

    public void update() {
        radius += 3.2f;
        lifetime--;
    }

    public boolean isExpired() {
        return lifetime <= 0;
    }

    public void draw(Graphics2D g2) {
        if (!CyberUIEffects.vfxEnabled) return;
        Stroke oldStroke = g2.getStroke();

        float ratio = (float) lifetime / MAX_LIFETIME;
        int alpha = Math.max(0, Math.min(255, (int) (ratio * 230)));

        // Soft fill glow
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha / 5));
        g2.fillOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));

        // Outer Shockwave Ring
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        g2.setStroke(new BasicStroke(3.2f * ratio + 0.8f));
        g2.drawOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));

        // Inner Echo Ring
        float innerR = radius * 0.68f;
        g2.setColor(new Color(255, 255, 255, alpha * 2 / 3));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval((int) (x - innerR), (int) (y - innerR), (int) (innerR * 2), (int) (innerR * 2));

        // Radial Energy Sparks
        for (int i = 0; i < 6; i++) {
            double ang = (i * Math.PI / 3.0) + (1.0 - ratio) * 0.8;
            int sx = (int) (x + Math.cos(ang) * (radius * 0.85));
            int sy = (int) (y + Math.sin(ang) * (radius * 0.85));
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fillOval(sx - 2, sy - 2, 5, 5);
        }

        g2.setStroke(oldStroke);
    }
}