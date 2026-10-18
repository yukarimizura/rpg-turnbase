package engine.canvas;

import model.characters.Character;
import model.characters.Defender;
import model.characters.Healer;
import model.characters.Support;
import service.PartyManager;
import view.component.DamageText;
import view.component.VFXEffect;

import java.util.List;

public class SupportResonanceEngine {

    public void updateResonance(PartyManager party, Character vanguard, List<VFXEffect> vfxList, List<DamageText> damageTexts) {
        if (vanguard == null || !vanguard.isAlive()) return;
        List<Character> supportSquad = party.getInactiveParty();
        if (supportSquad.isEmpty()) return;

        for (Character c : supportSquad) {
            if (c == null || !c.isAlive() || c.skillCdCurrent > 0) continue;

            if (c instanceof Healer) {
                // Langsung heal saat HP Active DPS turun dan cooldown Healer siap
                if (vanguard.getCurrentHp() < vanguard.getMaxHp()) {
                    ((Healer) c).triggerResonance(vanguard, vfxList, damageTexts);
                }
            } else if (c instanceof Defender) {
                // Langsung pasang shield/buff DEF saat buff DEF Active DPS habis dan cooldown Defender siap
                if (vanguard.bonusDefTimer <= 0) {
                    ((Defender) c).triggerResonance(vanguard, vfxList, damageTexts);
                }
            } else if (c instanceof Support) {
                if ("Chronomancer".equals(c.getSubRole())) {
                    // Langsung percepat cooldown saat Skill/Ult Active DPS sedang cooldown
                    if (vanguard.skillCdCurrent > 0 || vanguard.ultCdCurrent > 0) {
                        ((Support) c).triggerResonance(vanguard, vfxList, damageTexts);
                    }
                } else {
                    // Commander Aura: langsung pasang buff ATK saat buff ATK Active DPS habis
                    if (vanguard.bonusAtkTimer <= 0) {
                        ((Support) c).triggerResonance(vanguard, vfxList, damageTexts);
                    }
                }
            }
        }
    }
}