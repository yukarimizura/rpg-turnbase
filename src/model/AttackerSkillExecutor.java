package model;

import java.util.List;

public class AttackerSkillExecutor {
    public static void executeSkill(Attacker hero, List<Enemy> enemies) {
        int atk = hero.getAttack();
        switch (hero.getSubRole()) {
            case "Blade Dancer":
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - hero.x, en.y - hero.y) < 140) en.currentHp -= (int)(atk * 1.6f);
                }
                break;
            case "Ranger":
                Enemy target = null;
                float minD = Float.MAX_VALUE;
                for (Enemy en : enemies) {
                    float d = (float) Math.hypot(en.x - hero.x, en.y - hero.y);
                    if (d < minD) { minD = d; target = en; }
                }
                if (target != null) target.currentHp -= (int)(atk * 3.2f);
                break;
            case "Berserker":
                float multi = 1.5f + (1.0f - (float) hero.getCurrentHp() / hero.getMaxHp()) * 2.0f;
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - hero.x, en.y - hero.y) < 100) en.currentHp -= (int)(atk * multi);
                }
                break;
            case "Spellcaster":
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - hero.x, en.y - hero.y) < 220) en.currentHp -= (int)(atk * 1.8f);
                }
                break;
            case "Artillery":
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - hero.x, en.y - hero.y) < 180) {
                        en.x += (en.x - hero.x) * 1.2f;
                        en.y += (en.y - hero.y) * 1.2f;
                        en.currentHp -= (int)(atk * 2.0f);
                    }
                }
                break;
            default:
                for (Enemy en : enemies) {
                    if (Math.hypot(en.x - hero.x, en.y - hero.y) < 120) en.currentHp -= atk;
                }
                break;
        }
    }
}