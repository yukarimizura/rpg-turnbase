package service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import model.characters.Character;
import model.characters.Attacker;
import model.characters.Defender;
import model.characters.Healer;
import model.characters.Support;

public class GachaService {
    private Random rand = new Random();
    private int pityCounter = 0;
    private int idSequence = 100;
    private Character lastPulledCharacter = null;
    private List<Character> lastPulledBatch = new CopyOnWriteArrayList<>();

    private final String[] attackerSubRoles = {"Blade Dancer", "Ranger", "Berserker", "Spellcaster", "Artillery"};
    private final String[] defenderSubRoles = {"Guardian Shield", "Paladin Aegis"};
    private final String[] healerSubRoles   = {"Cleric Grace", "Druid Renewal"};
    private final String[] supportSubRoles  = {"Commander Aura", "Chronomancer"};

    public void startNewBatch() {
        lastPulledBatch.clear();
    }

    public Character roll() {
        pityCounter++;
        idSequence++;
        String rarity;

        if (pityCounter >= 50) {
            rarity = "SSR";
            pityCounter = 0;
        } else if (pityCounter % 10 == 0) {
            int rollSr = rand.nextInt(100);
            rarity = (rollSr < 15) ? "SSR" : "SR";
            if (rarity.equals("SSR")) pityCounter = 0;
        } else {
            int rollVal = rand.nextInt(100);
            if (rollVal < 3) {
                rarity = "SSR";
                pityCounter = 0;
            } else if (rollVal < 18) {
                rarity = "SR";
            } else {
                rarity = "R";
            }
        }

        int hpBase = 1000, atkBase = 100, defBase = 30;
        if (rarity.equals("SSR")) {
            hpBase = 2200; atkBase = 260; defBase = 70;
        } else if (rarity.equals("SR")) {
            hpBase = 1500; atkBase = 170; defBase = 45;
        }

        int roleType = rand.nextInt(4); // 0: Attacker, 1: Defender, 2: Healer, 3: Support
        String heroId = "HERO_" + (System.currentTimeMillis() % 10000) + "_" + idSequence;
        Character pulled;

        if (roleType == 0) {
            String sub = attackerSubRoles[rand.nextInt(attackerSubRoles.length)];
            pulled = new Attacker(heroId, rarity + " " + sub, rarity, sub, (int)(hpBase * 0.9), (int)(atkBase * 1.4), defBase);
        } else if (roleType == 1) {
            String sub = defenderSubRoles[rand.nextInt(defenderSubRoles.length)];
            pulled = new Defender(heroId, rarity + " " + sub, rarity, sub, (int)(hpBase * 1.5), (int)(atkBase * 0.8), (int)(defBase * 1.8));
        } else if (roleType == 2) {
            String sub = healerSubRoles[rand.nextInt(healerSubRoles.length)];
            pulled = new Healer(heroId, rarity + " " + sub, rarity, sub, (int)(hpBase * 1.1), atkBase, defBase);
        } else {
            String sub = supportSubRoles[rand.nextInt(supportSubRoles.length)];
            pulled = new Support(heroId, rarity + " " + sub, rarity, sub, hpBase, atkBase, defBase);
        }

        this.lastPulledCharacter = pulled;
        this.lastPulledBatch.add(pulled);
        return pulled;
    }

    public int getPityCounter() { return pityCounter; }
    public Character getLastPulledCharacter() { return lastPulledCharacter; }
    public List<Character> getLastPulledBatch() { return lastPulledBatch; }
    public void setPityCounter(int pity) { this.pityCounter = pity; }
}