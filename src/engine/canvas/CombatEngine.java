package engine.canvas;

import engine.GameCanvas;
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
        
        // Input Movement WASD
        hero.vx = (input.left ? -hero.speed : 0) + (input.right ? hero.speed : 0);
        hero.vy = (input.up ? -hero.speed : 0) + (input.down ? hero.speed : 0);
        hero.updatePhysics();

        // Clamping posisi player di arena 1920x1080 (menghindari HUD Atas & Bawah)
        hero.x = Math.max(80, Math.min(GameCanvas.BASE_WIDTH - 80, hero.x));
        hero.y = Math.max(220, Math.min(GameCanvas.BASE_HEIGHT - 220, hero.y));

        // Cast Skill [Q]
        if (input.skillCast && hero.skillCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 85, Color.CYAN));
            hero.castSkill(enemies);
            damageTexts.add(new DamageText(hero.x - 20, hero.y - 30, hero.getSubRole().toUpperCase() + " SKILL!", Color.CYAN));
            CyberUIEffects.triggerShake(6, 3.5f);
            input.skillCast = false;
        }
        
        // Cast Ultimate [R]
        if (input.ultCast && hero.ultCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 165, Color.YELLOW));
            hero.castUltimate(enemies);
            damageTexts.add(new DamageText(hero.x - 30, hero.y - 45, "ULTIMATE BURST!", Color.YELLOW));
            CyberUIEffects.triggerShake(16, 8.0f);
            input.ultCast = false;
        }

        // Panggil Boss Stage dengan tombol [B] (Spawn di Titik Pusat Arena 960, 480)
        if (input.bossChallengeKey && stage.canChallengeBoss(party.getActiveParty()) && !stage.isBossActive()) {
            enemies.clear();
            float bossSpawnX = GameCanvas.BASE_WIDTH / 2.0f;  // 960
            float bossSpawnY = GameCanvas.BASE_HEIGHT / 2.0f - 60; // 480 (Sedikit di atas pusat)
            
            enemies.add(new Enemy(bossSpawnX, bossSpawnY, true, stage.getCurrentStage()));
            stage.startBossBattle();
            vfxList.add(new VFXEffect(bossSpawnX, bossSpawnY, 220, Color.RED));
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