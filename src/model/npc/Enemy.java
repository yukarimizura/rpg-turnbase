package model.npc;

import model.npc.ai.EnemyAIEngine;

import java.awt.Graphics2D;

public class Enemy {
    public float x, y;
    public float vx, vy;
    public float speed;
    public int size;
    public int maxHp;
    public int currentHp;
    public int attack;
    public int expReward;
    public boolean isBoss;

    // Boss Telegraph Attack Variables
    public boolean isCharging = false;
    public int chargeTimer = 0;
    public final int CHARGE_MAX = 90; // 1.5 detik warning
    public float telegraphRadius = 130f;
    public boolean burstReady = false;

    private int aiTick = (int) (Math.random() * 120);
    public int dashCooldown = 0;

    public Enemy(float x, float y, boolean isBoss, int stageLevel) {
        this.x = x;
        this.y = y;
        this.isBoss = isBoss;

        if (isBoss) {
            this.size = 54;
            this.maxHp = 6000 * stageLevel;
            this.currentHp = maxHp;
            this.attack = 180 + (stageLevel * 40);
            this.speed = 1.3f;
            this.expReward = 500 * stageLevel;
        } else {
            this.size = 26;
            this.maxHp = 450 + (stageLevel * 120);
            this.currentHp = maxHp;
            this.attack = 40 + (stageLevel * 10);
            this.speed = 2.0f;
            this.expReward = 45 * stageLevel;
        }
    }

    public void updateAI(float targetX, float targetY) {
        aiTick++;
        if (dashCooldown > 0) dashCooldown--;

        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.hypot(dx, dy);

        if (isBoss) {
            EnemyAIEngine.updateBossAI(this, dx, dy, dist);
        } else {
            EnemyAIEngine.updateCreepAI(this, dx, dy, dist, aiTick);
        }
    }

    public void draw(Graphics2D g2) {
        view.component.SpriteRenderer.drawEnemy(g2, this);
    }
}