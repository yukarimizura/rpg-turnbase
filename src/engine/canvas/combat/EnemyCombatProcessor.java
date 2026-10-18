package engine.canvas.combat;

import engine.GameState;
import engine.canvas.CombatEngine;
import model.characters.Character;
import model.npc.Enemy;
import service.PlayerProfile;
import service.StageManager;
import view.component.CyberUIEffects;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Color;
import java.util.List;

public class EnemyCombatProcessor {

    public static void spawnCreepWave(List<Enemy> enemies, int stageLevel) {
        enemies.clear();
        for (int i = 0; i < 6; i++) {
            enemies.add(new Enemy((float) (Math.random() * 800 + 70),
                    (float) (Math.random() * 350 + 130), false, stageLevel));
        }
    }

    public static GameState processEnemies(CombatEngine combat, Character hero, StageManager stage,
                                           PlayerProfile profile, List<Enemy> enemies) {
        for (Enemy en : enemies) {
            en.updateAI(hero.x, hero.y);

            if (en.isBoss && en.burstReady) {
                en.burstReady = false;
                combat.vfxList.add(new VFXEffect(en.x, en.y, en.telegraphRadius, Color.RED));
                CyberUIEffects.triggerShake(14, 7.5f);
                if (Math.hypot(en.x - hero.x, en.y - hero.y) <= en.telegraphRadius) {
                    hero.takeDamage(en.attack * 2);
                    combat.damageTexts.add(new DamageText(hero.x, hero.y - 25, "BURST HIT! -" + (en.attack * 2), Color.MAGENTA));
                }
            }

            if (Math.hypot(en.x - hero.x, en.y - hero.y) < (en.size / 2.0f + hero.size / 2.0f)) {
                if (hero.iFrameTimer <= 0) {
                    combat.damageTexts.add(new DamageText(hero.x, hero.y - 15, "-" + Math.max(5, en.attack - (hero.getDefense() / 3)), Color.RED));
                    CyberUIEffects.triggerShake(5, 3.0f);
                }
                hero.takeDamage(en.attack);
            }

            if (en.currentHp <= 0) {
                combat.vfxList.add(new VFXEffect(en.x, en.y, en.isBoss ? 140 : 45, Color.ORANGE));
                combat.damageTexts.add(new DamageText(en.x, en.y - 10, "ELIMINATED! +" + en.expReward + " EXP", Color.ORANGE));
                hero.gainExp(en.expReward);
                int g = en.isBoss ? 300 : 20, c = en.isBoss ? 1500 : 120;
                profile.addGems(g);
                profile.addCredits(c);
                if (en.isBoss) {
                    combat.victoryGems = g;
                    combat.victoryCredits = c;
                    stage.onBossDefeated();
                    enemies.remove(en);
                    return GameState.VICTORY;
                }
                stage.onCreepKilled();
                enemies.remove(en);
            }
        }
        return GameState.BATTLE;
    }
}
