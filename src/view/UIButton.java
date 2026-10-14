package view;

import java.awt.*;

public class UIButton {
    private int x, y, width, height;
    private String text;
    private boolean hovered;
    private Color baseColor;
    private Color hoverColor;

    public UIButton(int x, int y, int width, int height, String text, Color baseColor, Color hoverColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.hovered = false;
    }

    public boolean contains(Point p) {
        return p != null && p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }

    public void checkHover(Point mousePos) {
        this.hovered = contains(mousePos);
    }

    public void draw(Graphics2D g2) {
        // Efek bayangan / glow
        if (hovered) {
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillRoundRect(x - 2, y - 2, width + 4, height + 4, 12, 12);
        }

        // Background tombol
        g2.setColor(hovered ? hoverColor : baseColor);
        g2.fillRoundRect(x, y, width, height, 10, 10);

        // Border garis
        g2.setColor(hovered ? Color.WHITE : new Color(200, 200, 200, 100));
        g2.setStroke(new BasicStroke(hovered ? 2.0f : 1.0f));
        g2.drawRoundRect(x, y, width, height, 10, 10);

        // Render teks di tengah tombol
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, textX, textY);
    }

    public String getText() { return text; }
}