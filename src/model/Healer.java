package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Healer extends Character {
    private List<Character> partyRef;

    public Healer(String id, String name, String rarity, int maxHp, int attack, int defense) {
        super(id, name, rarity, maxHp, attack, defense);
    }

    public void setPartyReference(List<Character> party) {
        this.partyRef = party;
    }

    @Override
    public void castSkill(List<Enemy> enemies) {
        this.skillCdCurrent = this.skillCdMax;
        int healAmt = (int) (getMaxHp() * 0.35f);
        this.heal(healAmt);

        // Heal anggota party cadangan (terutama Attacker)
        if (partyRef != null) {
            for (Character c : partyRef) {
                if (c.isAlive()) c.heal(healAmt);
            }
        }
    }

    @Override
    public void castUltimate(List<Enemy> enemies) {
        this.ultCdCurrent = this.ultCdMax;
        // Divine Prayer: Pulihkan 75% HP seluruh party
        int fullHeal = (int) (getMaxHp() * 0.75f);
        this.heal(fullHeal);

        if (partyRef != null) {
            for (Character c : partyRef) {
                if (c.isAlive()) c.heal(fullHeal);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(46, 204, 113));
        g2.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
        g2.setColor(Color.WHITE);
        g2.drawOval((int) (x - size / 2), (int) (y - size / 2), size, size);
    }

    @Override
    public String getRole() { return "Healer"; }
}