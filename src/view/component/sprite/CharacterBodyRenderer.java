package view.component.sprite;

import model.characters.Character;

import java.awt.*;

public class CharacterBodyRenderer {

    public static void drawAuraAndShadow(Graphics2D g2, Character c, Color rarityColor, float bob, long tick) {
        // 1. Ground Shadow
        g2.setColor(new Color(0, 0, 0, 110));
        g2.fillOval(-18, 12, 36, 10);

        // 2. Rarity Ground Aura Ring
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
    }

    public static void drawChibiBody(Graphics2D g2, Color primary, Color accent, Color rarityColor, boolean moving, long tick) {
        // 3. Cyber Cape / Energy Wings
        g2.setColor(new Color(primary.getRed(), primary.getGreen(), primary.getBlue(), 160));
        Polygon cape = new Polygon(new int[]{-8, 8, 14, -14}, new int[]{-4, -4, 14, 14}, 4);
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
    }
}
