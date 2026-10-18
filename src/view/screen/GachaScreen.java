package view.screen;

import service.GachaService;
import service.PlayerProfile;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;
import view.component.UIButton;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

import model.characters.Character;

public class GachaScreen {
    private static int summonTick = 999;
    private static final int PORTAL_CHARGE_TICKS = 30;

    public static void triggerSummonAnimation() {
        summonTick = 0;
    }

    public static void render(Graphics2D g2, PlayerProfile profile, GachaService gacha, String notice,
                             UIButton btnBack, UIButton btn1x, UIButton btn10x) {
        CyberUIEffects.drawCyberBackground(g2, false);
        if (summonTick < 999) summonTick++;

        // Top Header Bar
        CyberUIEffects.drawGlassCard(g2, 20, 15, 740, 68, new Color(142, 68, 173, 180), false);
        btnBack.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("SUMMON PORTAL: STELLAR CHRONO", 40, 44);

        g2.setColor(new Color(0, 215, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Cyber Gems: " + profile.getCyberGems(), 40, 68);

        // Pity Progress Bar
        int pity = gacha.getPityCounter();
        g2.setColor(new Color(255, 204, 0));
        g2.drawString("SSR Pity: " + pity + " / 50", 250, 68);
        g2.setColor(new Color(35, 40, 58));
        g2.fillRoundRect(375, 56, 180, 12, 6, 6);
        g2.setColor(new Color(255, 204, 0));
        g2.fillRoundRect(375, 56, (int) (180 * (pity / 50.0f)), 12, 6, 6);

        List<Character> batch = gacha.getLastPulledBatch();
        Color highestColor = getHighestRarityColor(batch);

        // Phase 1: Portal Charging / Dimensional Rift Animation
        if (!batch.isEmpty() && summonTick < PORTAL_CHARGE_TICKS) {
            drawPortalChargeAnimation(g2, summonTick, PORTAL_CHARGE_TICKS, highestColor);
        } else if (batch.isEmpty()) {
            // Idle Portal Preview when no summon yet
            drawIdlePortal(g2);
        } else if (batch.size() == 1) {
            // Phase 2A: Single Pull Large Holographic Card Reveal
            int revealTick = summonTick - PORTAL_CHARGE_TICKS;
            float scale = Math.min(1.0f, 0.25f + revealTick * 0.08f);
            drawSinglePullCard(g2, batch.get(0), scale, revealTick);
        } else {
            // Phase 2B: 10x Pull Full 5x2 Grid Staggered Card Reveal
            int revealTick = summonTick - PORTAL_CHARGE_TICKS;
            drawTenPullGrid(g2, batch, revealTick);
        }

        // Flash overlay right at the moment portal opens
        if (!batch.isEmpty() && summonTick >= PORTAL_CHARGE_TICKS - 3 && summonTick <= PORTAL_CHARGE_TICKS + 8) {
            float flashRatio = 1.0f - Math.abs(summonTick - PORTAL_CHARGE_TICKS) / 9.0f;
            int flashAlpha = Math.max(0, Math.min(210, (int) (flashRatio * 200)));
            g2.setColor(new Color(highestColor.getRed(), highestColor.getGreen(), highestColor.getBlue(), flashAlpha));
            g2.fillRect(0, 0, 950, 600);
        }

        if (!notice.isEmpty()) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(new Color(255, 230, 90));
            g2.drawString(notice, 475 - fm.stringWidth(notice) / 2, 505);
        }

        btn1x.draw(g2);
        btn10x.draw(g2);
    }

    private static void drawIdlePortal(Graphics2D g2) {
        long tick = System.currentTimeMillis() / 16;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();
        g2.translate(475, 275);

        g2.rotate(tick * 0.02);
        g2.setColor(new Color(142, 68, 173, 90));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{12, 8}, 0));
        g2.drawOval(-110, -110, 220, 220);

        g2.rotate(-tick * 0.045);
        g2.setColor(new Color(0, 215, 255, 120));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawOval(-75, -75, 150, 150);

        g2.setTransform(old);
        g2.setStroke(oldStroke);

        g2.setColor(new Color(200, 220, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString("PORTAL READY - PILIH 1x ATAU 10x SUMMON", 305, 280);
    }

    private static void drawPortalChargeAnimation(Graphics2D g2, int tick, int maxTick, Color auraColor) {
        float progress = (float) tick / maxTick;
        AffineTransform old = g2.getTransform();
        Stroke oldStroke = g2.getStroke();
        g2.translate(475, 275);

        // Converging energy particles
        for (int i = 0; i < 12; i++) {
            double ang = (i * Math.PI / 6.0) + tick * 0.18;
            float dist = (1.0f - progress) * 190f + 10f;
            int px = (int) (Math.cos(ang) * dist);
            int py = (int) (Math.sin(ang) * dist);
            g2.setColor(auraColor);
            g2.fillOval(px - 4, py - 4, 8, 8);
            g2.setColor(Color.WHITE);
            g2.drawLine(px, py, (int) (px * 0.7f), (int) (py * 0.7f));
        }

        // Expanding & spinning dimensional rings
        int r1 = (int) (40 + progress * 110);
        g2.rotate(tick * 0.22);
        g2.setColor(auraColor);
        g2.setStroke(new BasicStroke(3.5f));
        g2.drawOval(-r1, -r1, r1 * 2, r1 * 2);

        g2.rotate(-tick * 0.4);
        int r2 = (int) (95 - progress * 50);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRect(-r2, -r2, r2 * 2, r2 * 2);

        g2.setTransform(old);
        g2.setStroke(oldStroke);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString("MATERIALIZING HERO DATA...", 360, 440);
    }

    private static void drawSinglePullCard(Graphics2D g2, Character c, float scale, int revealTick) {
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

    private static void drawTenPullGrid(Graphics2D g2, List<Character> batch, int revealTick) {
        int cols = 5;
        int cardW = 162, cardH = 182;
        int gapX = 16, gapY = 14;
        int startX = 40;
        int startY = 98;

        for (int i = 0; i < batch.size() && i < 10; i++) {
            int cardDelay = i * 4;
            int localTick = revealTick - cardDelay;
            if (localTick < 0) continue;

            float scale = Math.min(1.0f, 0.2f + localTick * 0.10f);
            Character c = batch.get(i);
            Color rarityColor = SpriteRenderer.getRarityColor(c.getRarity());

            int col = i % cols;
            int row = i / cols;
            int cx = startX + col * (cardW + gapX) + cardW / 2;
            int cy = startY + row * (cardH + gapY) + cardH / 2;

            AffineTransform old = g2.getTransform();
            g2.translate(cx, cy);
            g2.scale(scale, scale);

            int x = -cardW / 2;
            int y = -cardH / 2;

            boolean isHighRarity = "SSR".equals(c.getRarity()) || "SR".equals(c.getRarity());
            CyberUIEffects.drawGlassCard(g2, x, y, cardW, cardH, rarityColor, isHighRarity);

            // Pop-in shockwave ring
            if (localTick < 10) {
                int r = 35 + localTick * 6;
                g2.setColor(new Color(rarityColor.getRed(), rarityColor.getGreen(), rarityColor.getBlue(), 200 - localTick * 18));
                g2.drawOval(-r, -r, r * 2, r * 2);
            }

            // Rarity Header
            g2.setColor(rarityColor);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            String badge = "★ " + c.getRarity() + " ★";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(badge, -fm.stringWidth(badge) / 2, y + 20);

            // Mini Character Sprite
            SpriteRenderer.drawCharacter(g2, c, 0, y + 66, 1.0f);

            // Name & SubRole
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            fm = g2.getFontMetrics();
            String name = c.getName();
            g2.drawString(name, -fm.stringWidth(name) / 2, y + 108);

            g2.setColor(SpriteRenderer.getRoleAccentColor(c.getRole(), c.getSubRole()));
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            String roleStr = c.getRole() + (c.isVanguardEligible() ? " (DPS)" : " (Auto)");
            fm = g2.getFontMetrics();
            g2.drawString(roleStr, -fm.stringWidth(roleStr) / 2, y + 124);

            // Compact Stats
            g2.setColor(new Color(200, 215, 230));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            String stat1 = "HP: " + c.getMaxHp() + " | DEF: " + c.getDefense();
            String stat2 = "ATK: " + c.getAttack();
            fm = g2.getFontMetrics();
            g2.drawString(stat1, -fm.stringWidth(stat1) / 2, y + 146);
            g2.drawString(stat2, -fm.stringWidth(stat2) / 2, y + 162);

            g2.setTransform(old);
        }
    }

    private static Color getHighestRarityColor(List<Character> batch) {
        boolean hasSSR = false, hasSR = false;
        for (Character c : batch) {
            if ("SSR".equals(c.getRarity())) hasSSR = true;
            else if ("SR".equals(c.getRarity())) hasSR = true;
        }
        if (hasSSR) return new Color(255, 215, 0);
        if (hasSR) return new Color(185, 95, 255);
        return new Color(52, 162, 235);
    }
}