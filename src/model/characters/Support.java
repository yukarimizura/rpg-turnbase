package model.characters;

import model.characters.skills.SupportSkillExecutor;
import model.npc.Enemy;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Graphics2D;
import java.util.List;

public class Support extends Character {
    public Support(String id, String name, String rarity, String subRole, int maxHp, int attack, int defense) {
        super(id, name, rarity, subRole, maxHp, attack, defense);
        this.skillCdMax = "Chronomancer".equals(subRole) ? 160 : 200;
    }

    public void triggerResonance(Character vanguard, List<VFXEffect> vfx, List<DamageText> texts) {
        this.skillCdCurrent = this.skillCdMax;
        SupportSkillExecutor.executeResonance(this, vanguard, vfx, texts);
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
    public String getRole() { return "Support"; }

    @Override
    public boolean isVanguardEligible() { return false; }
}