package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Healer extends Character {

    public Healer(String id, String name, String rarity, int maxHp, int attack, int defense) {
        super(id, name, rarity, maxHp, attack, defense);
        this.speed = 3.8f;
        this.skillCdMax = 150;
        this.ultCdMax = 540;
    }

    @Override
    public String getRole() {
        return "Healer";
    }

    @Override
    public void draw(Graphics2D g2) {
        // Warna hijau terang
        g2.setColor(new Color(46, 204, 113));
        g2.fillOval((int) x - size / 2, (int) y - size / 2, size, size);

        // Simbol salib putih
        g2.setColor(Color.WHITE);
        g2.fillRect((int) x - 2, (int) y - 8, 4, 16);
        g2.fillRect((int) x - 8, (int) y - 2, 16, 4);
    }

    @Override
    public void castSkill(List<Enemy> enemies) {
        if (skillCdCurrent > 0) return;
        skillCdCurrent = skillCdMax;

        // Skill [Holy Pulse]: Heal diri + damage kecil ke musuh sekitar
        heal((int) (getMaxHp() * 0.25f));
        for (Enemy en : enemies) {
            if (Math.hypot(en.x - x, en.y - y) < 180) {
                en.currentHp -= (int) (getAttack() * 1.1f);
            }
        }
    }

    @Override
    public void castUltimate(List<Enemy> enemies) {
        if (ultCdCurrent > 0) return;
        ultCdCurrent = ultCdMax;

        // Ultimate [Resurrection Prayer]: Full Heal
        heal(getMaxHp());
        for (Enemy en : enemies) {
            en.currentHp -= (int) (getAttack() * 2.5f);
        }
    }
}