package view.screen;

import service.PartyManager;
import service.StageManager;
import view.component.DamageText;
import view.component.MainHUD;
import view.component.UIButton;
import view.component.VFXEffect;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import model.characters.Character;
import model.npc.Enemy;

public class BattleScreen {
    public static void render(Graphics2D g2, List<Enemy> enemies, PartyManager party, 
                             StageManager stage, UIButton btnBack, 
                             List<DamageText> damageTexts, List<VFXEffect> vfxList) {
        // 1. Gambar gelombang efek skill (di layer belakang/tanah)
        for (VFXEffect vfx : vfxList) {
            vfx.draw(g2);
        }

        // 2. Gambar musuh
        for (Enemy en : enemies) {
            en.draw(g2);
        }

        // 3. Gambar hero aktif
        Character hero = party.getActiveHero();
        if (hero != null && hero.isAlive()) {
            if (hero.iFrameTimer % 4 < 2) {
                hero.draw(g2);
            }
        }

        // 4. Gambar angka damage melayang
        for (DamageText dt : damageTexts) {
            dt.draw(g2);
        }

        // 5. Gambar HUD & UI
        MainHUD.draw(g2, hero, party, stage, enemies);
        btnBack.draw(g2);

        g2.setColor(new Color(255, 255, 255, 120));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("Tekan [ESC] atau klik tombol di atas untuk kembali ke Hub", 20, 580);
    }
}