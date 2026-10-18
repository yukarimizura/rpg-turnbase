package view.component;

import model.characters.Character;
import model.npc.Enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class SpriteRenderer {

    public static void drawCharacter(Graphics2D g2, Character c, float centerX, float centerY, float scale) {
        if (c == null) return;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();

        long tick = System.currentTimeMillis() / 16;
        boolean moving = Math.abs(c.vx) > 0.1f || Math.abs(c.vy) > 0.1f;
        float bob = (float) Math.sin(tick * (moving ? 0.35 : 0.1) + c.hashCode() % 10) * (moving ? 3.5f : 2.0f);
        float weaponSwing = (c.skillCdCurrent > c.skillCdMax - 18)
                ? (float) Math.sin((c.skillCdMax - c.skillCdCurrent) * 0.4f) * 1.2f
                : (float) Math.sin(tick * 0.08f) * 0.12f;

        g2.translate(centerX, centerY);
        g2.scale(scale, scale);

        // 1. Ground Shadow
        g2.setColor(new Color(0, 0, 0, 110));
        g2.fillOval(-18, 12, 36, 10);

        // 2. Rarity Ground Aura Ring
        Color rarityColor = getRarityColor(c.getRarity());
        g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), 95));
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawOval(-20, 10, 40, 12);

        if ("SSR".equals(c.getRarity())) {
            double angle = (tick * 0.05) % (Math.PI * 2);
            int orbX = (int) (Math.cos(angle) * 20);
            int orbY = 16 + (int) (Math.sin(angle) * 6);
            g2.setColor(new Color(255, 230, 80, 220));
            g2.fillOval(orbX - 3, orbY - 3, 6, 6);
        }

        // Active Buff Shields / Auras
        if (c.bonusDefTimer > 0) {
            g2.setColor(new Color(52, 152, 219, 75));
            g2.fillOval(-24, -24 + (int) bob, 48, 48);
            g2.setColor(new Color(100, 200, 255, 180));
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5, 4}, tick % 9));
            g2.drawOval(-24, -24 + (int) bob, 48, 48);
        }
        if (c.bonusAtkTimer > 0) {
            g2.setColor(new Color(255, 190, 40, 160));
            g2.setStroke(new BasicStroke(1.5f));
            int auraR = 21 + (int) (Math.sin(tick * 0.25) * 2);
            g2.drawOval(-auraR, -auraR + (int) bob, auraR * 2, auraR * 2);
        }

        g2.translate(0, bob);

        Color primary = getRolePrimaryColor(c.getRole());
        Color accent = getRoleAccentColor(c.getRole(), c.getSubRole());

        // 3. Cyber Cape / Energy Wings
        g2.setColor(new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 160));
        Polygon cape = new Polygon(
                new int[]{-8, 8, 14, -14},
                new int[]{-4, -4, 14, 14},
                4
        );
        g2.fillPolygon(cape);

        // 4. Legs (animated walk)
        int legOffset = moving ? (int) (Math.sin(tick * 0.4) * 4) : 0;
        g2.setColor(new Color(30, 35, 48));
        g2.fillRoundRect(-9, 7 + legOffset, 6, 9, 3, 3);
        g2.fillRoundRect(3, 7 - legOffset, 6, 9, 3, 3);

        // 5. Armored Torso
        g2.setColor(primary);
        g2.fillRoundRect(-11, -7, 22, 16, 6, 6);
        g2.setColor(accent);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(-11, -7, 22, 16, 6, 6);

        // Core Crystal on Chest
        g2.setColor(Color.WHITE);
        g2.fillOval(-3, -2, 6, 6);
        g2.setColor(accent);
        g2.drawOval(-4, -3, 8, 8);

        // Shoulder Pauldrons
        g2.setColor(primary.darker());
        g2.fillRoundRect(-15, -7, 6, 8, 4, 4);
        g2.fillRoundRect(9, -7, 6, 8, 4, 4);
        g2.setColor(accent);
        g2.drawRoundRect(-15, -7, 6, 8, 4, 4);
        g2.drawRoundRect(9, -7, 6, 8, 4, 4);

        // 6. Head & Cyber Helmet / Visor
        g2.setColor(new Color(40, 46, 62));
        g2.fillOval(-10, -22, 20, 17);
        g2.setColor(primary);
        g2.fillArc(-11, -23, 22, 14, 0, 180);

        // Helmet Crest / Horns by Rarity & Role
        g2.setColor(rarityColor);
        Polygon crest = new Polygon(new int[]{0, -5, 0, 5}, new int[]{-28, -20, -22, -20}, 4);
        g2.fillPolygon(crest);

        // Glowing Cyber Visor Eyes
        g2.setColor(accent);
        g2.fillRoundRect(-7, -15, 14, 4, 3, 3);
        g2.setColor(Color.WHITE);
        g2.fillRect(-5, -14, 4, 2);
        g2.fillRect(2, -14, 4, 2);

        // 7. Role & SubRole Specific Weapon
        drawRoleWeapon(g2, c.getRole(), c.getSubRole(), accent, rarityColor, weaponSwing, tick);

        g2.setStroke(oldStroke);
        g2.setTransform(old);
    }

    private static void drawRoleWeapon(Graphics2D g2, String role, String subRole, Color accent, Color rarityColor, float swing, long tick) {
        AffineTransform old = g2.getTransform();
        g2.translate(13, 0);
        g2.rotate(swing);

        if ("Attacker".equals(role)) {
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
        } else if ("Defender".equals(role)) {
            // Hexagonal Aegis Shield
            Polygon shield = new Polygon(
                    new int[]{0, 8, 8, 0, -5, -5},
                    new int[]{-12, -7, 5, 11, 5, -7},
                    6
            );
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

    public static void drawEnemy(Graphics2D g2, Enemy en) {
        if (en == null) return;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();

        long tick = System.currentTimeMillis() / 16;

        // Render Boss Warning Telegraph Zone first (under boss)
        if (en.isBoss && en.isCharging) {
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

        g2.translate(en.x, en.y);

        if (en.isBoss) {
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
            Polygon body = new Polygon(
                    new int[]{0, 22, 16, 0, -16, -22},
                    new int[]{-26, -10, 18, 26, 18, -10},
                    6
            );
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
        } else {
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
            Polygon hull = new Polygon(
                    new int[]{0, 12, 12, 0, -12, -12},
                    new int[]{-13, -6, 6, 13, 6, -6},
                    6
            );
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

        g2.setTransform(old);

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

        g2.setStroke(oldStroke);
    }

    public static Color getRarityColor(String rarity) {
        if ("SSR".equals(rarity)) return new Color(255, 215, 0);
        if ("SR".equals(rarity)) return new Color(185, 95, 255);
        return new Color(52, 162, 235);
    }

    public static Color getRolePrimaryColor(String role) {
        if ("Attacker".equals(role)) return new Color(210, 55, 50);
        if ("Defender".equals(role)) return new Color(41, 128, 185);
        if ("Healer".equals(role)) return new Color(39, 174, 96);
        return new Color(215, 160, 20);
    }

    public static Color getRoleAccentColor(String role, String subRole) {
        if ("Attacker".equals(role)) {
            if ("Spellcaster".equals(subRole)) return new Color(190, 110, 255);
            if ("Ranger".equals(subRole)) return new Color(80, 240, 180);
            if ("Artillery".equals(subRole)) return new Color(255, 150, 50);
            return new Color(255, 95, 85);
        }
        if ("Defender".equals(role)) return new Color(100, 210, 255);
        if ("Healer".equals(role)) return new Color(110, 255, 165);
        return new Color(255, 230, 90);
    }
}
