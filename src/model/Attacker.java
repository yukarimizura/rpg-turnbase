package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Attacker extends Character {

    public Attacker(String id, String name, String rarity, String subRole, int maxHp, int attack, int defense) {
        super(id, name, rarity, subRole, maxHp, attack, defense);
        
        // Atur spesifikasi dasar berdasarkan subrole
        switch (subRole) {
            case "Blade Dancer":
                this.speed = 5.2f;
                this.skillCdMax = 90; // Cepat spam
                break;
            case "Ranger":
                this.speed = 4.2f;
                this.skillCdMax = 160;
                break;
            case "Berserker":
                this.speed = 4.0f;
                this.skillCdMax = 180;
                break;
            case "Spellcaster":
                this.speed = 3.6f;
                this.skillCdMax = 200;
                break;
            case "Artillery":
                this.speed = 3.2f;
                this.skillCdMax = 240;
                break;
            default:
                break;
        }
    }

    @Override
    public void castSkill(List<Enemy> enemies) {
        this.skillCdCurrent = this.skillCdMax;
        int atk = getAttack();

        switch (getSubRole()) {
            case "Blade Dancer":
                // Blink maju dan tebas musuh terdekat
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - x, en.y - y) < 140) {
                        en.currentHp -= (int)(atk * 1.6f);
                    }
                }
                break;
            case "Ranger":
                // Snipe satu target paling dekat dengan damage besar
                Enemy closest = null;
                float minD = Float.MAX_VALUE;
                for (Enemy en : enemies) {
                    float d = (float) Math.hypot(en.x - x, en.y - y);
                    if (d < minD) { minD = d; closest = en; }
                }
                if (closest != null) {
                    closest.currentHp -= (int)(atk * 3.2f);
                }
                break;
            case "Berserker":
                // Damage bertambah besar saat HP sekarat
                float hpPercent = (float) getCurrentHp() / getMaxHp();
                float multi = 1.5f + (1.0f - hpPercent) * 2.0f;
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - x, en.y - y) < 100) {
                        en.currentHp -= (int)(atk * multi);
                    }
                }
                break;
            case "Spellcaster":
                // AoE Lingkaran Besar
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - x, en.y - y) < 220) {
                        en.currentHp -= (int)(atk * 1.8f);
                    }
                }
                break;
            case "Artillery":
                // Tembakan meriam: damage sedang + dorong musuh menjauh
                for (Enemy en : enemies) {
                    float d = (float) Math.hypot(en.x - x, en.y - y);
                    if (d < 180) {
                        en.x += (en.x - x) * 1.2f;
                        en.y += (en.y - y) * 1.2f;
                        en.currentHp -= (int)(atk * 2.0f);
                    }
                }
                break;
            default:
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - x, en.y - y) < 120) en.currentHp -= atk;
                }
                break;
        }
    }

    @Override
    public void castUltimate(List<Enemy> enemies) {
        this.ultCdCurrent = this.ultCdMax;
        int atk = getAttack();
        // Ultimate universal menghantam seluruh arena
        for (Enemy en : enemies) {
            en.currentHp -= (int)(atk * 4.5f);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(231, 76, 60));
        g2.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
        g2.setColor(Color.WHITE);
        g2.drawOval((int) (x - size / 2), (int) (y - size / 2), size, size);
    }

    @Override
    public String getRole() { return "Attacker"; }

    @Override
    public boolean isVanguardEligible() { return true; }
}