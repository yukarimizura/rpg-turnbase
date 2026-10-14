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
            Character current = getActiveHero();
            Character next = activeParty.get(index);
            // Hanya bisa ganti ke hero yang masih hidup
            if (next != null && next.isAlive()) {
                if (current != null) {
                    next.x = current.x;
                    next.y = current.y;
                }
                this.activeIndex = index;
            }
        }
    }

    // Cek apakah semua hero di dalam party sudah gugur
    public boolean isAllPartyDead() {
        if (activeParty.isEmpty()) return true;
        for (Character c : activeParty) {
            if (c.isAlive()) return false;
        }
        return true;
    }

    // Pemulihan HP seluruh party saat kembali ke Hub
    public void reviveAllParty() {
        for (Character c : activeParty) {
            c.reviveFull();
        }
    }

    public List<Character> getActiveParty() {
        return activeParty;
    }

    public int getActiveIndex() {
        return activeIndex;
    }
}