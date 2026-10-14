package service;

import model.Attacker;
import model.Character;
import model.Defender;
import model.Healer;

import java.util.Random;

public class GachaService {
    private Random rand = new Random();
    private int pityCounter = 0;
    private Character lastPulledCharacter = null;

    public Character roll() {
        pityCounter++;
        String rarity;

        // Pity Guarantee Logic
        if (pityCounter >= 50) {
            rarity = "SSR";
            pityCounter = 0;
        } else if (pityCounter % 10 == 0) {
            int rollSr = rand.nextInt(100);
            rarity = (rollSr < 15) ? "SSR" : "SR";
            if (rarity.equals("SSR")) pityCounter = 0;
        } else {
            int rollVal = rand.nextInt(100);
            if (rollVal < 2) {
                rarity = "SSR";
                pityCounter = 0;
            } else if (rollVal < 15) {
                rarity = "SR";
            } else {
                rarity = "R";
            }
        }

        // Penyesuaian Base Stat sesuai Rarity
        int hpBase = 1000, atkBase = 100, defBase = 30;
        if (rarity.equals("SSR")) {
            hpBase = 2200; atkBase = 260; defBase = 70;
        } else if (rarity.equals("SR")) {
            hpBase = 1500; atkBase = 170; defBase = 45;
        }

        int roleType = rand.nextInt(3);
        String heroId = "HERO_" + (System.currentTimeMillis() % 10000);
        Character pulled;

        if (roleType == 0) {
            pulled = new Attacker(heroId, getHeroName(rarity, "Attacker"), rarity, (int)(hpBase * 0.9), (int)(atkBase * 1.4), defBase);
        } else if (roleType == 1) {
            pulled = new Defender(heroId, getHeroName(rarity, "Defender"), rarity, (int)(hpBase * 1.5), (int)(atkBase * 0.8), (int)(defBase * 1.8));
        } else {
            pulled = new Healer(heroId, getHeroName(rarity, "Healer"), rarity, (int)(hpBase * 1.1), atkBase, defBase);
        }

        this.lastPulledCharacter = pulled;
        return pulled;
    }

    private String getHeroName(String rarity, String role) {
        if (rarity.equals("SSR")) {
            return role.equals("Attacker") ? "Kagura Shadow" : (role.equals("Defender") ? "Aegis Prime" : "Dawn Priestess");
        } else if (rarity.equals("SR")) {
            return role.equals("Attacker") ? "Crimson Blade" : (role.equals("Defender") ? "Iron Vanguard" : "Verdant Druid");
        }
        return role.equals("Attacker") ? "Scout Fighter" : (role.equals("Defender") ? "Rookie Guard" : "Field Medic");
    }

    public int getPityCounter() {
        return pityCounter;
    }

    public Character getLastPulledCharacter() {
        return lastPulledCharacter;
    }
}