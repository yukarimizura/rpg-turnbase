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
        // 1. Render Background Cyber Full Screen
        CyberUIEffects.drawCyberBackground(g2, false);

        // 2. Header Panel (Glass Card Utama di Atas Tengah)
        CyberUIEffects.drawGlassCard(g2, 460, 60, 1000, 140, new Color(0, 215, 255, 170), false);
        
        g2.setColor(new Color(0, 225, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 42));
        g2.drawString("CHOOSE COMMANDER PROFILE", 610, 125);

        g2.setColor(new Color(200, 215, 235));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2.drawString("Pilih profil simpanan yang sudah ada atau buat profil baru untuk memulai.", 610, 168);

        // 3. Render Slot Profil Simpanan
        if (profiles.isEmpty()) {
            CyberUIEffects.drawGlassCard(g2, 510, 320, 900, 200, new Color(90, 105, 130), false);
            g2.setColor(new Color(180, 195, 215));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 24));
            g2.drawString("Belum ada data simpanan. Klik [+ CREATE NEW] di bawah!", 640, 430);
        } else {
            for (int i = 0; i < profiles.size() && i < 4; i++) {
                int cardX = 510;
                int cardY = 250 + (i * 120);
                int cardW = 900;
                int cardH = 95;

                CyberUIEffects.drawGlassCard(g2, cardX, cardY, cardW, cardH, new Color(52, 152, 219), false);

                // Status Indicator Dot
                g2.setColor(new Color(0, 215, 255));
                g2.fillOval(cardX + 35, cardY + 32, 30, 30);
                
                // Text Commander Name
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 26));
                g2.drawString("COMMANDER: " + profiles.get(i), cardX + 85, cardY + 56);

                // Badge Load Profile Button
                g2.setColor(new Color(39, 174, 96));
                g2.fillRoundRect(cardX + 680, cardY + 22, 180, 50, 10, 10);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2.drawString("LOAD PROFILE", cardX + 705, cardY + 53);
            }
        }

        // 4. Render Action Buttons (+ CREATE NEW & QUIT GAME)
        btnNewProfile.draw(g2);
        btnQuit.draw(g2);
    }

    public static int getClickedProfileIndex(Point p, int count) {
        if (p == null) return -1;
        for (int i = 0; i < count && i < 4; i++) {
            int cardX = 510;
            int cardY = 250 + (i * 120);
            int cardW = 900;
            int cardH = 95;
            
            // Deteksi area klik mouse pada tiap card profil
            if (p.x >= cardX && p.x <= cardX + cardW && p.y >= cardY && p.y <= cardY + cardH) {
                return i;
            }
        }
        return -1;
    }
}