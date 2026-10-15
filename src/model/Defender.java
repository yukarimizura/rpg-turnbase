package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Defender extends Character {
    public Defender(String id, String name, String rarity, int maxHp, int attack, int defense) {
        super(id, name, rarity, maxHp, attack, defense);
    }

    @Override
    public void castSkill(List<Enemy> enemies) {
        this.skillCdCurrent = this.skillCdMax;
        // Knockback musuh terdekat
        for (Enemy en : enemies) {
            float dist = (float) Math.hypot(en.x - x, en.y - y);
            if (dist < 120) {
                en.x += (en.x - x) * 0.8f;
                en.y += (en.y - y) * 0.8f;
                en.currentHp -= (getAttack() * 0.6f);
            }
        }
        // Buff pertahanan masif sementara untuk diri sendiri
        applyDefBuff((int) (getDefense() * 0.6f), 300); // 5 detik buff
    }

    @Override
    public void castUltimate(List<Enemy> enemies) {
        this.ultCdCurrent = this.ultCdMax;
        // Fortress Aegis: Memberikan shield pertahanan +80% ke hero saat ini
        applyDefBuff((int) (getDefense() * 1.2f), 600); // 10 detik buff
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(41, 128, 185));
        g2.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
        g2.setColor(Color.WHITE);
        g2.drawOval((int) (x - size / 2), (int) (y - size / 2), size, size);

        if (bonusDefTimer > 0) {
            g2.setColor(new Color(52, 152, 219, 120));
            g2.drawOval((int) (x - size), (int) (y - size), size * 2, size * 2);
        }
    }

    @Override
    public String getRole() { return "Defender"; }
}