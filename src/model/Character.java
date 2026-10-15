package model;

import java.awt.Graphics2D;
import java.util.List;

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

    // Realtime 2D Engine Physics
    public float x, y;
    public float vx, vy;
    public float speed = 4.0f;
    public int size = 32;

    // Cooldown Timers (60 frame/tick = 1 detik)
    public int skillCdMax = 180;
    public int skillCdCurrent = 0;
    public int ultCdMax = 600;
    public int ultCdCurrent = 0;

    // Invincibility Frames (Kebal sementara setelah terkena serangan)
    public int iFrameTimer = 0;
    public static final int IFRAME_DURATION = 35; // ~0.58 detik kebal

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

    public void updatePhysics() {
        x += vx;
        y += vy;

        // Batas arena
        x = Math.max(20, Math.min(880, x));
        y = Math.max(20, Math.min(540, y));

        // Cooldown tick
        if (skillCdCurrent > 0) skillCdCurrent--;
        if (ultCdCurrent > 0) ultCdCurrent--;
        if (iFrameTimer > 0) iFrameTimer--;
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
            System.out.println(name + " LEVEL UP! Level: " + level);
        }
    }

    public void heal(int amount) {
        this.currentHp = Math.min(this.maxHp, this.currentHp + amount);
    }

    public void takeDamage(int dmg) {
        // Abaikan damage jika masih dalam masa kebal (I-Frame)
        if (iFrameTimer > 0) return;

        int finalDmg = Math.max(5, dmg - (defense / 3));
        this.currentHp = Math.max(0, this.currentHp - finalDmg);
        this.iFrameTimer = IFRAME_DURATION; // Aktifkan waktu kebal
    }

    public void reviveFull() {
        this.currentHp = this.maxHp;
        this.iFrameTimer = 0;
    }

    public void levelUpManual() {
        level++;
        maxHp += 250;
        currentHp = maxHp;
        attack += 40;
        defense += 15;
    }

    public int getUpgradeCost() {
        return level * 300; // Biaya credits makin tinggi tiap level
    }

    public abstract void draw(Graphics2D g2);
    public abstract String getRole();

    // Getters & Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public int getLevel() { return level; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public boolean isAlive() { return currentHp > 0; }
}