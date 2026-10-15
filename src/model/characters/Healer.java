package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Healer extends Character {
    public Healer(String id, String name, String rarity, String subRole, int maxHp, int attack, int defense) {
        super(id, name, rarity, subRole, maxHp, attack, defense);
    }

    @Override
    public void castSkill(List<Enemy> enemies) {}

    @Override
    public void castUltimate(List<Enemy> enemies) {}

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(46, 204, 113));
        g2.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
    }

    @Override
    public String getRole() { return "Healer"; }

    @Override
    public boolean isVanguardEligible() { return false; }
}