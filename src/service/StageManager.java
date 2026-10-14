package service;

import java.util.List;
import model.Character;

public class StageManager {
    private int currentStage = 1;
    private int creepsDefeated = 0;
    private final int CREEPS_REQUIRED = 8;
    private boolean bossActive = false;

    public boolean canChallengeBoss(List<Character> party) {
        return party.size() >= PartyManager.MAX_PARTY_SIZE && creepsDefeated >= CREEPS_REQUIRED;
    }

    public void onCreepKilled() {
        creepsDefeated++;
    }

    public void startBossBattle() {
        this.bossActive = true;
    }

    public void onBossDefeated() {
        this.currentStage++;
        this.creepsDefeated = 0;
        this.bossActive = false;
    }

    public int getCurrentStage() { return currentStage; }
    public int getCreepsDefeated() { return creepsDefeated; }
    public int getCreepsRequired() { return CREEPS_REQUIRED; }
    public boolean isBossActive() { return bossActive; }
}