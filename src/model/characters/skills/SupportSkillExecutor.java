package model.characters.skills;

import model.characters.Character;
import model.characters.Support;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Color;
import java.util.List;

public class SupportSkillExecutor {
    public static void executeResonance(Support support, Character vanguard, 
                                        List<VFXEffect> vfxList, List<DamageText> damageTexts) {
        if ("Chronomancer".equals(support.getSubRole())) {
            // Chrono: Percepat CD Skill Vanguard
            vanguard.skillCdCurrent = Math.max(0, vanguard.skillCdCurrent - 60);
            vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 50, new Color(155, 89, 182)));
            damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "CHRONO RECHARGE! -1s CD", new Color(155, 89, 182)));
        } else {
            // Commander Aura: Buff Attack masif (+50% ATK Support)
            int atkBonus = (int) (support.getAttack() * 0.5f);
            vanguard.applyAtkBuff(atkBonus, 240);
            vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 55, new Color(241, 196, 15)));
            damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "WAR CRY +" + atkBonus + " ATK", new Color(241, 196, 15)));
        }
    }
}