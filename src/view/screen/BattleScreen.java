package view.screen;

import service.PartyManager;
import service.StageManager;
import view.component.CyberUIEffects;
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
        CyberUIEffects.drawCyberBackground(g2, true);

        int shakeX = CyberUIEffects.getShakeOffsetX();
        int shakeY = CyberUIEffects.getShakeOffsetY();
        g2.translate(shakeX, shakeY);

        // 1. Gambar gelombang efek skill (di layer belakang/tanah)
        for (VFXEffect vfx : vfxList) {
            vfx.draw(g2);
        }

        // 2. Gambar musuh
        for (Enemy en : enemies) {
            en.draw(g2);
        }

        // 3. Gambar hero DPS aktif
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

        g2.translate(-shakeX, -shakeY);

        // 5. Gambar HUD & UI
        MainHUD.draw(g2, hero, party, stage, enemies);
        btnBack.draw(g2);

        g2.setColor(new Color(200, 220, 255, 150));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.drawString("WASD: Move  |  [Q] Skill  [R] Ultimate  |  [1-5] Switch Active DPS  |  [ESC] Return to Hub", 20, 588);
    }
}