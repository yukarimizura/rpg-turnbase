package view.screen;

import service.StageManager;
import view.component.CyberUIEffects;
import view.component.UIButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ResultScreen {
    public static void render(Graphics2D g2, boolean victory, StageManager stage, 
                             int gems, int credits, UIButton btnReturn) {
        CyberUIEffects.drawCyberBackground(g2, false);

        Color accent = victory ? new Color(46, 204, 113) : new Color(231, 76, 60);
        CyberUIEffects.drawGlassCard(g2, 225, 120, 500, 350, accent, true);

        g2.setFont(new Font("SansSerif", Font.BOLD, 36));
        if (victory) {
            g2.setColor(new Color(46, 204, 113));
            g2.drawString("STAGE CLEARED!", 315, 185);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.drawString("Overlord Stage Boss berhasil ditumbangkan!", 315, 225);

            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.setColor(new Color(0, 225, 255));
            g2.drawString("◈ + " + gems + " Cyber Gems", 385, 275);
            g2.setColor(new Color(255, 215, 0));
            g2.drawString("⬡ + " + credits + " Credits", 395, 310);

            g2.setColor(new Color(110, 255, 165));
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.drawString("Stage Berikutnya Terbuka: STAGE " + stage.getCurrentStage(), 330, 355);
        } else {
            g2.setColor(new Color(231, 76, 60));
            g2.drawString("SQUAD DEFEATED!", 305, 195);

            g2.setColor(new Color(210, 220, 235));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.drawString("Seluruh anggota Active DPS Squad telah gugur.", 305, 255);
            g2.setColor(new Color(255, 215, 0));
            g2.drawString("Tip: Pasang Healer & Defender di Inactive Support Squad!", 265, 295);
        }

        btnReturn.draw(g2);
    }
}