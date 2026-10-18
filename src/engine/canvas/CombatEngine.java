package engine.canvas;

import engine.GameState;
import engine.InputHandler;
import engine.canvas.combat.EnemyCombatProcessor;
import model.characters.Character;
import model.npc.Enemy;
import service.PartyManager;
import service.PlayerProfile;
import service.StageManager;
import view.component.CyberUIEffects;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CombatEngine {
    public int victoryGems = 0, victoryCredits = 0;
    public List<DamageText> damageTexts = new CopyOnWriteArrayList<>();
    public List<VFXEffect> vfxList = new CopyOnWriteArrayList<>();
    private SupportResonanceEngine resonance = new SupportResonanceEngine();

    public GameState updateCombat(PartyManager party, StageManager stage, PlayerProfile profile,
                                  InputHandler input, List<Enemy> enemies, Runnable onRespawnCreeps) {
        party.tickAllCooldowns();

        // Switch Active DPS Hero dengan tombol angka [1] - [5]
        if (input.selectedHeroSlot >= 0) {
            int oldIdx = party.getActiveIndex();
            party.switchHero(input.selectedHeroSlot);
            if (party.getActiveIndex() != oldIdx) {
                Character switched = party.getActiveHero();
                if (switched != null) {
                    vfxList.add(new VFXEffect(switched.x, switched.y, 60, new Color(0, 215, 255)));
                    damageTexts.add(new DamageText(switched.x - 20, switched.y - 35, "TAG IN: " + switched.getName(), new Color(0, 230, 255)));
                }
            }
            input.selectedHeroSlot = -1;
        }

        Character hero = party.getActiveHero();
        if (hero == null || party.isAllPartyDead()) return GameState.GAME_OVER;

        resonance.updateResonance(party, hero, vfxList, damageTexts);
        hero.vx = (input.left ? -hero.speed : 0) + (input.right ? hero.speed : 0);
        hero.vy = (input.up ? -hero.speed : 0) + (input.down ? hero.speed : 0);
        hero.updatePhysics();

        if (input.skillCast && hero.skillCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 85, Color.CYAN));
            hero.castSkill(enemies);
            damageTexts.add(new DamageText(hero.x - 20, hero.y - 30, hero.getSubRole().toUpperCase() + " SKILL!", Color.CYAN));
            CyberUIEffects.triggerShake(6, 3.5f);
            input.skillCast = false;
        }
        if (input.ultCast && hero.ultCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 165, Color.YELLOW));
            hero.castUltimate(enemies);
            damageTexts.add(new DamageText(hero.x - 30, hero.y - 45, "ULTIMATE BURST!", Color.YELLOW));
            CyberUIEffects.triggerShake(16, 8.0f);
            input.ultCast = false;
        }

        if (input.bossChallengeKey && stage.canChallengeBoss(party.getActiveParty()) && !stage.isBossActive()) {
            enemies.clear();
            enemies.add(new Enemy(475, 200, true, stage.getCurrentStage()));
            stage.startBossBattle();
            vfxList.add(new VFXEffect(475, 200, 160, Color.RED));
            CyberUIEffects.triggerShake(18, 9.0f);
            input.bossChallengeKey = false;
        }

        GameState result = EnemyCombatProcessor.processEnemies(this, hero, stage, profile, enemies);
        if (result != GameState.BATTLE) return result;

        damageTexts.removeIf(dt -> { dt.update(); return dt.isExpired(); });
        vfxList.removeIf(vfx -> { vfx.update(); return vfx.isExpired(); });
        if (enemies.isEmpty() && !stage.isBossActive()) onRespawnCreeps.run();
        return GameState.BATTLE;
    }
}