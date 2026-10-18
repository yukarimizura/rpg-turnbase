package engine.handlers.click;

import engine.GameState;
import engine.handlers.CanvasButtonManager;
import model.characters.Attacker;
import model.characters.Character;
import service.GachaService;
import service.PartyManager;
import service.PlayerProfile;
import service.SaveManager;
import service.StageManager;
import view.component.CyberUIEffects;
import view.screen.ProfileSelectScreen;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Point;
import java.util.List;

public class ProfileClickHandler {

    public static void handleProfileSelectClick(Point m, CanvasButtonManager btn,
                                                PlayerProfile profile, PartyManager party,
                                                StageManager stage, GachaService gacha,
                                                List<String> savedProfiles, JPanel canvas,
                                                Runnable onProfileCreated) {
        int clickedIdx = ProfileSelectScreen.getClickedProfileIndex(m, savedProfiles.size());
        if (clickedIdx != -1) {
            SaveManager.loadGame(savedProfiles.get(clickedIdx), profile, party, stage, gacha);
            CyberUIEffects.startTransition(GameState.TITLE, "LOADING COMMANDER PROFILE...");
        } else if (btn.btnNewProfile.contains(m)) {
            String name = JOptionPane.showInputDialog(canvas, "Enter Commander Name:", "Create Profile", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                profile.setPlayerName(name.trim());
                profile.setGems(1600);
                profile.setCredits(2000);
                profile.getOwnedHeroes().clear();
                party.getActiveParty().clear();
                party.getInactiveParty().clear();
                Attacker starter = new Attacker("HERO_" + (System.currentTimeMillis() % 1000), "Dawn Blade Dancer", "SR", "Blade Dancer", 1600, 180, 50);
                profile.addHero(starter);
                party.addHeroToParty(starter);
                SaveManager.saveGame(profile, party, stage, gacha);
                onProfileCreated.run();
                CyberUIEffects.startTransition(GameState.TITLE, "INITIALIZING NEW COMMANDER...");
            }
        } else if (btn.btnQuitProfile.contains(m)) {
            System.exit(0);
        }
    }

    public static void handleTrainHero(PlayerProfile profile, PartyManager party, StageManager stage, GachaService gacha) {
        Character leader = party.getActiveHero();
        if (leader != null && profile.getCredits() >= leader.getUpgradeCost()) {
            profile.addCredits(-leader.getUpgradeCost());
            leader.levelUpManual();
            SaveManager.saveGame(profile, party, stage, gacha);
        }
    }
}
