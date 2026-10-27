package view.component.cyber;

import engine.GameCanvas;
import engine.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class CyberTransitionEffect {
    // Screen Transition & Smooth Loading State
    private static boolean transitioning = false;
    private static GameState pendingState = null;
    private static int transitionTick = 0;
    private static final int TRANSITION_MAX = 26; // ~0.43s smooth cyber loading transition
    private static String loadingLabel = "SYNCHRONIZING CHRONO LINK...";

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

        int width = GameCanvas.BASE_WIDTH;   // 1920
        int height = GameCanvas.BASE_HEIGHT; // 1080
        int centerX = width / 2;             // 960
        int centerY = height / 2;            // 540

        float half = TRANSITION_MAX / 2.0f;
        float progress = (float) transitionTick / TRANSITION_MAX;
        float alphaFactor = (transitionTick <= half)
                ? (transitionTick / half)
                : (1.0f - (transitionTick - half) / half);
        alphaFactor = Math.max(0f, Math.min(1f, alphaFactor));

        // 1. Overlay Dark Fullscreen Fade (1920x1080)
        int bgAlpha = (int) (alphaFactor * 245);
        g2.setColor(new Color(8, 11, 20, bgAlpha));
        g2.fillRect(0, 0, width, height);

        if (alphaFactor > 0.25f) {
            int uiAlpha = (int) (Math.min(1f, (alphaFactor - 0.2f) * 1.3f) * 255);
            long tick = System.currentTimeMillis() / 16;

            // 2. Rotating Cyber Spinner (Pusat Layar X: 960, Y: 500)
            AffineTransform old = g2.getTransform();
            Stroke oldStroke = g2.getStroke();
            
            g2.translate(centerX, centerY - 40);
            
            // Outer Ring Spinner
            g2.rotate(tick * 0.14);
            g2.setColor(new Color(0, 215, 255, uiAlpha));
            g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(-50, -50, 100, 100, 0, 120);
            g2.drawArc(-50, -50, 100, 100, 180, 120);

            // Inner Ring Spinner
            g2.rotate(-tick * 0.26);
            g2.setColor(new Color(255, 205, 0, uiAlpha));
            g2.setStroke(new BasicStroke(3.0f));
            g2.drawArc(-32, -32, 64, 64, 45, 90);
            g2.drawArc(-32, -32, 64, 64, 225, 90);
            
            g2.setTransform(old);
            g2.setStroke(oldStroke);

            // 3. Loading Label Text (Centered)
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(new Color(220, 240, 255, uiAlpha));
            g2.drawString(loadingLabel, centerX - fm.stringWidth(loadingLabel) / 2, centerY + 50);

            // 4. Progress Bar Accent Line
            int barW = 480;
            int barX = centerX - barW / 2;
            int barY = centerY + 80;
            
            g2.setColor(new Color(30, 40, 60, uiAlpha));
            g2.fillRoundRect(barX, barY, barW, 12, 8, 8);
            
            g2.setColor(new Color(0, 215, 255, uiAlpha));
            g2.fillRoundRect(barX, barY, (int) (barW * progress), 12, 8, 8);
        }
    }
}