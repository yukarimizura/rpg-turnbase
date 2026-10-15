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
    private int defTimer = 0, healTimer = 0, supTimer = 0;

    public void updateResonance(PartyManager party, Character vanguard, List<VFXEffect> vfxList, List<DamageText> damageTexts) {
        List<Character> squad = party.getActiveParty();
        if (squad.size() <= 1) return;

        defTimer++; healTimer++; supTimer++;
        for (int i = 1; i < squad.size(); i++) {
            Character c = squad.get(i);
            if (c == null || !c.isAlive()) continue;

            if (c instanceof Defender && defTimer >= 420) {
                ((Defender) c).triggerResonance(vanguard, vfxList, damageTexts);
                defTimer = 0;
            } else if (c instanceof Healer && healTimer >= 300 && vanguard.getCurrentHp() < vanguard.getMaxHp() * 0.8f) {
                ((Healer) c).triggerResonance(vanguard, vfxList, damageTexts);
                healTimer = 0;
            } else if (c instanceof Support && supTimer >= 540) {
                ((Support) c).triggerResonance(vanguard, vfxList, damageTexts);
                supTimer = 0;
            }
        }
    }
}