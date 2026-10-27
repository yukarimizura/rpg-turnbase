package view.screen;

import engine.GameCanvas;
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

        int width = GameCanvas.BASE_WIDTH;   // 1920
        int height = GameCanvas.BASE_HEIGHT; // 1080
        int centerX = width / 2;             // 960

        // 1. Top Header Bar (Lebar: 1800px, X: 60 s/d 1860)
        CyberUIEffects.drawGlassCard(g2, 60, 20, 1800, 85, new Color(142, 68, 173, 180), false);

        // Teks Judul & Cyber Gems (Kiri Header)
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.drawString("SUMMON PORTAL: STELLAR CHRONO", 90, 58);

        g2.setColor(new Color(0, 215, 255));
        g2.setFont(new Font("SansSerif", Font.BOLD, 17));
        g2.drawString("Cyber Gems: " + profile.getCyberGems(), 90, 86);

        // SSR Pity Progress Bar (Diposisikan di Tengah-Kanan: X: 980 s/d 1360)
        int pity = gacha.getPityCounter();
        g2.setColor(new Color(255, 204, 0));
        g2.setFont(new Font("SansSerif", Font.BOLD, 17));
        g2.drawString("SSR Pity: " + pity + " / 50", 980, 70);

        int barWidth = 220;
        int barX = 1110; // Berakhir di X = 1330
        g2.setColor(new Color(35, 40, 58));
        g2.fillRoundRect(barX, 55, barWidth, 18, 8, 8);
        g2.setColor(new Color(255, 204, 0));
        g2.fillRoundRect(barX, 55, (int) (barWidth * (pity / 50.0f)), 18, 8, 8);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(barX, 55, barWidth, 18, 8, 8);

        // Render Tombol BACK TO HUB di Kanan Header (Area X > 1400)
        if (btnBack != null) {
            btnBack.draw(g2);
        }

        // 2. Batch & Portal Logic Render
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

        // 3. Dimensional Flash Overlay (Full 1920x1080 Screen)
        if (!batch.isEmpty() && summonTick >= PORTAL_CHARGE_TICKS - 3 && summonTick <= PORTAL_CHARGE_TICKS + 8) {
            float flashRatio = 1.0f - Math.abs(summonTick - PORTAL_CHARGE_TICKS) / 9.0f;
            int flashAlpha = Math.max(0, Math.min(210, (int) (flashRatio * 200)));
            g2.setColor(new Color(highestColor.getRed(), highestColor.getGreen(), highestColor.getBlue(), flashAlpha));
            g2.fillRect(0, 0, width, height);
        }

        // 4. Notice Message (Centered above buttons)
        if (!notice.isEmpty()) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(new Color(255, 230, 90));
            g2.drawString(notice, centerX - fm.stringWidth(notice) / 2, 880);
        }

        // 5. Draw Action Buttons
        btn1x.draw(g2);
        btn10x.draw(g2);
    }
}