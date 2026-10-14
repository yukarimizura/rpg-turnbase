package model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Enemy {
    public float x, y;
    public int maxHp, currentHp, attack, expReward;
    public boolean isBoss;
    public int size;
    private float speed;

    public Enemy(float x, float y, boolean isBoss, int stageLevel) {
        this.x = x;
        this.y = y;
        this.isBoss = isBoss;

        if (isBoss) {
            this.size = 64;
            this.maxHp = 5000 * stageLevel;
            this.attack = 40 + (stageLevel * 20);
            this.expReward = 500 * stageLevel;
            this.speed = 1.2f;
        } else {
            this.size = 24;
            this.maxHp = 300 * stageLevel;
            this.attack = 15 + (stageLevel * 5);
            this.expReward = 45 * stageLevel;
            this.speed = 2.0f;
        }
        this.currentHp = this.maxHp;
    }

    public void updateAI(float targetX, float targetY) {
        // Simple Chase AI menuju hero
        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist > 5) {
            x += (dx / dist) * speed;
            y += (dy / dist) * speed;
        }
    }

    public void draw(Graphics2D g2) {
        // Render badan musuh
        g2.setColor(isBoss ? new Color(178, 34, 34) : new Color(105, 105, 105));
        g2.fillOval((int) x - size / 2, (int) y - size / 2, size, size);

        // Health bar terapung
        g2.setColor(Color.RED);
        g2.fillRect((int) x - size / 2, (int) y - size / 2 - 10, size, 5);
        g2.setColor(Color.GREEN);
        int hpWidth = (int) (((float) currentHp / maxHp) * size);
        g2.fillRect((int) x - size / 2, (int) y - size / 2 - 10, hpWidth, 5);
    }
}