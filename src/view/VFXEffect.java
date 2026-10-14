package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class VFXEffect {
    public float x, y;
    public float radius;
    private Color color;
    private int lifetime;
    private final int MAX_LIFETIME = 20; // Durasi animasi (~0.3 detik)

    public VFXEffect(float x, float y, float radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.lifetime = MAX_LIFETIME;
    }

    public void update() {
        radius += 2.5f; // Efek gelombang membesar
        lifetime--;
    }

    public boolean isExpired() {
        return lifetime <= 0;
    }

    public void draw(Graphics2D g2) {
        int alpha = (int) (((float) lifetime / MAX_LIFETIME) * 220);
        alpha = Math.max(0, Math.min(255, alpha));

        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        g2.setStroke(new BasicStroke(3.0f));
        g2.drawOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
    }
}