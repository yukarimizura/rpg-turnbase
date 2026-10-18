package model.characters.stats;

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
        c.x = Math.max(20, Math.min(880, c.x));
        c.y = Math.max(20, Math.min(540, c.y));
    }

    public static int calculateMitigatedDamage(int rawDamage, int totalDef) {
        return Math.max(5, rawDamage - (totalDef / 3));
    }
}
