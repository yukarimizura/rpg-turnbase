package engine.canvas;

import engine.GameState;
import engine.InputHandler;
import model.Character;
import model.Defender;
import model.Enemy;
import model.Healer;
import model.Support;
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

    // Timers untuk resonance auto-trigger off-field (60 ticks = 1 detik)
    private int defenderResonanceTimer = 0;
    private int healerResonanceTimer = 0;
    private int supportResonanceTimer = 0;

    public GameState updateCombat(PartyManager party, StageManager stage, PlayerProfile profile, 
                                  InputHandler input, List<Enemy> enemies, Runnable onRespawnCreeps) {
        
        // 1. Cooldown global tick untuk seluruh tim
        party.tickAllCooldowns();

        Character hero = party.getActiveHero();
        if (hero == null || !hero.isAlive()) {
            return GameState.GAME_OVER;
        }

        // 2. Off-field Resonance Auto-Triggers
        updateOffFieldResonance(party, hero);

        // 3. Gerakan WASD Vanguard
        hero.vx = (input.left ? -hero.speed : 0) + (input.right ? hero.speed : 0);
        hero.vy = (input.up ? -hero.speed : 0) + (input.down ? hero.speed : 0);
        hero.updatePhysics();

        // 4. Skill Q & R Vanguard
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

        // 5. Boss Spawn (Tombol B)
        if (input.bossChallengeKey && stage.canChallengeBoss(party.getActiveParty()) && !stage.isBossActive()) {
            enemies.clear();
            enemies.add(new Enemy(475, 200, true, stage.getCurrentStage()));
            stage.startBossBattle();
            vfxList.add(new VFXEffect(475, 200, 150, Color.RED));
            input.bossChallengeKey = false;
        }

        // 6. Loop tabrakan & damage musuh
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

        // 7. Bersihkan visual yang sudah expired
        damageTexts.removeIf(dt -> { dt.update(); return dt.isExpired(); });
        vfxList.removeIf(vfx -> { vfx.update(); return vfx.isExpired(); });

        if (enemies.isEmpty() && !stage.isBossActive()) onRespawnCreeps.run();
        return GameState.BATTLE;
    }

    private void updateOffFieldResonance(PartyManager party, Character vanguard) {
        List<Character> squad = party.getActiveParty();
        if (squad.size() <= 1) return;

        defenderResonanceTimer++;
        healerResonanceTimer++;
        supportResonanceTimer++;

        for (int i = 1; i < squad.size(); i++) {
            Character supportHero = squad.get(i);
            if (supportHero == null || !supportHero.isAlive()) continue;

            // 1. Defender Auto-Shield (Tiap 7 detik / 420 ticks)
            if (supportHero instanceof Defender && defenderResonanceTimer >= 420) {
                int shieldBonus = (int) (supportHero.getDefense() * 0.75f);
                vanguard.applyDefBuff(shieldBonus, 240); // Durasi 4 detik
                vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 45, new Color(52, 152, 219)));
                damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "DEF SHIELD +" + shieldBonus, new Color(52, 152, 219)));
                defenderResonanceTimer = 0;
            }

            // 2. Healer Auto-Pulse (Tiap 5 detik / 300 ticks, saat HP Vanguard < 80%)
            if (supportHero instanceof Healer && healerResonanceTimer >= 300) {
                if (vanguard.getCurrentHp() < (int)(vanguard.getMaxHp() * 0.8f)) {
                    int healAmount = (int) (supportHero.getMaxHp() * 0.25f);
                    vanguard.heal(healAmount);
                    vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 50, new Color(46, 204, 113)));
                    damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "REGEN +" + healAmount, new Color(46, 204, 113)));
                    healerResonanceTimer = 0;
                }
            }

            // 3. Support ATK War Cry (Tiap 9 detik / 540 ticks)
            if (supportHero instanceof Support && supportResonanceTimer >= 540) {
                int atkBonus = (int) (supportHero.getAttack() * 0.5f);
                vanguard.applyAtkBuff(atkBonus, 240); // Durasi 4 detik
                vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 55, new Color(241, 196, 15)));
                damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "ATK BOOST +" + atkBonus, new Color(241, 196, 15)));
                supportResonanceTimer = 0;
            }
        }
    }
}