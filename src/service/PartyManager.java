package service;

import model.Character;
import model.Defender;
import model.Healer;

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
        syncPartyReferences();
        return true;
    }

    public void replacePartyMember(int slotIndex, Character newHero) {
        if (newHero == null) return;
        // Jika hero sudah ada di party di slot lain, abaikan
        if (activeParty.contains(newHero)) return;

        if (slotIndex >= 0 && slotIndex < activeParty.size()) {
            activeParty.set(slotIndex, newHero);
        } else if (activeParty.size() < MAX_PARTY_SIZE) {
            activeParty.add(newHero);
        }
        syncPartyReferences();
    }

    // Berikan referensi seluruh party ke Healer agar skill heal memulihkan semua hero
    public void syncPartyReferences() {
        for (Character c : activeParty) {
            if (c instanceof Healer) {
                ((Healer) c).setPartyReference(activeParty);
            }
        }
    }

    // Mengurangi cooldown untuk SEMUA hero di party setiap tick
    public void tickAllCooldowns() {
        for (Character c : activeParty) {
            c.tickCooldown();
        }
    }

    // Menerapkan buff Defender ke Main DPS saat swap
    public void switchHero(int index) {
        if (index >= 0 && index < activeParty.size()) {
            Character current = getActiveHero();
            Character next = activeParty.get(index);
            if (next != null && next.isAlive()) {
                if (current != null) {
                    next.x = current.x;
                    next.y = current.y;

                    // Jika hero sebelumnya adalah Defender dan punya buff shield aktif, oper ke hero berikutnya
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

    public List<Character> getActiveParty() { return activeParty; }
    public int getActiveIndex() { return activeIndex; }
}