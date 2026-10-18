package view.component;

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
        Stroke oldStroke = g2.getStroke();
        int expandX = (int) (hoverAnim * 4);
        int expandY = (int) (hoverAnim * 2);
        int drawX = x - expandX / 2;
        int drawY = y - expandY / 2 - (int) (hoverAnim * 2);
        int drawW = width + expandX;
        int drawH = height + expandY;

        // Drop shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(drawX + 2, drawY + 4, drawW, drawH, 12, 12);

        // Outer neon glow when hovered
        if (hoverAnim > 0.05f) {
            int glowAlpha = (int) (hoverAnim * 95);
            g2.setColor(new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue(), glowAlpha));
            g2.fillRoundRect(drawX - 4, drawY - 4, drawW + 8, drawH + 8, 16, 16);
        }

        // Interpolated button fill color
        int r = (int) (baseColor.getRed() + (hoverColor.getRed() - baseColor.getRed()) * hoverAnim);
        int g = (int) (baseColor.getGreen() + (hoverColor.getGreen() - baseColor.getGreen()) * hoverAnim);
        int b = (int) (baseColor.getBlue() + (hoverColor.getBlue() - baseColor.getBlue()) * hoverAnim);
        Color currentColor = new Color(clamp(r), clamp(g), clamp(b));

        GradientPaint gp = new GradientPaint(
                drawX, drawY, currentColor.brighter(),
                drawX, drawY + drawH, currentColor.darker()
        );
        g2.setPaint(gp);
        g2.fillRoundRect(drawX, drawY, drawW, drawH, 10, 10);

        // Top gloss highlight
        g2.setColor(new Color(255, 255, 255, 35 + (int) (hoverAnim * 35)));
        g2.fillRoundRect(drawX + 3, drawY + 2, drawW - 6, drawH / 2 - 2, 8, 8);

        // Cyber border
        int borderAlpha = 130 + (int) (hoverAnim * 125);
        g2.setColor(new Color(220, 245, 255, borderAlpha));
        g2.setStroke(new BasicStroke(1.2f + hoverAnim * 1.2f));
        g2.drawRoundRect(drawX, drawY, drawW, drawH, 10, 10);

        // Cyber corner ticks
        g2.setColor(new Color(0, 230, 255, 160 + (int) (hoverAnim * 95)));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawLine(drawX + 2, drawY + 8, drawX + 2, drawY + 2);
        g2.drawLine(drawX + 2, drawY + 2, drawX + 10, drawY + 2);
        g2.drawLine(drawX + drawW - 2, drawY + drawH - 8, drawX + drawW - 2, drawY + drawH - 2);
        g2.drawLine(drawX + drawW - 2, drawY + drawH - 2, drawX + drawW - 10, drawY + drawH - 2);

        // Button text
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        int textX = drawX + (drawW - fm.stringWidth(text)) / 2;
        int textY = drawY + (drawH - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString(text, textX + 1, textY + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX, textY);
        g2.setStroke(oldStroke);
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    public String getText() { return text; }
}