package engine.canvas;

import engine.GameState;
import engine.InputHandler;
import model.Character;
import model.Enemy;
import service.PartyManager;
import service.PlayerProfile;
import service.StageManager;

import java.util.Iterator;
import java.util.List;

public class CombatEngine {
    public int victoryGems = 0;
    public int victoryCredits = 0;

    public GameState updateCombat(PartyManager party, StageManager stage, PlayerProfile profile, 
                                  InputHandler input, List<Enemy> enemies, Runnable onRespawnCreeps) {
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

        if (input.selectedHeroSlot != -1) {
            party.switchHero(input.selectedHeroSlot);
            input.selectedHeroSlot = -1;
        }

        hero.vx = (input.left ? -hero.speed : 0) + (input.right ? hero.speed : 0);
        hero.vy = (input.up ? -hero.speed : 0) + (input.down ? hero.speed : 0);
        hero.updatePhysics();

        if (input.skillCast) { hero.castSkill(enemies); input.skillCast = false; }
        if (input.ultCast)   { hero.castUltimate(enemies); input.ultCast = false; }

        if (input.bossChallengeKey && stage.canChallengeBoss(party.getActiveParty()) && !stage.isBossActive()) {
            enemies.clear();
            enemies.add(new Enemy(475, 200, true, stage.getCurrentStage()));
            stage.startBossBattle();
            input.bossChallengeKey = false;
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy en = it.next();
            en.updateAI(hero.x, hero.y);
            if (Math.hypot(en.x - hero.x, en.y - hero.y) < (en.size / 2.0f + hero.size / 2.0f)) {
                hero.takeDamage(en.attack);
            }
            if (en.currentHp <= 0) {
                hero.gainExp(en.expReward);
                int g = en.isBoss ? 300 : 20, c = en.isBoss ? 1500 : 120;
                profile.addGems(g);
                profile.addCredits(c);

                if (en.isBoss) {
                    victoryGems = g; victoryCredits = c;
                    stage.onBossDefeated();
                    it.remove();
                    return GameState.VICTORY;
                }
                stage.onCreepKilled();
                it.remove();
            }
        }

        if (enemies.isEmpty() && !stage.isBossActive()) onRespawnCreeps.run();
        return GameState.BATTLE;
    }
}