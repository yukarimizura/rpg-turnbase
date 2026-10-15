package view.screen;

import view.UIButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class SettingsScreen {
    public static void render(Graphics2D g2, UIButton btnBack, UIButton btnRes1, UIButton btnRes2, UIButton btnVfx, UIButton btnSave) {
        btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("ENGINE GRAPHICS & SAVE GAME", 250, 75);

        g2.setColor(new Color(255, 204, 0));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Window dapat di-resize langsung via border window (AffineTransform Scaling).", 215, 115);

        btnRes1.draw(g2);
        btnRes2.draw(g2);
        btnVfx.draw(g2);
        btnSave.draw(g2);

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 13));
        g2.drawString("Game juga otomatis menyimpan data setelah gacha, upgrade, atau menang boss.", 210, 460);
    }
}