package service;

import model.Character;
import java.util.ArrayList;
import java.util.List;

public class PartyManager {
    private List<Character> activeParty;
    private int activeIndex = 0;
    public static final int MAX_PARTY_SIZE = 5;

    public PartyManager() {
        this.activeParty = new ArrayList<>();
    }

    public boolean addHeroToParty(Character hero) {
        if (activeParty.size() >= MAX_PARTY_SIZE) return false;
        if (activeParty.contains(hero)) return false;
        activeParty.add(hero);
        return true;
    }

    public boolean isPartyFull() {
        return activeParty.size() >= MAX_PARTY_SIZE;
    }

    public Character getActiveHero() {
        if (activeParty.isEmpty()) return null;
        if (activeIndex >= activeParty.size()) activeIndex = 0;
        return activeParty.get(activeIndex);
    }

    public void switchHero(int index) {
        if (index >= 0 && index < activeParty.size()) {
            // Sinkronkan posisi saat berganti hero agar tidak teleport
            Character current = getActiveHero();
            Character next = activeParty.get(index);
            if (current != null && next != null) {
                next.x = current.x;
                next.y = current.y;
            }
            this.activeIndex = index;
        }
    }

    public List<Character> getActiveParty() {
        return activeParty;
    }

    public int getActiveIndex() {
        return activeIndex;
    }
}