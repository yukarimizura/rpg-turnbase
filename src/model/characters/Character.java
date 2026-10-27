package model.characters;

import model.characters.skills.SkillAction;
import model.characters.stats.CharacterCombatHelper;

import java.awt.Graphics2D;

public abstract class Character implements SkillAction {
    private String id, name, rarity, subRole;
    private int level = 1, currentExp = 0, expToNextLevel = 100;

    // Stats
    private int maxHp, currentHp, attack, defense;

    // Buff Modifiers
    public int bonusDefTimer = 0, bonusDefAmount = 0, bonusAtkTimer = 0, bonusAtkAmount = 0;

    // Realtime 2D Physics - Default spawn di pusat layar 1920x1080 (960, 540)
    public float x = 960, y = 540, vx, vy, speed = 4.0f;
    public int size = 32;

    // Cooldown Timers
    public int skillCdMax = 180, skillCdCurrent = 0, ultCdMax = 600, ultCdCurrent = 0;
    public int iFrameTimer = 0;
    public static final int IFRAME_DURATION = 35;
    public int autoAttackCdMax = 40;
    public int autoAttackCdCurrent = 0;
    public int attackRange = 120;

    public Character(String id, String name, String rarity, String subRole, int maxHp, int attack, int defense) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.subRole = subRole;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
    }

    public void tickCooldown() { 
    		CharacterCombatHelper.tickTimers(this);
    		if ( autoAttackCdCurrent > 0 ) autoAttackCdCurrent--;
    	}
    public void updatePhysics() { CharacterCombatHelper.clampArenaBounds(this); }

    public void applyDefBuff(int bonus, int durationTicks) {
        this.bonusDefAmount = bonus;
        this.bonusDefTimer = durationTicks;
    }

    public void applyAtkBuff(int bonus, int durationTicks) {
        this.bonusAtkAmount = bonus;
        this.bonusAtkTimer = durationTicks;
    }

    public void gainExp(int amount) {
        currentExp += amount;
        while (currentExp >= expToNextLevel) {
            currentExp -= expToNextLevel;
            expToNextLevel = (int) (expToNextLevel * 1.5);
            levelUpManual();
        }
    }

    public void levelUpManual() {
        level++;
        maxHp += 250;
        currentHp = maxHp;
        attack += 40;
        defense += 15;
    }

    public int getUpgradeCost() { return level * 300; }
    public void heal(int amount) { this.currentHp = Math.min(this.maxHp, this.currentHp + amount); }

    public void takeDamage(int dmg) {
        if (iFrameTimer > 0) return;
        int finalDmg = CharacterCombatHelper.calculateMitigatedDamage(dmg, getDefense());
        this.currentHp = Math.max(0, this.currentHp - finalDmg);
        this.iFrameTimer = IFRAME_DURATION;
    }

    public void reviveFull() {
        this.currentHp = this.maxHp;
        this.iFrameTimer = 0;
        this.bonusDefAmount = 0; this.bonusDefTimer = 0;
        this.bonusAtkAmount = 0; this.bonusAtkTimer = 0;
    }

    public abstract void draw(Graphics2D g2);
    public abstract String getRole();
    public abstract boolean isVanguardEligible();

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public String getSubRole() { return subRole; }
    public int getLevel() { return level; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack + bonusAtkAmount; }
    public int getDefense() { return defense + bonusDefAmount; }
    public float getMoveSpeed() { return speed; }
    public boolean isAlive() { return currentHp > 0; }
}