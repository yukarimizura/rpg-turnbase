package view.screen;

import java.awt.*;
import java.awt.geom.AffineTransform;

import view.component.CyberUIEffects;
import view.component.UIButton;

public class TitleScreen {
    public static void render(Graphics2D g2, UIButton btnStart, UIButton btnQuit) {
        CyberUIEffects.drawCyberBackground(g2, false);
        long tick = System.currentTimeMillis() / 16;

        // Rotating Cyber Chrono Emblem behind title
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();
        g2.translate(475, 195);
        g2.rotate(tick * 0.015);
        g2.setColor(new Color(0, 215, 255, 55));
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{14, 10}, 0));
        g2.drawOval(-130, -130, 260, 260);
        g2.rotate(-tick * 0.03);
        g2.setColor(new Color(185, 95, 255, 65));
        g2.drawRect(-85, -85, 170, 170);
        g2.setTransform(old);
        g2.setStroke(oldStroke);

        CyberUIEffects.drawGlassCard(g2, 215, 120, 520, 150, new Color(0, 215, 255, 180), true);

        int floatY = (int) (Math.sin(tick * 0.08) * 4);
        g2.setFont(new Font("SansSerif", Font.BOLD, 44));
        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString("CYBER - CHRONO", 278, 188 + floatY);
        g2.setColor(new Color(0, 225, 255));
        g2.drawString("CYBER - CHRONO", 275, 185 + floatY);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("DUAL-SQUAD 2D REALTIME ARPG GACHA ENGINE", 270, 222);

        g2.setColor(new Color(190, 210, 235));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("5 Active DPS Vanguard  •  5 Auto-Resonance Off-Field Supports", 292, 248);

        btnStart.draw(g2);
        btnQuit.draw(g2);
    }
}