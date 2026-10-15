package model.characters;

import model.characters.skills.DefenderSkillExecutor;
import model.npc.Enemy;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Defender extends Character {
    public Defender(String id, String name, String rarity, String subRole, int maxHp, int attack, int defense) {
        super(id, name, rarity, subRole, maxHp, attack, defense);
    }

    public void triggerResonance(Character vanguard, List<VFXEffect> vfx, List<DamageText> texts) {
        DefenderSkillExecutor.executeResonance(this, vanguard, vfx, texts);
    }

    @Override
    public void castSkill(List<Enemy> enemies) {}

    @Override
    public void castUltimate(List<Enemy> enemies) {}

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(41, 128, 185));
        g2.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
    }

    @Override
    public String getRole() { return "Defender"; }

    @Override
    public boolean isVanguardEligible() { return false; }
}