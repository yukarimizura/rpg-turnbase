package view.screen.party;

import model.characters.Character;
import view.component.CyberUIEffects;
import view.component.SpriteRenderer;

import java.awt.*;

public class SquadSlotCardRenderer {

    public static void drawSquadSlotCard(Graphics2D g2, int x, int y, int w, int h,
                                         String slotTitle, Character c, boolean isSelected, Color themeColor) {
        Color border = isSelected ? new Color(255, 215, 0) : (c != null ? themeColor : new Color(70, 80, 100));
        CyberUIEffects.drawGlassCard(g2, x, y, w, h, border, isSelected);

        g2.setColor(isSelected ? new Color(255, 225, 80) : new Color(180, 195, 215));
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2.drawString(slotTitle, x + 10, y + 16);

        if (c != null) {
            Color rarCol = SpriteRenderer.getRarityColor(c.getRarity());
            g2.setColor(rarCol);
            g2.drawString("★" + c.getRarity(), x + w - 42, y + 16);

            // Mini Character Sprite on Left
            SpriteRenderer.drawCharacter(g2, c, x + 32, y + 58, 0.9f);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString(trimText(c.getName(), 15), x + 62, y + 36);

            g2.setColor(SpriteRenderer.getRoleAccentColor(c.getRole(), c.getSubRole()));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString(c.getSubRole(), x + 62, y + 51);

            g2.setColor(new Color(200, 215, 230));
            g2.drawString("Lv." + c.getLevel() + " HP:" + c.getMaxHp(), x + 62, y + 67);
            g2.drawString("ATK:" + c.getAttack() + " DEF:" + c.getDefense(), x + 62, y + 82);
        } else {
            g2.setColor(new Color(110, 120, 140));
            g2.setFont(new Font("SansSerif", Font.ITALIC, 11));
            g2.drawString("[ EMPTY SLOT ]", x + 38, y + 55);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g2.drawString("Klik untuk memilih", x + 40, y + 73);
        }
    }

    private static String trimText(String s, int maxLen) {
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }
}
