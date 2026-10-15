package model.characters.skills;

import model.characters.Character;
import model.characters.Healer;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Color;
import java.util.List;

public class HealerSkillExecutor {
    public static void executeResonance(Healer healer, Character vanguard, 
                                        List<VFXEffect> vfxList, List<DamageText> damageTexts) {
        if ("Druid Renewal".equals(healer.getSubRole())) {
            // Druid: Denyut Regen HoT berkala yang stabil
            int healAmount = (int) (healer.getMaxHp() * 0.18f);
            vanguard.heal(healAmount);
            vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 40, new Color(46, 204, 113)));
            damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "RENEWAL +" + healAmount, new Color(46, 204, 113)));
        } else {
            // Cleric Grace: Pemulihan besar instan
            int healAmount = (int) (healer.getMaxHp() * 0.35f);
            vanguard.heal(healAmount);
            vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 55, new Color(39, 174, 96)));
            damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "CLERIC GRACE +" + healAmount, new Color(39, 174, 96)));
        }
    }
}