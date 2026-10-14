package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Defender extends Character {

    public Defender(String id, String name, String rarity, int maxHp, int attack, int defense) {
        super(id, name, rarity, maxHp, attack, defense);
        this.speed = 3.2f;
        this.size = 38; // Lebih besar
        this.skillCdMax = 180;
        this.ultCdMax = 600;
    }

    @Override
    public String getRole() {
        return "Defender";
    }

    @Override
    public void draw(Graphics2D g2) {
        // Warna biru perisai
        g2.setColor(new Color(52, 152, 219));
        g2.fillRoundRect((int) x - size / 2, (int) y - size / 2, size, size, 8, 8);

        // Aura shield
        g2.setColor(new Color(41, 128, 185));
        g2.drawRoundRect((int) x - size / 2 - 2, (int) y - size / 2 - 2, size + 4, size + 4, 10, 10);
    }

    @Override
    public void castSkill(List<Enemy> enemies) {
        if (skillCdCurrent > 0) return;
        skillCdCurrent = skillCdMax;

        // Skill [Shield Slam]: Knockback dan stun damage
        for (Enemy en : enemies) {
            float dist = (float) Math.hypot(en.x - x, en.y - y);
            if (dist < 160) {
                en.currentHp -= (int) (getAttack() * 1.4f);
                // Dorong musuh menjauh
                float dx = en.x - x;
                float dy = en.y - y;
                if (dist > 0) {
                    en.x += (dx / dist) * 60;
                    en.y += (dy / dist) * 60;
                }
            }
        }
    }

    @Override
    public void castUltimate(List<Enemy> enemies) {
        if (ultCdCurrent > 0) return;
        ultCdCurrent = ultCdMax;

        // Ultimate [Fortress Aegis]: Pulihkan HP sendiri + damage ke sekitar
        heal((int) (getMaxHp() * 0.4f));
        for (Enemy en : enemies) {
            en.currentHp -= getAttack() * 2;
        }
    }
}