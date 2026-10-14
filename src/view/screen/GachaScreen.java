package view.screen;

import model.Character;
import service.GachaService;
import service.PlayerProfile;
import view.UIButton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GachaScreen {
    public static void render(Graphics2D g2, PlayerProfile profile, GachaService gacha, String notice,
                             UIButton btnBack, UIButton btn1x, UIButton btn10x) {
        btnBack.draw(g2);

        // Header
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 26));
        g2.drawString("SUMMON PORTAL: STELLAR CHRONO", 220, 60);

        g2.setColor(new Color(0, 210, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString("Gems: " + profile.getCyberGems(), 340, 95);

        g2.setColor(new Color(255, 204, 0));
        g2.drawString("Pity Counter: " + gacha.getPityCounter() + "/50 (Guaranteed SSR)", 460, 95);

        // Tampilan Hasil Summon Kartu Visual
        Character last = gacha.getLastPulledCharacter();
        int cardX = 375, cardY = 130, cardW = 200, cardH = 280;

        g2.setColor(new Color(28, 22, 40));
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 15, 15);

        Color rarityColor = Color.GRAY;
        if (last != null) {
            if ("SSR".equals(last.getRarity())) rarityColor = new Color(255, 215, 0);
            else if ("SR".equals(last.getRarity())) rarityColor = new Color(170, 80, 255);
            else rarityColor = new Color(52, 152, 219);
        }

        g2.setColor(rarityColor);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cardX, cardY, cardW, cardH, 15, 15);

        if (last != null) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            g2.drawString("★ " + last.getRarity() + " ★", cardX + 55, cardY + 40);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString(last.getName(), cardX + 20, cardY + 90);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2.drawString("Role: " + last.getRole(), cardX + 20, cardY + 130);
            g2.drawString("Base HP: " + last.getMaxHp(), cardX + 20, cardY + 160);
            g2.drawString("Base ATK: " + last.getAttack(), cardX + 20, cardY + 190);
            g2.drawString("Base DEF: " + last.getDefense(), cardX + 20, cardY + 220);
        } else {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
            g2.drawString("Lakukan summon untuk", cardX + 25, cardY + 130);
            g2.drawString("melihat karakter", cardX + 45, cardY + 155);
        }

        if (!notice.isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g2.drawString(notice, 330, 435);
        }

        btn1x.draw(g2);
        btn10x.draw(g2);
    }
}