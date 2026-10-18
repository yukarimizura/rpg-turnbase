package view.component;

import view.component.cyber.CyberButtonRenderer;

import java.awt.*;

public class UIButton {
    private int x, y, width, height;
    private String text;
    private boolean hovered;
    private float hoverAnim = 0.0f;
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
        if (hovered) {
            hoverAnim = Math.min(1.0f, hoverAnim + 0.16f);
        } else {
            hoverAnim = Math.max(0.0f, hoverAnim - 0.14f);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void draw(Graphics2D g2) {
        CyberButtonRenderer.drawCyberButton(g2, x, y, width, height, text, baseColor, hoverColor, hoverAnim);
    }

    public String getText() { return text; }
}