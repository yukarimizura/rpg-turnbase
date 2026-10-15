package model;

import java.awt.Graphics2D;

public abstract class Character implements SkillAction {
    private String id;
    private String name;
    private String rarity;
    private int level;
    private int currentExp;
    private int expToNextLevel;

    // Stats
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;

    // Buff Defense Modifier (dari Defender)
    public int bonusDefTimer = 0;
    public int bonusDefAmount = 0;

    // Realtime 2D Engine Physics
    public float x, y;
    public float vx, vy;
    public float speed = 4.0f;
    public int size = 32;

    // Cooldown Timers
    public int skillCdMax = 180;
    public int skillCdCurrent = 0;
    public int ultCdMax = 600;
    public int ultCdCurrent = 0;

    public int iFrameTimer = 0;
    public static final int IFRAME_DURATION = 35;

    public Character(String id, String name, String rarity, int maxHp, int attack, int defense) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.level = 1;
        this.currentExp = 0;
        this.expToNextLevel = 100;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.x = 200;
        this.y = 200;
    }

    // Cooldown berjalan terus untuk SEMUA hero di party
    public void tickCooldown() {
        if (skillCdCurrent > 0) skillCdCurrent--;
        if (ultCdCurrent > 0) ultCdCurrent--;
        if (iFrameTimer > 0) iFrameTimer--;
        if (bonusDefTimer > 0) {
            bonusDefTimer--;
            if (bonusDefTimer == 0) bonusDefAmount = 0;
        }
    }

    public void updatePhysics() {
        x += vx;
        y += vy;
        x = Math.max(20, Math.min(880, x));
        y = Math.max(20, Math.min(540, y));
    }

    public void applyDefBuff(int bonus, int durationTicks) {
        this.bonusDefAmount = bonus;
        this.bonusDefTimer = durationTicks;
    }

    public void gainExp(int amount) {
        currentExp += amount;
        while (currentExp >= expToNextLevel) {
            currentExp -= expToNextLevel;
            level++;
            expToNextLevel = (int) (expToNextLevel * 1.5);
            maxHp += 250;
            currentHp = maxHp;
            attack += 40;
            defense += 15;
        }
    }

    public void levelUpManual() {
        level++;
        maxHp += 250;
        currentHp = maxHp;
        attack += 40;
        defense += 15;
    }

    public int getUpgradeCost() {
        return level * 300;
    }

    public void heal(int amount) {
        this.currentHp = Math.min(this.maxHp, this.currentHp + amount);
    }

    public void takeDamage(int dmg) {
        if (iFrameTimer > 0) return;
        int totalDef = defense + bonusDefAmount;
        int finalDmg = Math.max(5, dmg - (totalDef / 3));
        this.currentHp = Math.max(0, this.currentHp - finalDmg);
        this.iFrameTimer = IFRAME_DURATION;
    }

    public void reviveFull() {
        this.currentHp = this.maxHp;
        this.iFrameTimer = 0;
        this.bonusDefAmount = 0;
        this.bonusDefTimer = 0;
    }

    public abstract void draw(Graphics2D g2);
    public abstract String getRole();

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public int getLevel() { return level; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense + bonusDefAmount; }
    public boolean isAlive() { return currentHp > 0; }
}