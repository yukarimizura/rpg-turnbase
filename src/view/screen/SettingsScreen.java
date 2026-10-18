package view.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import view.component.CyberUIEffects;
import view.component.UIButton;

public class SettingsScreen {
    public static void render(Graphics2D g2, UIButton btnBack, UIButton btnRes1, UIButton btnRes2, UIButton btnVfx, UIButton btnSave) {
        CyberUIEffects.drawCyberBackground(g2, false);
        btnBack.draw(g2);

        CyberUIEffects.drawGlassCard(g2, 200, 40, 550, 445, new Color(0, 215, 255, 160), false);

        g2.setColor(new Color(0, 225, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.drawString("ENGINE GRAPHICS & SAVE SYSTEM", 265, 82);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.drawString("Window mendukung live-scaling otomatis (AffineTransform Virtual Resolution).", 240, 112);

        btnRes1.draw(g2);
        btnRes2.draw(g2);
        btnVfx.draw(g2);
        btnSave.draw(g2);

        g2.setColor(new Color(180, 200, 225));
        g2.setFont(new Font("SansSerif", Font.ITALIC, 12));
        g2.drawString("Auto-Save aktif setiap selesai Summon, Upgrade Hero, atau mengalahkan Boss.", 245, 458);
    }
}