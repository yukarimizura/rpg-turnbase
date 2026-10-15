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
    public int victoryGems = 0;
    public int victoryCredits = 0;

    // Visual lists
    public List<DamageText> damageTexts = new ArrayList<>();
    public List<VFXEffect> vfxList = new ArrayList<>();

    public GameState updateCombat(PartyManager party, StageManager stage, PlayerProfile profile, 
                                  InputHandler input, List<Enemy> enemies, Runnable onRespawnCreeps) {
        
        // 1. Cooldown berjalan terus untuk SEMUA hero di party (aktif maupun cadangan)
        party.tickAllCooldowns();

        Character hero = party.getActiveHero();
        if (hero == null || !hero.isAlive()) {
            boolean replaced = false;
            for (int i = 0; i < party.getActiveParty().size(); i++) {
                if (party.getActiveParty().get(i).isAlive()) {
                    party.switchHero(i);
                    hero = party.getActiveHero();
                    replaced = true;
                    break;
                }
            }
            if (!replaced || party.isAllPartyDead()) return GameState.GAME_OVER;
        }

        // 2. Ganti hero via angka 1 - 5
        if (input.selectedHeroSlot != -1) {
            party.switchHero(input.selectedHeroSlot);
            input.selectedHeroSlot = -1;
            hero = party.getActiveHero();
        }

        // 3. Gerakan WASD
        hero.vx = (input.left ? -hero.speed : 0) + (input.right ? hero.speed : 0);
        hero.vy = (input.up ? -hero.speed : 0) + (input.down ? hero.speed : 0);
        hero.updatePhysics();

        // 4. Skill Q & R dengan pemicu VFX
        if (input.skillCast && hero.skillCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 60, Color.CYAN));
            hero.castSkill(enemies);
            input.skillCast = false;
        }
        if (input.ultCast && hero.ultCdCurrent == 0) {
            vfxList.add(new VFXEffect(hero.x, hero.y, 120, Color.YELLOW));
            hero.castUltimate(enemies);
            input.ultCast = false;
        }

        // 5. Spawn Boss (Tombol B)
        if (input.bossChallengeKey && stage.canChallengeBoss(party.getActiveParty()) && !stage.isBossActive()) {
            enemies.clear();
            enemies.add(new Enemy(475, 200, true, stage.getCurrentStage()));
            stage.startBossBattle();
            vfxList.add(new VFXEffect(475, 200, 150, Color.RED));
            input.bossChallengeKey = false;
        }

        // 6. Loop musuh, serangan, dan tabrakan
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy en = it.next();
            en.updateAI(hero.x, hero.y);

            // Eksekusi Burst AoE jika telegraph charge boss selesai
            if (en.isBoss && en.burstReady) {
                en.burstReady = false;
                vfxList.add(new VFXEffect(en.x, en.y, en.telegraphRadius, Color.RED));
                
                float distHero = (float) Math.hypot(en.x - hero.x, en.y - hero.y);
                if (distHero <= en.telegraphRadius) {
                    int burstDmg = en.attack * 2;
                    hero.takeDamage(burstDmg);
                    damageTexts.add(new DamageText(hero.x, hero.y - 25, "BURST HIT! -" + burstDmg, Color.MAGENTA));
                }
            }

            // Tabrakan kontak biasa ke hero
            if (Math.hypot(en.x - hero.x, en.y - hero.y) < (en.size / 2.0f + hero.size / 2.0f)) {
                if (hero.iFrameTimer <= 0) {
                    int takenDmg = Math.max(5, en.attack - (hero.getDefense() / 3));
                    damageTexts.add(new DamageText(hero.x, hero.y - 15, "-" + takenDmg, Color.RED));
                }
                hero.takeDamage(en.attack);
            }

            // Musuh mati
            if (en.currentHp <= 0) {
                damageTexts.add(new DamageText(en.x, en.y - 10, "CRIT! " + (hero.getAttack() * 2), Color.ORANGE));
                hero.gainExp(en.expReward);
                int g = en.isBoss ? 300 : 20;
                int c = en.isBoss ? 1500 : 120;
                profile.addGems(g);
                profile.addCredits(c);

                if (en.isBoss) {
                    victoryGems = g;
                    victoryCredits = c;
                    stage.onBossDefeated();
                    it.remove();
                    return GameState.VICTORY;
                }
                stage.onCreepKilled();
                it.remove();
            }
        }

        // 7. Update status animasi Damage Text dan VFX
        damageTexts.removeIf(dt -> { dt.update(); return dt.isExpired(); });
        vfxList.removeIf(vfx -> { vfx.update(); return vfx.isExpired(); });

        if (enemies.isEmpty() && !stage.isBossActive()) onRespawnCreeps.run();
        return GameState.BATTLE;
    }
}