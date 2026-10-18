package view.component;

import engine.GameState;
import view.component.cyber.CyberBackgroundRenderer;
import view.component.cyber.CyberTransitionEffect;

import java.awt.*;

public class CyberUIEffects {
    public static boolean vfxEnabled = true;

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
        CyberTransitionEffect.startTransition(target, label);
    }

    public static boolean isTransitioning() {
        return CyberTransitionEffect.isTransitioning();
    }

    public static GameState updateTransition(GameState currentState) {
        if (shakeTimer > 0) {
            shakeTimer--;
            shakeIntensity *= 0.9f;
        }
        return CyberTransitionEffect.updateTransition(currentState);
    }

    public static void drawTransitionOverlay(Graphics2D g2) {
        CyberTransitionEffect.drawTransitionOverlay(g2);
    }

    public static void drawCyberBackground(Graphics2D g2, boolean isBattleArena) {
        CyberBackgroundRenderer.drawCyberBackground(g2, isBattleArena, vfxEnabled);
    }

    public static void drawGlassCard(Graphics2D g2, int x, int y, int w, int h, Color borderColor, boolean highlighted) {
        CyberBackgroundRenderer.drawGlassCard(g2, x, y, w, h, borderColor, highlighted);
    }
}
