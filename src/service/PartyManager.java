package service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import model.characters.Character;
import model.characters.Attacker;

public class PartyManager {
    private List<Character> activeParty;
    private List<Character> inactiveParty;
    private int activeIndex = 0;
    public static final int MAX_PARTY_SIZE = 5;
    public static final int MAX_INACTIVE_SIZE = 5;

    public PartyManager() {
        this.activeParty = new CopyOnWriteArrayList<>();
        this.inactiveParty = new CopyOnWriteArrayList<>();
    }

    public boolean addHeroToParty(Character hero) {
        if (hero == null) return false;

        if (hero.isVanguardEligible()) {
            if (activeParty.size() >= MAX_PARTY_SIZE || activeParty.contains(hero)) return false;
            activeParty.add(hero);
            return true;
        } else {
            if (inactiveParty.size() >= MAX_INACTIVE_SIZE || inactiveParty.contains(hero)) return false;
            inactiveParty.add(hero);
            return true;
        }
    }

    public boolean isInAnyParty(Character hero) {
        return activeParty.contains(hero) || inactiveParty.contains(hero);
    }

    /**
     * slotIndex 0..4 = Active Party (Khusus Attacker/DPS)
     * slotIndex 5..9 = Inactive Party (Khusus Defender/Healer/Support)
     */
    public void replacePartyMember(int slotIndex, Character newHero) {
        if (newHero == null || isInAnyParty(newHero)) return;

        if (newHero instanceof Attacker) {
            int targetIdx = (slotIndex >= 0 && slotIndex < MAX_PARTY_SIZE) ? slotIndex : activeParty.size();
            if (targetIdx < activeParty.size()) {
                Character old = activeParty.get(targetIdx);
                newHero.x = old.x;
                newHero.y = old.y;
                activeParty.set(targetIdx, newHero);
            } else if (activeParty.size() < MAX_PARTY_SIZE) {
                activeParty.add(newHero);
            }
        } else {
            int inactSlot = (slotIndex >= 5 && slotIndex < 10) ? (slotIndex - 5) : inactiveParty.size();
            if (inactSlot < inactiveParty.size()) {
                inactiveParty.set(inactSlot, newHero);
            } else if (inactiveParty.size() < MAX_INACTIVE_SIZE) {
                inactiveParty.add(newHero);
            }
        }
    }

    // Mengurangi cooldown untuk SEMUA hero di Active & Inactive party setiap tick
    public void tickAllCooldowns() {
        for (Character c : activeParty) {
            c.tickCooldown();
        }
        for (Character c : inactiveParty) {
            c.tickCooldown();
        }
    }

    public void switchHero(int index) {
        if (index >= 0 && index < activeParty.size()) {
            Character current = getActiveHero();
            Character next = activeParty.get(index);
            if (next != null && next.isAlive() && index != activeIndex) {
                if (current != null) {
                    next.x = current.x;
                    next.y = current.y;
                    if (current.bonusDefTimer > 0) {
                        next.applyDefBuff(current.bonusDefAmount, current.bonusDefTimer);
                    }
                    if (current.bonusAtkTimer > 0) {
                        next.applyAtkBuff(current.bonusAtkAmount, current.bonusAtkTimer);
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
        return activeParty.size() >= MAX_PARTY_SIZE && inactiveParty.size() >= MAX_INACTIVE_SIZE;
    }

    public Character getActiveHero() {
        if (activeParty.isEmpty()) return null;
        if (activeIndex >= activeParty.size()) activeIndex = 0;
        Character current = activeParty.get(activeIndex);
        if (current != null && !current.isAlive()) {
            for (int i = 0; i < activeParty.size(); i++) {
                Character candidate = activeParty.get(i);
                if (candidate != null && candidate.isAlive()) {
                    candidate.x = current.x;
                    candidate.y = current.y;
                    activeIndex = i;
                    return candidate;
                }
            }
        }
        return current;
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
        for (Character c : inactiveParty) {
            c.reviveFull();
        }
    }

    public List<Character> getActiveParty() {
        return activeParty;
    }

    public List<Character> getInactiveParty() {
        return inactiveParty;
    }

    public int getActiveIndex() {
        return activeIndex;
    }
}