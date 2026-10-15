package view.screen;

import view.UIButton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class ProfileSelectScreen {
    public static void render(Graphics2D g2, List<String> profiles, UIButton btnNewProfile, UIButton btnQuit) {
        g2.setColor(new Color(15, 17, 28));
        g2.fillRect(0, 0, 950, 600);

        g2.setColor(new Color(0, 210, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 32));
        g2.drawString("CHOOSE COMMANDER PROFILE", 230, 80);

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Pilih profil simpanan yang sudah ada atau buat profil baru untuk memulai perjalanan.", 205, 115);

        // Render Kartu Akun Tersimpan (Maks 4 akun di layar)
        if (profiles.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
            g2.drawString("Belum ada data simpanan. Buat profil baru di bawah!", 285, 260);
        } else {
            for (int i = 0; i < profiles.size() && i < 4; i++) {
                int cardX = 225;
                int cardY = 150 + (i * 65);
                int cardW = 500;
                int cardH = 50;

                g2.setColor(new Color(30, 35, 50));
                g2.fillRoundRect(cardX, cardY, cardW, cardH, 8, 8);
                g2.setColor(new Color(41, 128, 185));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(cardX, cardY, cardW, cardH, 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                g2.drawString("COMMANDER: " + profiles.get(i), cardX + 25, cardY + 30);

                // Badge Load
                g2.setColor(new Color(39, 174, 96));
                g2.fillRoundRect(cardX + 380, cardY + 12, 100, 26, 5, 5);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("LOAD GAME", cardX + 395, cardY + 29);
            }
        }

        btnNewProfile.draw(g2);
        btnQuit.draw(g2);
    }

    public static int getClickedProfileIndex(Point p, int count) {
        if (p == null) return -1;
        for (int i = 0; i < count && i < 4; i++) {
            int cardX = 225;
            int cardY = 150 + (i * 65);
            if (p.x >= cardX && p.x <= cardX + 500 && p.y >= cardY && p.y <= cardY + 50) {
                return i;
            }
        }
        return -1;
    }
}