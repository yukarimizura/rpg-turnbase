package engine.canvas;

import engine.GameState;
import engine.InputHandler;
import model.Character;
import model.Enemy;
import service.PartyManager;
import service.PlayerProfile;
import service.StageManager;
import view.DamageText;
import view.VFXEffect;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CombatEngine {
    public int victoryGems = 0, victoryCredits = 0;
    public List<DamageText> damageTexts = new ArrayList<>();
    public List<VFXEffect> vfxList = new ArrayList<>();
    private SupportResonanceEngine resonance = new SupportResonanceEngine();

    public GameState updateCombat(PartyManager party, StageManager stage, PlayerProfile profile,
                                  InputHandler input, List<Enemy> enemies, Runnable onRespawnCreeps) {
        party.tickAllCooldowns();
        Character hero = party.getActiveHero();
        if (hero == null || !hero.isAlive()) return GameState.GAME_OVER;

        resonance.updateResonance(party, hero, vfxList, damageTexts);
        hero.vx = (input.left ? -hero.speed : 0) + (input.right ? hero.speed : 0);
        hero.vy = (input.up ? -hero.speed : 0) + (input.down ? hero.speed : 0);
        hero.updatePhysics();

        if (input.skillCast && hero.skillCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 70, Color.CYAN));
            hero.castSkill(enemies);
            damageTexts.add(new DamageText(hero.x, hero.y - 30, hero.getSubRole().toUpperCase() + " SKILL!", Color.CYAN));
            input.skillCast = false;
        }
        if (input.ultCast && hero.ultCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 140, Color.YELLOW));
            hero.castUltimate(enemies);
            damageTexts.add(new DamageText(hero.x, hero.y - 45, "ULTIMATE BURST!", Color.YELLOW));
            input.ultCast = false;
        }

        if (input.bossChallengeKey && stage.canChallengeBoss(party.getActiveParty()) && !stage.isBossActive()) {
            enemies.clear();
            enemies.add(new Enemy(475, 200, true, stage.getCurrentStage()));
            stage.startBossBattle();
            vfxList.add(new VFXEffect(475, 200, 150, Color.RED));
            input.bossChallengeKey = false;
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy en = it.next();
            en.updateAI(hero.x, hero.y);

            if (en.isBoss && en.burstReady) {
                en.burstReady = false;
                vfxList.add(new VFXEffect(en.x, en.y, en.telegraphRadius, Color.RED));
                if (Math.hypot(en.x - hero.x, en.y - hero.y) <= en.telegraphRadius) {
                    hero.takeDamage(en.attack * 2);
                    damageTexts.add(new DamageText(hero.x, hero.y - 25, "BURST HIT! -" + (en.attack * 2), Color.MAGENTA));
                }
            }

            if (Math.hypot(en.x - hero.x, en.y - hero.y) < (en.size / 2.0f + hero.size / 2.0f)) {
                if (hero.iFrameTimer <= 0) damageTexts.add(new DamageText(hero.x, hero.y - 15, "-" + Math.max(5, en.attack - (hero.getDefense() / 3)), Color.RED));
                hero.takeDamage(en.attack);
            }

            if (en.currentHp <= 0) {
                damageTexts.add(new DamageText(en.x, en.y - 10, "CRIT! " + (hero.getAttack() * 2), Color.ORANGE));
                hero.gainExp(en.expReward);
                int g = en.isBoss ? 300 : 20, c = en.isBoss ? 1500 : 120;
                profile.addGems(g); profile.addCredits(c);
                if (en.isBoss) { victoryGems = g; victoryCredits = c; stage.onBossDefeated(); it.remove(); return GameState.VICTORY; }
                stage.onCreepKilled(); it.remove();
            }
        }

        damageTexts.removeIf(dt -> { dt.update(); return dt.isExpired(); });
        vfxList.removeIf(vfx -> { vfx.update(); return vfx.isExpired(); });
        if (enemies.isEmpty() && !stage.isBossActive()) onRespawnCreeps.run();
        return GameState.BATTLE;
    }
}