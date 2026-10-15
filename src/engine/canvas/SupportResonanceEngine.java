package engine.canvas;

import service.PartyManager;
import view.DamageText;
import view.VFXEffect;

import java.awt.Color;
import java.util.List;

import model.characters.Character;
import model.characters.Defender;
import model.characters.Healer;
import model.characters.Support;

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
                int def = (int)(c.getDefense() * 0.75f);
                vanguard.applyDefBuff(def, 240);
                vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 45, new Color(52, 152, 219)));
                damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "DEF SHIELD +" + def, new Color(52, 152, 219)));
                defTimer = 0;
            } else if (c instanceof Healer && healTimer >= 300 && vanguard.getCurrentHp() < vanguard.getMaxHp() * 0.8f) {
                int heal = (int)(c.getMaxHp() * 0.25f);
                vanguard.heal(heal);
                vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 50, new Color(46, 204, 113)));
                damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "REGEN +" + heal, new Color(46, 204, 113)));
                healTimer = 0;
            } else if (c instanceof Support && supTimer >= 540) {
                int atk = (int)(c.getAttack() * 0.5f);
                vanguard.applyAtkBuff(atk, 240);
                vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 55, new Color(241, 196, 15)));
                damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "ATK BOOST +" + atk, new Color(241, 196, 15)));
                supTimer = 0;
            }
        }
    }
}