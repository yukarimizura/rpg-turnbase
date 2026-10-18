package view.component.sprite;

import java.awt.Color;

public class SpriteColorPalette {

    public static Color getRarityColor(String rarity) {
        if ("SSR".equals(rarity)) return new Color(255, 215, 0);
        if ("SR".equals(rarity)) return new Color(185, 95, 255);
        return new Color(52, 162, 235);
    }

    public static Color getRolePrimaryColor(String role) {
        if ("Attacker".equals(role)) return new Color(210, 55, 50);
        if ("Defender".equals(role)) return new Color(41, 128, 185);
        if ("Healer".equals(role)) return new Color(39, 174, 96);
        return new Color(215, 160, 20);
    }

    public static Color getRoleAccentColor(String role, String subRole) {
        if ("Attacker".equals(role)) {
            if ("Spellcaster".equals(subRole)) return new Color(190, 110, 255);
            if ("Ranger".equals(subRole)) return new Color(80, 240, 180);
            if ("Artillery".equals(subRole)) return new Color(255, 150, 50);
            return new Color(255, 95, 85);
        }
        if ("Defender".equals(role)) return new Color(100, 210, 255);
        if ("Healer".equals(role)) return new Color(110, 255, 165);
        return new Color(255, 230, 90);
    }
}
