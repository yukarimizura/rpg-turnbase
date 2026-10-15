package model.characters.skills;

import model.characters.Character;
import model.characters.Defender;
import view.component.DamageText;
import view.component.VFXEffect;

import java.awt.Color;
import java.util.List;

public class DefenderSkillExecutor {
    public static void executeResonance(Defender defender, Character vanguard, 
                                        List<VFXEffect> vfxList, List<DamageText> damageTexts) {
        if ("Paladin Aegis".equals(defender.getSubRole())) {
            // Paladin memberikan buff Thorns + Shield DEF sedang
            int shieldBonus = (int) (defender.getDefense() * 0.5f);
            vanguard.applyDefBuff(shieldBonus, 300);
            vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 50, new Color(241, 196, 15)));
            damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "AEGIS THORNS +" + shieldBonus, new Color(241, 196, 15)));
        } else {
            // Guardian Shield memberikan Flat Shield DEF masif
            int shieldBonus = (int) (defender.getDefense() * 0.9f);
            vanguard.applyDefBuff(shieldBonus, 240);
            vfxList.add(new VFXEffect(vanguard.x, vanguard.y, 45, new Color(52, 152, 219)));
            damageTexts.add(new DamageText(vanguard.x, vanguard.y - 20, "GUARDIAN SHIELD +" + shieldBonus, new Color(52, 152, 219)));
        }
    }
}