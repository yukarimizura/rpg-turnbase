package model.npc.ai;

import model.npc.Enemy;

public class EnemyAIEngine {

    public static void updateBossAI(Enemy boss, float dx, float dy, float dist) {
        boolean enraged = boss.currentHp < boss.maxHp * 0.5f;
        float bossSpeed = enraged ? boss.speed * 1.45f : boss.speed;

        // Boss AI: Charge Attack Cycle
        if (boss.isCharging) {
            boss.chargeTimer += enraged ? 2 : 1;
            if (boss.chargeTimer >= boss.CHARGE_MAX) {
                boss.burstReady = true;
                boss.isCharging = false;
                boss.chargeTimer = 0;
            }
            return;
        } else {
            if (dist < 190 && Math.random() < (enraged ? 0.025 : 0.015)) {
                boss.isCharging = true;
                boss.chargeTimer = 0;
                return;
            }
            // Tactical lunge when medium distance
            if (dist > 140 && dist < 280 && boss.dashCooldown == 0 && Math.random() < 0.02) {
                boss.x += (dx / dist) * 28f;
                boss.y += (dy / dist) * 28f;
                boss.dashCooldown = 120;
            }
        }

        if (dist > 2) {
            boss.x += (dx / dist) * bossSpeed;
            boss.y += (dy / dist) * bossSpeed;
        }
    }

    public static void updateCreepAI(Enemy creep, float dx, float dy, float dist, int aiTick) {
        // Creep AI: Tactical flanking weave + short lunge
        if (dist > 2) {
            float dirX = dx / dist;
            float dirY = dy / dist;
            // Perpendicular strafe wave so creeps don't just walk in a straight line
            float strafe = (float) Math.sin(aiTick * 0.08f + creep.hashCode() % 9) * 0.45f;
            float moveX = dirX - dirY * strafe;
            float moveY = dirY + dirX * strafe;

            float boost = (dist < 110 && creep.dashCooldown == 0) ? 2.2f : 1.0f;
            if (boost > 1.5f) creep.dashCooldown = 90;

            creep.x += moveX * creep.speed * boost;
            creep.y += moveY * creep.speed * boost;
            creep.x = Math.max(25, Math.min(925, creep.x));
            creep.y = Math.max(60, Math.min(520, creep.y));
        }
    }
}
