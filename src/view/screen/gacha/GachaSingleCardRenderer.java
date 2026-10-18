package view.screen.gacha;

import model.characters.Character;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GachaSingleCardRenderer {

    public static void drawSinglePullCard(Graphics2D g2, Character c, float scale, int revealTick) {
        AffineTransform old = g2.getTransform();
        g2.translate(475, 275);
        g2.scale(scale, scale);

        Color rarityColor = SpriteRenderer.getRarityColor(c.getRarity());
        int cardW = 220, cardH = 290;
        int cardX = -cardW / 2, cardY = -cardH / 2;

        if (revealTick < 20) {
            int ringR = 70 + revealTick * 6;
            int alpha = Math.max(0, 200 - revealTick * 10);
            g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), alpha));
            g2.setStroke(new BasicStroke(3.0f));
            g2.drawOval(-ringR, -ringR, ringR * 2, ringR * 2);
        }

        CyberUIEffects.drawGlassCard(g2, cardX, cardY, cardW, cardH, rarityColor, true);

        g2.setColor(rarityColor);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        String badge = "★ " + c.getRarity() + " ★";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(badge, -fm.stringWidth(badge) / 2, cardY + 32);

        // Animated Hero Sprite inside Card
        SpriteRenderer.drawCharacter(g2, c, 0, cardY + 95, 1.45f);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        fm = g2.getFontMetrics();
        g2.drawString(c.getName(), -fm.stringWidth(c.getName()) / 2, cardY + 155);

        g2.setColor(new Color(0, 215, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        String roleText = c.getRole() + " • " + c.getSubRole();
        fm = g2.getFontMetrics();
        g2.drawString(roleText, -fm.stringWidth(roleText) / 2, cardY + 176);

        g2.setColor(new Color(210, 220, 235));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("Base HP  : " + c.getMaxHp(), cardX + 28, cardY + 208);
        g2.drawString("Base ATK : " + c.getAttack(), cardX + 28, cardY + 230);
        g2.drawString("Base DEF : " + c.getDefense(), cardX + 28, cardY + 252);

        g2.setColor(c.isVanguardEligible() ? new Color(255, 100, 90) : new Color(80, 220, 140));
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        String squadType = c.isVanguardEligible() ? "[ACTIVE DPS SQUAD]" : "[INACTIVE AUTO-SUPPORT]";
        fm = g2.getFontMetrics();
        g2.drawString(squadType, -fm.stringWidth(squadType) / 2, cardY + 276);

        g2.setTransform(old);
    }
}
