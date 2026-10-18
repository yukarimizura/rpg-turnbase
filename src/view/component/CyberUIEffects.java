package view.component;

import engine.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class CyberUIEffects {
    public static boolean vfxEnabled = true;

    // Screen Transition & Smooth Loading State
    private static boolean transitioning = false;
    private static GameState pendingState = null;
    private static int transitionTick = 0;
    private static final int TRANSITION_MAX = 26; // ~0.43s smooth cyber loading transition
    private static String loadingLabel = "SYNCHRONIZING CHRONO LINK...";

    // Screen Shake State
    private static int shakeTimer = 0;
    private static float shakeIntensity = 0f;

    public static void triggerShake(int ticks, float intensity) {
        if (!vfxEnabled) return;
        shakeTimer = Math.max(shakeTimer, ticks);
        shakeIntensity = Math.max(shakeIntensity, intensity);
    }

    public static int getShakeOffsetX() {
        if (shakeTimer <= 0 || !vfxEnabled) return 0;
        return (int) ((Math.random() - 0.5) * 2 * shakeIntensity);
    }

    public static int getShakeOffsetY() {
        if (shakeTimer <= 0 || !vfxEnabled) return 0;
        return (int) ((Math.random() - 0.5) * 2 * shakeIntensity);
    }

    public static void startTransition(GameState target, String label) {
        if (transitioning) return;
        pendingState = target;
        loadingLabel = (label != null && !label.isEmpty()) ? label : "LOADING MODULE...";
        transitionTick = 0;
        transitioning = true;
    }

    public static boolean isTransitioning() {
        return transitioning;
    }

    public static GameState updateTransition(GameState currentState) {
        if (shakeTimer > 0) {
            shakeTimer--;
            shakeIntensity *= 0.9f;
        }
        if (!transitioning) return currentState;

        transitionTick++;
        GameState out = currentState;
        if (transitionTick == TRANSITION_MAX / 2 && pendingState != null) {
            out = pendingState;
        }
        if (transitionTick >= TRANSITION_MAX) {
            transitioning = false;
            pendingState = null;
        }
        return out;
    }

    public static void drawTransitionOverlay(Graphics2D g2) {
        if (!transitioning) return;

        float half = TRANSITION_MAX / 2.0f;
        float progress = (float) transitionTick / TRANSITION_MAX;
        float alphaFactor = (transitionTick <= half)
                ? (transitionTick / half)
                : (1.0f - (transitionTick - half) / half);
        alphaFactor = Math.max(0f, Math.min(1f, alphaFactor));

        int bgAlpha = (int) (alphaFactor * 245);
        g2.setColor(new Color(8, 11, 20, bgAlpha));
        g2.fillRect(0, 0, 950, 600);

        if (alphaFactor > 0.25f) {
            int uiAlpha = (int) (Math.min(1f, (alphaFactor - 0.2f) * 1.3f) * 255);
            long tick = System.currentTimeMillis() / 16;

            // Rotating Cyber Spinner
            AffineTransform old = g2.getTransform();
            Stroke oldStroke = g2.getStroke();
            g2.translate(475, 265);
            g2.rotate(tick * 0.14);
            g2.setColor(new Color(0, 215, 255, uiAlpha));
            g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(-28, -28, 56, 56, 0, 120);
            g2.drawArc(-28, -28, 56, 56, 180, 120);

            g2.rotate(-tick * 0.26);
            g2.setColor(new Color(255, 205, 0, uiAlpha));
            g2.setStroke(new BasicStroke(2.2f));
            g2.drawArc(-18, -18, 36, 36, 45, 90);
            g2.drawArc(-18, -18, 36, 36, 225, 90);
            g2.setTransform(old);
            g2.setStroke(oldStroke);

            // Loading Label
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(new Color(220, 240, 255, uiAlpha));
            g2.drawString(loadingLabel, 475 - fm.stringWidth(loadingLabel) / 2, 322);

            // Progress Bar
            int barW = 240;
            int barX = 475 - barW / 2;
            int barY = 335;
            g2.setColor(new Color(30, 40, 60, uiAlpha));
            g2.fillRoundRect(barX, barY, barW, 8, 6, 6);
            g2.setColor(new Color(0, 215, 255, uiAlpha));
            g2.fillRoundRect(barX, barY, (int) (barW * progress), 8, 6, 6);
        }
    }

    public static void drawCyberBackground(Graphics2D g2, boolean isBattleArena) {
        long tick = System.currentTimeMillis() / 20;

        if (isBattleArena) {
            GradientPaint gp = new GradientPaint(0, 0, new Color(14, 18, 28), 0, 600, new Color(22, 16, 34));
            g2.setPaint(gp);
            g2.fillRect(0, 0, 950, 600);

            // Tactical Arena Floor Grid
            g2.setColor(new Color(0, 210, 255, 18));
            g2.setStroke(new BasicStroke(1.0f));
            for (int x = 0; x < 950; x += 50) {
                g2.drawLine(x, 0, x, 600);
            }
            for (int y = 0; y < 600; y += 50) {
                g2.drawLine(0, y, 950, y);
            }

            // Center Arena Tactical Ring
            g2.setColor(new Color(0, 210, 255, 28));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(475 - 150, 300 - 150, 300, 300);
            g2.drawOval(475 - 80, 300 - 80, 160, 160);
        } else {
            GradientPaint gp = new GradientPaint(0, 0, new Color(11, 14, 24), 0, 600, new Color(26, 15, 42));
            g2.setPaint(gp);
            g2.fillRect(0, 0, 950, 600);

            // Moving Cyber Grid
            int offset = (int) (tick % 45);
            g2.setColor(new Color(0, 195, 255, 20));
            g2.setStroke(new BasicStroke(1.0f));
            for (int x = -45 + offset; x < 950; x += 45) {
                g2.drawLine(x, 0, x, 600);
            }
            for (int y = -45 + offset; y < 600; y += 45) {
                g2.drawLine(0, y, 950, y);
            }
        }

        // Floating Ambient Cyber Particles
        if (vfxEnabled) {
            for (int i = 0; i < 18; i++) {
                float px = ((i * 137 + tick * (0.4f + (i % 3) * 0.2f)) % 950);
                float py = 600 - ((i * 97 + tick * (0.6f + (i % 4) * 0.25f)) % 600);
                int alpha = 40 + (int) (Math.sin(tick * 0.08 + i) * 30);
                int size = 2 + (i % 3);
                g2.setColor(i % 2 == 0 ? new Color(0, 215, 255, alpha) : new Color(185, 95, 255, alpha));
                g2.fillOval((int) px, (int) py, size, size);
            }
        }
    }

    public static void drawGlassCard(Graphics2D g2, int x, int y, int w, int h, Color borderColor, boolean highlighted) {
        Stroke oldStroke = g2.getStroke();
        g2.setColor(new Color(20, 25, 38, 215));
        g2.fillRoundRect(x, y, w, h, 12, 12);

        if (highlighted) {
            g2.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 45));
            g2.fillRoundRect(x - 3, y - 3, w + 6, h + 6, 14, 14);
        }

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(highlighted ? 2.2f : 1.2f));
        g2.drawRoundRect(x, y, w, h, 12, 12);

        // Top highlight bar
        g2.setColor(new Color(255, 255, 255, 25));
        g2.fillRoundRect(x + 4, y + 3, w - 8, 5, 4, 4);
        g2.setStroke(oldStroke);
    }
}
