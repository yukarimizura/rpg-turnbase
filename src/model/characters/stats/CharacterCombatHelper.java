package model.characters.stats;

import engine.GameCanvas;
import model.characters.Character;

public class CharacterCombatHelper {

    public static void tickTimers(Character c) {
        if (c.skillCdCurrent > 0) c.skillCdCurrent--;
        if (c.ultCdCurrent > 0) c.ultCdCurrent--;
        if (c.iFrameTimer > 0) c.iFrameTimer--;

        if (c.bonusDefTimer > 0) {
            c.bonusDefTimer--;
            if (c.bonusDefTimer == 0) c.bonusDefAmount = 0;
        }
        if (c.bonusAtkTimer > 0) {
            c.bonusAtkTimer--;
            if (c.bonusAtkTimer == 0) c.bonusAtkAmount = 0;
        }
    }

    public static void clampArenaBounds(Character c) {
        c.x += c.vx;
        c.y += c.vy;

        // Buka pembatas batas dari 880x540 lama ke 1920x1080 Full HD
        int minX = 40;
        int maxX = GameCanvas.BASE_WIDTH - 40;   // 1880
        int minY = 120;                          // Menghindari HUD Atas
        int maxY = GameCanvas.BASE_HEIGHT - 220; // Menghindari HUD Bawah (860)

        c.x = Math.max(minX, Math.min(maxX, c.x));
        c.y = Math.max(minY, Math.min(maxY, c.y));
    }

    public static int calculateMitigatedDamage(int rawDamage, int totalDef) {
        return Math.max(5, rawDamage - (totalDef / 3));
    }
}