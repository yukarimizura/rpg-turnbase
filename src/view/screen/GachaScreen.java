package view.screen;

import service.PlayerProfile;
import view.UIButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GachaScreen {
    public static void render(Graphics2D g2, PlayerProfile profile, String notice,
                             UIButton btnBack, UIButton btn1x, UIButton btn10x) {
        btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("SUMMON PORTAL: STELLAR CHRONO", 220, 80);

        g2.setColor(new Color(0, 210, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("Gems Kamu: " + profile.getCyberGems(), 400, 120);

        g2.setColor(new Color(30, 20, 45));
        g2.fillRoundRect(220, 150, 510, 250, 15, 15);
        g2.setColor(new Color(155, 89, 182));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(220, 150, 510, 250, 15, 15);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("SSR RATE UP: DAWN PRIESTESS & BLADE DANCER", 235, 230);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Rates: SSR 2% | SR 13% | R 85%", 370, 270);

        if (!notice.isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.drawString(notice, 310, 340);
        }

        btn1x.draw(g2);
        btn10x.draw(g2);
    }
}