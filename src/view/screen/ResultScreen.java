package view.screen;

import service.StageManager;
import view.UIButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ResultScreen {
    public static void render(Graphics2D g2, boolean victory, StageManager stage, 
                             int gems, int credits, UIButton btnReturn) {
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(0, 0, 950, 600);

        g2.setFont(new Font("SansSerif", Font.BOLD, 40));
        if (victory) {
            g2.setColor(new Color(46, 204, 113));
            g2.drawString("STAGE CLEARED!", 310, 200);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g2.drawString("Stage Boss Berhasil Ditumbangkan!", 335, 245);

            g2.setColor(new Color(0, 210, 255));
            g2.drawString("+ " + gems + " Cyber Gems", 400, 290);
            g2.setColor(new Color(255, 204, 0));
            g2.drawString("+ " + credits + " Credits", 420, 325);
            g2.setColor(Color.GREEN);
            g2.drawString("Stage Berikutnya Terbuka: Stage " + stage.getCurrentStage(), 310, 360);
        } else {
            g2.setColor(new Color(231, 76, 60));
            g2.drawString("ALL PARTY DEFEATED!", 260, 220);

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g2.drawString("Seluruh anggota regu tempur telah gugur.", 315, 270);
            g2.drawString("Lakukan grinding EXP atau summon karakter baru!", 285, 305);
        }

        btnReturn.draw(g2);
    }
}