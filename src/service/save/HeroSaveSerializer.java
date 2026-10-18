package service.save;

import model.characters.Attacker;
import model.characters.Character;
import model.characters.Defender;
import model.characters.Healer;
import model.characters.Support;

public class HeroSaveSerializer {

    public static String serializeHero(Character c) {
        return "HERO;" + c.getId() + ";" + c.getName() + ";" + c.getRarity() + ";"
                + c.getSubRole() + ";" + c.getRole() + ";" + c.getLevel() + ";"
                + c.getMaxHp() + ";" + c.getAttack() + ";" + c.getDefense();
    }

    public static Character deserializeHero(String line) {
        String[] d = line.split(";");
        String id = d[1], name = d[2], rarity = d[3], sub = d[4], role = d[5];
        int level = Integer.parseInt(d[6]);
        int hp = Integer.parseInt(d[7]);
        int atk = Integer.parseInt(d[8]);
        int def = Integer.parseInt(d[9]);

        // Compute base level-1 stats before applying levelUpManual
        int baseHp = hp - (level - 1) * 250;
        int baseAtk = atk - (level - 1) * 40;
        int baseDef = def - (level - 1) * 15;

        Character c;
        if ("Attacker".equals(role)) {
            c = new Attacker(id, name, rarity, sub, baseHp, baseAtk, baseDef);
        } else if ("Defender".equals(role)) {
            c = new Defender(id, name, rarity, sub, baseHp, baseAtk, baseDef);
        } else if ("Healer".equals(role)) {
            c = new Healer(id, name, rarity, sub, baseHp, baseAtk, baseDef);
        } else {
            c = new Support(id, name, rarity, sub, baseHp, baseAtk, baseDef);
        }

        // Set level up stats
        for (int lv = 1; lv < level; lv++) {
            c.levelUpManual();
        }
        return c;
    }
}
