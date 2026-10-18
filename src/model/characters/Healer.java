package model.characters;

import model.characters.skills.HealerSkillExecutor;
import model.npc.Enemy;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Graphics2D;
import java.util.List;

public class Healer extends Character {
    public Healer(String id, String name, String rarity, String subRole, int maxHp, int attack, int defense) {
        super(id, name, rarity, subRole, maxHp, attack, defense);
        this.skillCdMax = "Druid Renewal".equals(subRole) ? 140 : 190;
    }

    public void triggerResonance(Character vanguard, List<VFXEffect> vfx, List<DamageText> texts) {
        this.skillCdCurrent = this.skillCdMax;
        HealerSkillExecutor.executeResonance(this, vanguard, vfx, texts);
    }

    @Override
    public void castSkill(List<Enemy> enemies) {}

    @Override
    public void castUltimate(List<Enemy> enemies) {}

    @Override
    public void draw(Graphics2D g2) {
        view.component.SpriteRenderer.drawCharacter(g2, this, x, y, 1.0f);
    }

    @Override
    public String getRole() { return "Healer"; }

    @Override
    public boolean isVanguardEligible() { return false; }
}