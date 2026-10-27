package view.screen;

import java.awt.*;
import java.awt.geom.AffineTransform;

import view.component.CyberUIEffects;
import view.component.UIButton;

public class TitleScreen {
    public static void render(Graphics2D g2, UIButton btnStart, UIButton btnQuit) {
        CyberUIEffects.drawCyberBackground(g2, false);
        long tick = System.currentTimeMillis() / 16;

        // Pusat Layar 1920x1080 -> X: 960, Y: 540
        int centerX = 960;
        int centerY = 320; // Pusat vertikal untuk banner judul

        // 1. Rotating Cyber Chrono Emblem behind title (Di tengah layar 1920x1080)
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();
        
        g2.translate(centerX, centerY);
        g2.rotate(tick * 0.015);
        g2.setColor(new Color(0, 215, 255, 55));
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{14, 10}, 0));
        g2.drawOval(-220, -220, 440, 440);
        
        g2.rotate(-tick * 0.03);
        g2.setColor(new Color(185, 95, 255, 65));
        g2.drawRect(-140, -140, 280, 280);
        
        g2.setTransform(old);
        g2.setStroke(oldStroke);

        // 2. Glass Card Utama untuk Banner Judul (X: 510, Y: 190, Width: 900, Height: 260)
        CyberUIEffects.drawGlassCard(g2, centerX - 450, centerY - 130, 900, 260, new Color(0, 215, 255, 180), true);

        // 3. Floating Animation & Teks Judul
        int floatY = (int) (Math.sin(tick * 0.08) * 6);
        
        // Judul Utama (CYBER - CHRONO) dengan Drop Shadow
        g2.setFont(new Font("SansSerif", Font.BOLD, 54));
        FontMetrics fm = g2.getFontMetrics();
        String title = "CYBER - CHRONO";
        int titleX = centerX - fm.stringWidth(title) / 2;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString(title, titleX + 4, centerY - 20 + floatY + 4);
        g2.setColor(new Color(0, 225, 255));
        g2.drawString(title, titleX, centerY - 20 + floatY);

        // Sub-judul / Tagline
        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        fm = g2.getFontMetrics();
        String subTitle = "DUAL-SQUAD 2D REALTIME ARPG GACHA ENGINE";
        g2.drawString(subTitle, centerX - fm.stringWidth(subTitle) / 2, centerY + 40);

        // Deskripsi Engine
        g2.setColor(new Color(190, 210, 235));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        fm = g2.getFontMetrics();
        String desc = "5 Active DPS Vanguard  •  5 Auto-Resonance Off-Field Supports";
        g2.drawString(desc, centerX - fm.stringWidth(desc) / 2, centerY + 80);

        // 4. Render Tombol ENTER GAME & EXIT
        btnStart.draw(g2);
        btnQuit.draw(g2);
    }
}