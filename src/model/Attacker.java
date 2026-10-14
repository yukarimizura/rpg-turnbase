package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Attacker extends Character {

    public Attacker(String id, String name, String rarity, int maxHp, int attack, int defense) {
        super(id, name, rarity, maxHp, attack, defense);
        this.speed = 4.5f;
        this.skillCdMax = 120; // 2 detik
        this.ultCdMax = 480;   // 8 detik
    }

    @Override
    public String getRole() {
        return "Attacker";
    }

    @Override
    public void draw(Graphics2D g2) {
        // Lingkaran merah menyala khas DPS
        g2.setColor(new Color(231, 76, 60));
        g2.fillOval((int) x - size / 2, (int) y - size / 2, size, size);

        // Core visual tengah
        g2.setColor(new Color(255, 200, 200));
        g2.fillOval((int) x - size / 4, (int) y - size / 4, size / 2, size / 2);

        // Border indikator
        g2.setColor(Color.WHITE);
        g2.drawOval((int) x - size / 2, (int) y - size / 2, size, size);
    }

    @Override
    public void castSkill(List<Enemy> enemies) {
        if (skillCdCurrent > 0) return;
        skillCdCurrent = skillCdMax;

        // Skill [Crimson Slash]: Tebasan area jarak dekat dengan damage tinggi
        for (Enemy en : enemies) {
            float dist = (float) Math.hypot(en.x - x, en.y - y);
            if (dist < 140) {
                int dmg = (int) (getAttack() * 2.2f);
                en.currentHp -= dmg;
            }
        }
    }

    @Override
    public void castUltimate(List<Enemy> enemies) {
        if (ultCdCurrent > 0) return;
        ultCdCurrent = ultCdMax;

        // Ultimate [Blade Storm]: Hancurkan semua musuh di layar
        for (Enemy en : enemies) {
            int ultDmg = (int) (getAttack() * 4.0f);
            en.currentHp -= ultDmg;
        }
    }
}