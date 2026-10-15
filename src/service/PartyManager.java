package service;

import model.Attacker;
import model.Character;
import model.Defender;

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
        if (hero == null) return false;
        if (activeParty.size() >= MAX_PARTY_SIZE) return false;
        if (activeParty.contains(hero)) return false;

        // Slot 1 (Vanguard) wajib tipe Attacker
        if (activeParty.isEmpty() && !hero.isVanguardEligible()) {
            return false;
        }

        activeParty.add(hero);
        return true;
    }

    public void replacePartyMember(int slotIndex, Character newHero) {
        if (newHero == null) return;
        if (activeParty.contains(newHero)) return;

        // Validasi: Slot 0 (Vanguard) hanya boleh diisi oleh Attacker
        if (slotIndex == 0 && !(newHero instanceof Attacker)) {
            return;
        }

        if (slotIndex >= 0 && slotIndex < activeParty.size()) {
            activeParty.set(slotIndex, newHero);
        } else if (activeParty.size() < MAX_PARTY_SIZE) {
            activeParty.add(newHero);
        }
    }

    // Mengurangi cooldown untuk SEMUA hero di party setiap tick
    public void tickAllCooldowns() {
        for (Character c : activeParty) {
            c.tickCooldown();
        }
    }

    public void switchHero(int index) {
        if (index >= 0 && index < activeParty.size()) {
            Character current = getActiveHero();
            Character next = activeParty.get(index);
            if (next != null && next.isAlive()) {
                if (current != null) {
                    next.x = current.x;
                    next.y = current.y;

                    if (current instanceof Defender && current.bonusDefTimer > 0) {
                        next.applyDefBuff(current.bonusDefAmount, current.bonusDefTimer);
                    }
                }
                this.activeIndex = index;
            }
        }
    }

    public void setActiveIndex(int index) {
        switchHero(index);
    }

    public boolean isPartyFull() {
        return activeParty.size() >= MAX_PARTY_SIZE;
    }

    public Character getActiveHero() {
        if (activeParty.isEmpty()) return null;
        if (activeIndex >= activeParty.size()) activeIndex = 0;
        return activeParty.get(activeIndex);
    }

    public boolean isAllPartyDead() {
        if (activeParty.isEmpty()) return true;
        for (Character c : activeParty) {
            if (c.isAlive()) return false;
        }
        return true;
    }

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