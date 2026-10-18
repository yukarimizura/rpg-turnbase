package model.characters;

import model.characters.skills.AttackerSkillExecutor;
import model.npc.Enemy;

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
        view.component.SpriteRenderer.drawCharacter(g2, this, x, y, 1.0f);
    }

    @Override
    public String getRole() { return "Attacker"; }

    @Override
    public boolean isVanguardEligible() { return true; }
}