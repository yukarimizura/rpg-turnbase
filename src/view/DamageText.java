package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DamageText {
    public float x, y;
    private String text;
    private Color color;
    private int lifetime;
    private final int MAX_LIFETIME = 45;

    public DamageText(float x, float y, String text, Color color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.lifetime = MAX_LIFETIME;
    }

    public void update() {
        y -= 0.8f;
        lifetime--;
    }

    public boolean isExpired() {
        return lifetime <= 0;
    }

    public void draw(Graphics2D g2) {
        int alpha = (int) (((float) lifetime / MAX_LIFETIME) * 255);
        alpha = Math.max(0, Math.min(255, alpha));

        Color renderColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        g2.setColor(renderColor);
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString(text, (int) x, (int) y);
    }
}
