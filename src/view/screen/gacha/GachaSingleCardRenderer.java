package view.screen.gacha;

import model.characters.Character;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GachaSingleCardRenderer {

    public static void drawSinglePullCard(Graphics2D g2, Character c, float scale, int revealTick) {
        if (c == null) return;

        AffineTransform old = g2.getTransform();
        
        // Translasi tepat ke Pusat Layar 1920x1080 -> X: 960, Y: 480
        g2.translate(960, 480);
        g2.scale(scale, scale);

        Color rarityColor = SpriteRenderer.getRarityColor(c.getRarity());
        
        // Ukuran Kartu Disesuaikan untuk Resolusi 1080p
        int cardW = 360, cardH = 480;
        int cardX = -cardW / 2, cardY = -cardH / 2;

        // Animated Shockwave Ring saat Kartu Terbuka
        if (revealTick < 20) {
            int ringR = 120 + revealTick * 12;
            int alpha = Math.max(0, 200 - revealTick * 10);
            g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), alpha));
            g2.setStroke(new BasicStroke(4.0f));
            g2.drawOval(-ringR, -ringR, ringR * 2, ringR * 2);
        }

        // Card Glass Frame
        CyberUIEffects.drawGlassCard(g2, cardX, cardY, cardW, cardH, rarityColor, true);

        // Header Rarity Badge
        g2.setColor(rarityColor);
        g2.setFont(new Font("SansSerif", Font.BOLD, 26));
        String badge = "★ " + c.getRarity() + " ★";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(badge, -fm.stringWidth(badge) / 2, cardY + 48);

        // Animated Hero Sprite inside Card
        SpriteRenderer.drawCharacter(g2, c, 0, cardY + 160, 2.4f);

        // Character Name
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        fm = g2.getFontMetrics();
        g2.drawString(c.getName(), -fm.stringWidth(c.getName()) / 2, cardY + 260);

        // Role & SubRole
        g2.setColor(new Color(0, 215, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        String roleText = c.getRole() + " • " + c.getSubRole();
        fm = g2.getFontMetrics();
        g2.drawString(roleText, -fm.stringWidth(roleText) / 2, cardY + 295);

        // Base Stats Detail
        g2.setColor(new Color(210, 220, 235));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.drawString("Base HP  : " + c.getMaxHp(), cardX + 45, cardY + 345);
        g2.drawString("Base ATK : " + c.getAttack(), cardX + 45, cardY + 380);
        g2.drawString("Base DEF : " + c.getDefense(), cardX + 45, cardY + 415);

        // Active / Support Squad Eligibility Tag
        g2.setColor(c.isVanguardEligible() ? new Color(255, 100, 90) : new Color(80, 220, 140));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        String squadType = c.isVanguardEligible() ? "[ACTIVE DPS SQUAD]" : "[INACTIVE AUTO-SUPPORT]";
        fm = g2.getFontMetrics();
        g2.drawString(squadType, -fm.stringWidth(squadType) / 2, cardY + 455);

        g2.setTransform(old);
    }
}