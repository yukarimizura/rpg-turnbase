package view.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import view.component.CyberUIEffects;
import view.component.UIButton;

public class ProfileSelectScreen {
    public static void render(Graphics2D g2, List<String> profiles, UIButton btnNewProfile, UIButton btnQuit) {
        CyberUIEffects.drawCyberBackground(g2, false);

        CyberUIEffects.drawGlassCard(g2, 180, 35, 590, 88, new Color(0, 215, 255, 170), false);
        g2.setColor(new Color(0, 225, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.drawString("CHOOSE COMMANDER PROFILE", 245, 75);

        g2.setColor(new Color(200, 215, 235));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.drawString("Pilih profil simpanan yang sudah ada atau buat profil baru untuk memulai.", 235, 103);

        if (profiles.isEmpty()) {
            CyberUIEffects.drawGlassCard(g2, 225, 185, 500, 110, new Color(90, 105, 130), false);
            g2.setColor(new Color(180, 195, 215));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 15));
            g2.drawString("Belum ada data simpanan. Klik [+ CREATE NEW] di bawah!", 270, 245);
        } else {
            for (int i = 0; i < profiles.size() && i < 4; i++) {
                int cardX = 225;
                int cardY = 150 + (i * 68);
                int cardW = 500;
                int cardH = 54;

                CyberUIEffects.drawGlassCard(g2, cardX, cardY, cardW, cardH, new Color(52, 152, 219), false);

                g2.setColor(new Color(0, 215, 255));
                g2.fillOval(cardX + 18, cardY + 17, 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                g2.drawString("COMMANDER: " + profiles.get(i), cardX + 48, cardY + 33);

                // Badge Load
                g2.setColor(new Color(39, 174, 96));
                g2.fillRoundRect(cardX + 375, cardY + 13, 108, 28, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("LOAD PROFILE", cardX + 386, cardY + 31);
            }
        }

        btnNewProfile.draw(g2);
        btnQuit.draw(g2);
    }

    public static int getClickedProfileIndex(Point p, int count) {
        if (p == null) return -1;
        for (int i = 0; i < count && i < 4; i++) {
            int cardX = 225;
            int cardY = 150 + (i * 68);
            if (p.x >= cardX && p.x <= cardX + 500 && p.y >= cardY && p.y <= cardY + 54) {
                return i;
            }
        }
        return -1;
    }
}