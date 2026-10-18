package view.component.sprite;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class CharacterWeaponRenderer {

    public static void drawRoleWeapon(Graphics2D g2, String role, String subRole, Color accent, Color rarityColor, float swing, long tick) {
        AffineTransform old = g2.getTransform();
        g2.translate(13, 0);
        g2.rotate(swing);

        if ("Attacker".equals(role)) {
            drawAttackerWeapon(g2, subRole, accent, rarityColor);
        } else if ("Defender".equals(role)) {
            // Hexagonal Aegis Shield
            Polygon shield = new Polygon(new int[]{0, 8, 8, 0, -5, -5}, new int[]{-12, -7, 5, 11, 5, -7}, 6);
            g2.setColor(new Color(41, 128, 185, 230));
            g2.fillPolygon(shield);
            g2.setColor(accent);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawPolygon(shield);
        } else if ("Healer".equals(role)) {
            // Holy Caduceus Staff + Orbiting Heal Motes
            g2.setColor(new Color(220, 230, 210));
            g2.setStroke(new BasicStroke(2.2f));
            g2.drawLine(2, -12, 2, 12);
            g2.setColor(new Color(46, 204, 113));
            g2.fillOval(-3, -18, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawLine(2, -16, 2, -10);
            g2.drawLine(-1, -13, 5, -13);
        } else {
            // Support: Floating Chrono / Tactical Ring
            g2.rotate(tick * 0.06);
            g2.setColor(accent);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawOval(-7, -7, 14, 14);
            g2.setColor(Color.WHITE);
            g2.fillOval(-3, -3, 6, 6);
        }

        g2.setTransform(old);
    }

    private static void drawAttackerWeapon(Graphics2D g2, String subRole, Color accent, Color rarityColor) {
        if ("Ranger".equals(subRole)) {
            // Cyber Energy Bow
            g2.setColor(accent);
            g2.setStroke(new BasicStroke(2.2f));
            g2.drawArc(-6, -14, 14, 28, -75, 150);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawLine(0, -13, 0, 13);
        } else if ("Spellcaster".equals(subRole)) {
            // Arcane Staff with Floating Orb
            g2.setColor(new Color(140, 100, 60));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawLine(2, -14, 2, 12);
            g2.setColor(accent);
            g2.fillOval(-3, -21, 10, 10);
            g2.setColor(Color.WHITE);
            g2.fillOval(-1, -19, 5, 5);
        } else if ("Artillery".equals(subRole)) {
            // Plasma Cannon
            g2.setColor(new Color(55, 65, 82));
            g2.fillRoundRect(-2, -6, 16, 9, 4, 4);
            g2.setColor(accent);
            g2.fillOval(11, -5, 5, 7);
        } else if ("Berserker".equals(subRole)) {
            // Giant Crimson Cleaver / Battleaxe
            g2.setColor(new Color(180, 180, 195));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawLine(2, -16, 2, 12);
            g2.setColor(accent);
            Polygon axe = new Polygon(new int[]{2, 13, 11, 2}, new int[]{-15, -18, -4, -6}, 4);
            g2.fillPolygon(axe);
            g2.setColor(Color.WHITE);
            g2.drawPolygon(axe);
        } else {
            // Default / Blade Dancer: Dual Neon Katana
            g2.setColor(accent);
            g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(2, 4, 16, -18);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawLine(2, 4, 16, -18);
            // Guard
            g2.setColor(rarityColor);
            g2.fillRect(-1, 2, 7, 3);
        }
    }
}
