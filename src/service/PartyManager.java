package service;

import model.characters.Character;
import service.party.SquadFormationHelper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PartyManager {
    private List<Character> activeParty = new CopyOnWriteArrayList<>();
    private List<Character> inactiveParty = new CopyOnWriteArrayList<>();
    private int activeIndex = 0;
    public static final int MAX_PARTY_SIZE = 5;
    public static final int MAX_INACTIVE_SIZE = 5;

    public boolean addHeroToParty(Character hero) {
        if (hero == null) return false;
        if (hero.isVanguardEligible()) {
            if (activeParty.size() >= MAX_PARTY_SIZE || activeParty.contains(hero)) return false;
            return activeParty.add(hero);
        } else {
            if (inactiveParty.size() >= MAX_INACTIVE_SIZE || inactiveParty.contains(hero)) return false;
            return inactiveParty.add(hero);
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
        SquadFormationHelper.replaceMember(activeParty, inactiveParty, slotIndex, newHero);
    }

    // Mengurangi cooldown untuk SEMUA hero di Active & Inactive party setiap tick
    public void tickAllCooldowns() {
        for (Character c : activeParty) c.tickCooldown();
        for (Character c : inactiveParty) c.tickCooldown();
    }

    public void switchHero(int index) {
        if (index >= 0 && index < activeParty.size()) {
            Character current = getActiveHero();
            Character next = activeParty.get(index);
            if (next != null && next.isAlive() && index != activeIndex) {
                SquadFormationHelper.transferPositionAndBuffs(current, next);
                this.activeIndex = index;
            }
        }
    }

    public void setActiveIndex(int index) { switchHero(index); }

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
        for (Character c : activeParty) if (c.isAlive()) return false;
        return true;
    }

    public void reviveAllParty() {
        for (Character c : activeParty) c.reviveFull();
        for (Character c : inactiveParty) c.reviveFull();
    }

    public List<Character> getActiveParty() { return activeParty; }
    public List<Character> getInactiveParty() { return inactiveParty; }
    public int getActiveIndex() { return activeIndex; }
}