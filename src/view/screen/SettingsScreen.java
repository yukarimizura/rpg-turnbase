package view.screen;

import view.UIButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class SettingsScreen {
    public static void render(Graphics2D g2, UIButton btnBack, UIButton btnRes1, UIButton btnRes2, UIButton btnVfx) {
        btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("ENGINE GRAPHICS & RESOLUTION", 240, 80);

        g2.setColor(new Color(255, 204, 0));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Window dapat di-resize langsung secara fleksibel via pinggiran window (Drag-Resize).", 215, 120);

        btnRes1.draw(g2);
        btnRes2.draw(g2);
        btnVfx.draw(g2);

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 13));
        g2.drawString("Engine dibangun dengan Dynamic AffineTransform Scaling (Native 950x600 Ratio).", 225, 420);
    }
}