package view.component.sprite;

import model.npc.Enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BossSpriteRenderer {

    public static void drawTelegraphZone(Graphics2D g2, Enemy en, long tick) {
        if (!en.isBoss || !en.isCharging) return;
        float progress = (float) en.chargeTimer / en.CHARGE_MAX;
        int alphaFill = (int) (progress * 105);
        g2.setColor(new Color(255, 20, 40, Math.min(255, alphaFill)));
        g2.fillOval((int) (en.x - en.telegraphRadius), (int) (en.y - en.telegraphRadius),
                (int) (en.telegraphRadius * 2), (int) (en.telegraphRadius * 2));

        int innerR = (int) (en.telegraphRadius * progress);
        g2.setColor(new Color(255, 80, 80, 140));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawOval((int) en.x - innerR, (int) en.y - innerR, innerR * 2, innerR * 2);

        g2.setColor(new Color(255, 50, 50, 220));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{8, 6}, tick % 14));
        g2.drawOval((int) (en.x - en.telegraphRadius), (int) (en.y - en.telegraphRadius),
                (int) (en.telegraphRadius * 2), (int) (en.telegraphRadius * 2));
    }

    public static void drawBossBody(Graphics2D g2, Enemy en, long tick) {
        float hover = (float) Math.sin(tick * 0.08) * 4.0f;
        // Shadow
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillOval(-30, 22, 60, 16);

        g2.translate(0, hover);

        // Rotating Menacing Energy Ring behind Boss
        AffineTransform preRot = g2.getTransform();
        g2.rotate(tick * 0.03);
        g2.setColor(new Color(231, 76, 60, 140));
        g2.setStroke(new BasicStroke(2.2f));
        g2.drawRect(-26, -26, 52, 52);
        g2.rotate(Math.PI / 4);
        g2.setColor(new Color(255, 195, 0, 140));
        g2.drawRect(-26, -26, 52, 52);
        g2.setTransform(preRot);

        // Mecha Dragon / Titan Wings
        int wingFlap = (int) (Math.sin(tick * 0.18) * 6);
        g2.setColor(new Color(90, 15, 25));
        Polygon leftWing = new Polygon(new int[]{-15, -42, -34, -15}, new int[]{-8, -22 + wingFlap, 8 + wingFlap, 10}, 4);
        Polygon rightWing = new Polygon(new int[]{15, 42, 34, 15}, new int[]{-8, -22 + wingFlap, 8 + wingFlap, 10}, 4);
        g2.fillPolygon(leftWing);
        g2.fillPolygon(rightWing);
        g2.setColor(new Color(255, 80, 60));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(leftWing);
        g2.drawPolygon(rightWing);

        // Boss Armored Body
        Polygon body = new Polygon(new int[]{0, 22, 16, 0, -16, -22}, new int[]{-26, -10, 18, 26, 18, -10}, 6);
        g2.setColor(new Color(45, 18, 28));
        g2.fillPolygon(body);
        g2.setColor(new Color(255, 200, 50));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawPolygon(body);

        // Horns
        g2.setColor(new Color(255, 215, 0));
        g2.fillPolygon(new int[]{-12, -24, -6}, new int[]{-18, -36, -23}, 3);
        g2.fillPolygon(new int[]{12, 24, 6}, new int[]{-18, -36, -23}, 3);

        // Pulsing Reactor Core & Visor
        int corePulse = 7 + (int) (Math.sin(tick * 0.25) * 2);
        g2.setColor(en.isCharging ? Color.YELLOW : new Color(255, 40, 60));
        g2.fillOval(-corePulse, -2, corePulse * 2, corePulse * 2);
        g2.setColor(Color.WHITE);
        g2.fillOval(-3, 2, 6, 6);

        // Boss Glowing Eyes
        g2.setColor(new Color(255, 50, 50));
        g2.fillPolygon(new int[]{-12, -3, -5}, new int[]{-13, -9, -15}, 3);
        g2.fillPolygon(new int[]{12, 3, 5}, new int[]{-13, -9, -15}, 3);
    }
}
