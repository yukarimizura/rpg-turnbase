package view.screen;

import view.UIButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class TitleScreen {
    public static void render(Graphics2D g2, UIButton btnStart, UIButton btnQuit) {
        GradientPaint gp = new GradientPaint(0, 0, new Color(15, 17, 28), 0, 600, new Color(30, 10, 45));
        g2.setPaint(gp);
        g2.fillRect(0, 0, 950, 600);

        g2.setColor(new Color(0, 210, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 42));
        g2.drawString("CYBER - CHRONO", 285, 190);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("2D REALTIME ARPG GACHA ENGINE", 305, 230);

        btnStart.draw(g2);
        btnQuit.draw(g2);
    }
}