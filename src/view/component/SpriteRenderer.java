package view.component;

import model.characters.Character;
import model.npc.Enemy;
import view.component.sprite.*;

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

        Color rarityColor = getRarityColor(c.getRarity());
        CharacterBodyRenderer.drawAuraAndShadow(g2, c, rarityColor, bob, tick);

        g2.translate(0, bob);

        Color primary = getRolePrimaryColor(c.getRole());
        Color accent = getRoleAccentColor(c.getRole(), c.getSubRole());
        CharacterBodyRenderer.drawChibiBody(g2, primary, accent, rarityColor, moving, tick);

        // 7. Role & SubRole Specific Weapon
        CharacterWeaponRenderer.drawRoleWeapon(g2, c.getRole(), c.getSubRole(), accent, rarityColor, weaponSwing, tick);

        g2.setStroke(oldStroke);
        g2.setTransform(old);
    }

    public static void drawEnemy(Graphics2D g2, Enemy en) {
        if (en == null) return;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();

        long tick = System.currentTimeMillis() / 16;

        // Render Boss Warning Telegraph Zone first (under boss)
        BossSpriteRenderer.drawTelegraphZone(g2, en, tick);

        g2.translate(en.x, en.y);
        if (en.isBoss) {
            BossSpriteRenderer.drawBossBody(g2, en, tick);
        } else {
            CreepSpriteRenderer.drawCreepBody(g2, en, tick);
        }
        g2.setTransform(old);

        CreepSpriteRenderer.drawEnemyHealthBar(g2, en);
        g2.setStroke(oldStroke);
    }

    public static Color getRarityColor(String rarity) {
        return SpriteColorPalette.getRarityColor(rarity);
    }

    public static Color getRolePrimaryColor(String role) {
        return SpriteColorPalette.getRolePrimaryColor(role);
    }

    public static Color getRoleAccentColor(String role, String subRole) {
        return SpriteColorPalette.getRoleAccentColor(role, subRole);
    }
}
