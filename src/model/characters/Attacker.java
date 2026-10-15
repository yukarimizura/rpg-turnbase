package model.characters;

import model.Enemy;
import model.characters.attacker.AttackerSkillExecutor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Attacker extends Character {
    public Attacker(String id, String name, String rarity, String subRole, int maxHp, int attack, int defense) {
        super(id, name, rarity, subRole, maxHp, attack, defense);
        if ("Blade Dancer".equals(subRole)) { this.speed = 5.2f; this.skillCdMax = 90; }
        else if ("Ranger".equals(subRole))  { this.speed = 4.2f; this.skillCdMax = 160; }
        else if ("Berserker".equals(subRole)) { this.speed = 4.0f; this.skillCdMax = 180; }
        else if ("Spellcaster".equals(subRole)) { this.speed = 3.6f; this.skillCdMax = 200; }
        else if ("Artillery".equals(subRole)) { this.speed = 3.2f; this.skillCdMax = 240; }
    }

    @Override
    public void castSkill(List<Enemy> enemies) {
        this.skillCdCurrent = this.skillCdMax;
        AttackerSkillExecutor.executeSkill(this, enemies);
    }

    @Override
    public void castUltimate(List<Enemy> enemies) {
        this.ultCdCurrent = this.ultCdMax;
        int atk = getAttack();
        for (Enemy en : enemies) en.currentHp -= (int)(atk * 4.5f);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(231, 76, 60));
        g2.fillOval((int)(x - size / 2), (int)(y - size / 2), size, size);
        g2.setColor(Color.WHITE);
        g2.drawOval((int)(x - size / 2), (int)(y - size / 2), size, size);
    }

    @Override
    public String getRole() { return "Attacker"; }

    @Override
    public boolean isVanguardEligible() { return true; }
}