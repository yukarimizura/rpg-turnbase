package service.party;

import model.characters.Attacker;
import model.characters.Character;
import service.PartyManager;

import java.util.List;

public class SquadFormationHelper {

    /**
     * slotIndex 0..4 = Active Party (Khusus Attacker/DPS)
     * slotIndex 5..9 = Inactive Party (Khusus Defender/Healer/Support)
     */
    public static void replaceMember(List<Character> activeParty, List<Character> inactiveParty,
                                     int slotIndex, Character newHero) {
        if (newHero == null || activeParty.contains(newHero) || inactiveParty.contains(newHero)) return;

        if (newHero instanceof Attacker) {
            int targetIdx = (slotIndex >= 0 && slotIndex < PartyManager.MAX_PARTY_SIZE) ? slotIndex : activeParty.size();
            if (targetIdx < activeParty.size()) {
                Character old = activeParty.get(targetIdx);
                newHero.x = old.x;
                newHero.y = old.y;
                activeParty.set(targetIdx, newHero);
            } else if (activeParty.size() < PartyManager.MAX_PARTY_SIZE) {
                activeParty.add(newHero);
            }
        } else {
            int inactSlot = (slotIndex >= 5 && slotIndex < 10) ? (slotIndex - 5) : inactiveParty.size();
            if (inactSlot < inactiveParty.size()) {
                inactiveParty.set(inactSlot, newHero);
            } else if (inactiveParty.size() < PartyManager.MAX_INACTIVE_SIZE) {
                inactiveParty.add(newHero);
            }
        }
    }

    public static void transferPositionAndBuffs(Character fromHero, Character toHero) {
        if (fromHero == null || toHero == null) return;
        toHero.x = fromHero.x;
        toHero.y = fromHero.y;
        if (fromHero.bonusDefTimer > 0) {
            toHero.applyDefBuff(fromHero.bonusDefAmount, fromHero.bonusDefTimer);
        }
        if (fromHero.bonusAtkTimer > 0) {
            toHero.applyAtkBuff(fromHero.bonusAtkAmount, fromHero.bonusAtkTimer);
        }
    }
}
