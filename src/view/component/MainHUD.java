package view.component;

import model.characters.Character;
import model.npc.Enemy;
import service.PartyManager;
import service.StageManager;
import view.component.hud.BottomSquadSlotsHUD;
import view.component.hud.TopBattleStatusHUD;

import java.awt.Graphics2D;
import java.util.List;

public class MainHUD {

    public static void draw(Graphics2D g2, Character activeHero, PartyManager partyManager, StageManager stageManager, List<Enemy> enemies) {
        TopBattleStatusHUD.drawTopPanels(g2, activeHero, partyManager, stageManager, enemies);
        BottomSquadSlotsHUD.drawBottomSquadPanels(g2, partyManager);
    }
}