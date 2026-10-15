package service;

import model.Character;
import java.util.ArrayList;
import java.util.List;

public class PlayerProfile {
    private String playerName;
    private int cyberGems;
    private int credits;
    private List<Character> ownedHeroes;

    public PlayerProfile(String name) {
        this.playerName = name;
        this.cyberGems = 1600; // Starter modal untuk 10x Pull
        this.credits = 10000;
        this.ownedHeroes = new ArrayList<>();
    }

    public void addHero(Character c) {
        ownedHeroes.add(c);
    }

    public boolean spendGems(int amount) {
        if (cyberGems >= amount) {
            cyberGems -= amount;
            return true;
        }
        return false;
    }

    public void addGems(int amount) {
        this.cyberGems += amount;
    }

    public void addCredits(int amount) {
        this.credits += amount;
    }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String name) { this.playerName = name; }
    public void setGems(int gems) { this.cyberGems = gems; }
    public void setCredits(int credits) { this.credits = credits; }
    public int getCyberGems() { return cyberGems; }
    public int getCredits() { return credits; }
    public List<Character> getOwnedHeroes() { return ownedHeroes; }
    
}