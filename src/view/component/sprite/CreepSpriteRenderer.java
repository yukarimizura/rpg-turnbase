package view.component.sprite;

import model.npc.Enemy;

import java.awt.*;

public class CreepSpriteRenderer {

    public static void drawCreepBody(Graphics2D g2, Enemy en, long tick) {
        // Creep: Cyber-Spider / Void Drone Mecha
        float bob = (float) Math.sin(tick * 0.2 + en.hashCode() % 10) * 2.5f;
        g2.setColor(new Color(0, 0, 0, 95));
        g2.fillOval(-13, 10, 26, 7);

        g2.translate(0, bob);

        // Mechanical Legs / Blades
        int legSwing = (int) (Math.sin(tick * 0.35 + en.hashCode() % 7) * 3);
        g2.setColor(new Color(160, 50, 70));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawLine(-10, -4, -17, -9 + legSwing);
        g2.drawLine(-10, 4, -17, 9 - legSwing);
        g2.drawLine(10, -4, 17, -9 - legSwing);
        g2.drawLine(10, 4, 17, 9 + legSwing);

        // Drone Octagonal Chassis
        Polygon hull = new Polygon(new int[]{0, 12, 12, 0, -12, -12}, new int[]{-13, -6, 6, 13, 6, -6}, 6);
        g2.setColor(new Color(48, 52, 68));
        g2.fillPolygon(hull);
        g2.setColor(new Color(231, 76, 60));
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawPolygon(hull);

        // Glowing Crimson Cyclops Eye
        g2.setColor(new Color(255, 45, 65));
        g2.fillOval(-5, -5, 10, 10);
        g2.setColor(Color.WHITE);
        g2.fillOval(-2, -3, 4, 4);
    }

    public static void drawEnemyHealthBar(Graphics2D g2, Enemy en) {
        // Health Bar above Enemy
        int barW = en.size + 20;
        int barH = 6;
        int barX = (int) (en.x - barW / 2);
        int barY = (int) (en.y - en.size / 2 - 15);

        g2.setColor(new Color(20, 22, 30, 210));
        g2.fillRoundRect(barX - 1, barY - 1, barW + 2, barH + 2, 4, 4);
        float hpRatio = Math.max(0f, Math.min(1f, (float) en.currentHp / en.maxHp));
        g2.setColor(en.isBoss ? new Color(235, 45, 65) : new Color(46, 204, 113));
        g2.fillRoundRect(barX, barY, (int) (barW * hpRatio), barH, 3, 3);
        g2.setColor(new Color(255, 255, 255, 90));
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawRoundRect(barX - 1, barY - 1, barW + 2, barH + 2, 4, 4);
    }
}
