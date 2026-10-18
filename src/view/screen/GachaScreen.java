package view.screen;

import model.characters.Character;
import service.GachaService;
import service.PlayerProfile;
import view.component.CyberUIEffects;
import view.component.UIButton;
import view.screen.gacha.GachaPortalRenderer;
import view.screen.gacha.GachaSingleCardRenderer;
import view.screen.gacha.GachaTenPullGridRenderer;

import java.awt.*;
import java.util.List;

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
        Color highestColor = GachaPortalRenderer.getHighestRarityColor(batch);

        // Phase 1: Portal Charging / Dimensional Rift Animation
        if (!batch.isEmpty() && summonTick < PORTAL_CHARGE_TICKS) {
            GachaPortalRenderer.drawPortalChargeAnimation(g2, summonTick, PORTAL_CHARGE_TICKS, highestColor);
        } else if (batch.isEmpty()) {
            // Idle Portal Preview when no summon yet
            GachaPortalRenderer.drawIdlePortal(g2);
        } else if (batch.size() == 1) {
            // Phase 2A: Single Pull Large Holographic Card Reveal
            int revealTick = summonTick - PORTAL_CHARGE_TICKS;
            float scale = Math.min(1.0f, 0.25f + revealTick * 0.08f);
            GachaSingleCardRenderer.drawSinglePullCard(g2, batch.get(0), scale, revealTick);
        } else {
            // Phase 2B: 10x Pull Full 5x2 Grid Staggered Card Reveal
            int revealTick = summonTick - PORTAL_CHARGE_TICKS;
            GachaTenPullGridRenderer.drawTenPullGrid(g2, batch, revealTick);
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
}