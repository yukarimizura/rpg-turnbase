package model.npc;

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

    private int aiTick = (int) (Math.random() * 120);
    private int dashCooldown = 0;

    public void updateAI(float targetX, float targetY) {
        aiTick++;
        if (dashCooldown > 0) dashCooldown--;

        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.hypot(dx, dy);

        if (isBoss) {
            boolean enraged = currentHp < maxHp * 0.5f;
            float bossSpeed = enraged ? speed * 1.45f : speed;

            // Boss AI: Charge Attack Cycle
            if (isCharging) {
                chargeTimer += enraged ? 2 : 1;
                if (chargeTimer >= CHARGE_MAX) {
                    burstReady = true;
                    isCharging = false;
                    chargeTimer = 0;
                }
                return;
            } else {
                if (dist < 190 && Math.random() < (enraged ? 0.025 : 0.015)) {
                    isCharging = true;
                    chargeTimer = 0;
                    return;
                }
                // Tactical lunge when medium distance
                if (dist > 140 && dist < 280 && dashCooldown == 0 && Math.random() < 0.02) {
                    x += (dx / dist) * 28f;
                    y += (dy / dist) * 28f;
                    dashCooldown = 120;
                }
            }

            if (dist > 2) {
                x += (dx / dist) * bossSpeed;
                y += (dy / dist) * bossSpeed;
            }
            return;
        }

        // Creep AI: Tactical flanking weave + short lunge
        if (dist > 2) {
            float dirX = dx / dist;
            float dirY = dy / dist;
            // Perpendicular strafe wave so creeps don't just walk in a straight line
            float strafe = (float) Math.sin(aiTick * 0.08f + this.hashCode() % 9) * 0.45f;
            float moveX = dirX - dirY * strafe;
            float moveY = dirY + dirX * strafe;

            float boost = (dist < 110 && dashCooldown == 0) ? 2.2f : 1.0f;
            if (boost > 1.5f) dashCooldown = 90;

            x += moveX * speed * boost;
            y += moveY * speed * boost;
            x = Math.max(25, Math.min(925, x));
            y = Math.max(60, Math.min(520, y));
        }
    }

    public void draw(Graphics2D g2) {
        view.component.SpriteRenderer.drawEnemy(g2, this);
    }
}