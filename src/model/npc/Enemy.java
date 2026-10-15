package model;

import java.awt.BasicStroke;
import java.awt.Color;
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

    public void updateAI(float targetX, float targetY) {
        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.hypot(dx, dy);

        if (isBoss) {
            // Boss AI: Charge Attack Cycle
            if (isCharging) {
                chargeTimer++;
                if (chargeTimer >= CHARGE_MAX) {
                    burstReady = true;
                    isCharging = false;
                    chargeTimer = 0;
                }
                return; // Boss diam di tempat saat charging jurus
            } else {
                // Tiap jarak dekat, 1.5% chance per frame memicu telegraph attack
                if (dist < 180 && Math.random() < 0.015) {
                    isCharging = true;
                    chargeTimer = 0;
                    return;
                }
            }
        }

        // Kejar Hero
        if (dist > 2) {
            x += (dx / dist) * speed;
            y += (dy / dist) * speed;
        }
    }

    public void draw(Graphics2D g2) {
        // Render Red Warning Telegraph Zone
        if (isBoss && isCharging) {
            float progress = (float) chargeTimer / CHARGE_MAX;
            int alphaFill = (int) (progress * 90);
            g2.setColor(new Color(255, 0, 0, Math.min(255, alphaFill)));
            g2.fillOval((int) (x - telegraphRadius), (int) (y - telegraphRadius), (int) (telegraphRadius * 2), (int) (telegraphRadius * 2));

            g2.setColor(new Color(255, 50, 50, 200));
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6}, 0));
            g2.drawOval((int) (x - telegraphRadius), (int) (y - telegraphRadius), (int) (telegraphRadius * 2), (int) (telegraphRadius * 2));
        }

        // Render Badan Musuh / Boss
        if (isBoss) {
            g2.setColor(new Color(180, 40, 40));
            g2.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval((int) (x - size / 2), (int) (y - size / 2), size, size);
        } else {
            g2.setColor(new Color(120, 120, 120));
            g2.fillOval((int) (x - size / 2), (int) (y - size / 2), size, size);
        }

        // Health Bar di atas kepala
        int barW = size + 16;
        int barH = 5;
        int barX = (int) (x - barW / 2);
        int barY = (int) (y - size / 2 - 10);

        g2.setColor(new Color(40, 40, 40, 180));
        g2.fillRect(barX, barY, barW, barH);
        float hpRatio = (float) currentHp / maxHp;
        g2.setColor(isBoss ? new Color(230, 40, 40) : new Color(46, 204, 113));
        g2.fillRect(barX, barY, (int) (barW * hpRatio), barH);
    }
}