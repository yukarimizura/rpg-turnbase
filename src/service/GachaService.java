package service;

import model.Attacker;
import model.Character;
import model.Defender;
import model.Healer;
import java.util.Random;

public class GachaService {
    private Random random;
    private int counter;

    public GachaService() {
        this.random = new Random();
        this.counter = 1;
    }

    public Character roll() {
        int chance = random.nextInt(100) + 1; // 1 - 100
        String rarity;
        int hpBase, atkBase, defBase;

        if (chance <= 2) { // 2% SSR
            rarity = "SSR";
            hpBase = 4000;
            atkBase = 1200;
            defBase = 450;
        } else if (chance <= 15) { // 13% SR
            rarity = "SR";
            hpBase = 2600;
            atkBase = 750;
            defBase = 280;
        } else { // 85% R
            rarity = "R";
            hpBase = 1600;
            atkBase = 450;
            defBase = 160;
        }

        // Tentukan Role acak (0: Attacker, 1: Defender, 2: Healer)
        int roleType = random.nextInt(3);
        String id = String.format("C%03d", counter++);

        switch (roleType) {
            case 0:
                return new Attacker(id, "Blade Dancer", rarity, hpBase, atkBase + 200, defBase);
            case 1:
                return new Defender(id, "Aegis Sentinel", rarity, hpBase + 1000, atkBase - 100, defBase + 200);
            case 2:
            default:
                return new Healer(id, "Dawn Priestess", rarity, hpBase + 300, atkBase - 50, defBase + 50);
        }
    }
}