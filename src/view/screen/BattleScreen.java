package view.screen;

import model.Character;
import model.Enemy;
import service.PartyManager;
import service.StageManager;
import view.MainHUD;
import view.UIButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

public class BattleScreen {
    public static void render(Graphics2D g2, List<Enemy> enemies, PartyManager party, 
                             StageManager stage, UIButton btnBack) {
        for (Enemy en : enemies) {
            en.draw(g2);
        }

        Character hero = party.getActiveHero();
        if (hero != null && hero.isAlive()) {
            if (hero.iFrameTimer % 4 < 2) {
                hero.draw(g2);
            }
        }

        MainHUD.draw(g2, hero, party, stage, enemies);
        btnBack.draw(g2);

        g2.setColor(new Color(255, 255, 255, 120));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("Tekan [ESC] atau klik tombol di atas untuk kembali ke Hub", 20, 580);
    }
}