package engine.canvas;

import engine.GameState;
import model.characters.Character;
import model.characters.Attacker;
import service.*;
import view.screen.PartyScreen;
import view.screen.ProfileSelectScreen;

import javax.swing.*;
import java.awt.Container;
import java.awt.Point;
import java.util.List;

public class CanvasClickHandler {
    public static GameState handleClick(GameState state, Point m, CanvasButtonManager btn,
                                     PlayerProfile profile, PartyManager party, StageManager stage,
                                     GachaService gacha, GachaHandler gachaHandler,
                                     List<String> savedProfiles, JPanel canvas, Runnable onProfileCreated) {
        if (state == GameState.PROFILE_SELECT) {
            int clickedIdx = ProfileSelectScreen.getClickedProfileIndex(m, savedProfiles.size());
            if (clickedIdx != -1) {
                SaveManager.loadGame(savedProfiles.get(clickedIdx), profile, party, stage, gacha);
                return GameState.TITLE;
            } else if (btn.btnNewProfile.contains(m)) {
                String name = JOptionPane.showInputDialog(canvas, "Enter Commander Name:", "Create Profile", JOptionPane.PLAIN_MESSAGE);
                if (name != null && !name.trim().isEmpty()) {
                    profile.setPlayerName(name.trim());
                    profile.setGems(1600);
                    profile.setCredits(2000);
                    profile.getOwnedHeroes().clear();
                    party.getActiveParty().clear();
                    Attacker starter = new Attacker("HERO_" + (System.currentTimeMillis() % 1000), "Dawn Blade Dancer", "SR", "Blade Dancer", 1600, 180, 50);
                    profile.addHero(starter);
                    party.addHeroToParty(starter);
                    SaveManager.saveGame(profile, party, stage, gacha);
                    onProfileCreated.run();
                    return GameState.TITLE;
                }
            } else if (btn.btnQuitProfile.contains(m)) System.exit(0);
        } else if (state == GameState.TITLE) {
            if (btn.btnStart.contains(m)) return GameState.LOBBY;
            if (btn.btnQuit.contains(m)) System.exit(0);
        } else if (state == GameState.LOBBY) {
            if (btn.btnBattle.contains(m)) { canvas.requestFocusInWindow(); return GameState.BATTLE; }
            if (btn.btnGacha.contains(m)) return GameState.GACHA;
            if (btn.btnParty.contains(m)) return GameState.PARTY;
            if (btn.btnSettings.contains(m)) return GameState.SETTINGS;
            if (btn.btnTrainHero.contains(m)) handleTrainHero(profile, party, stage, gacha);
        } else if (state == GameState.GACHA) {
            if (btn.btnBack.contains(m)) return GameState.LOBBY;
            if (btn.btn1x.contains(m)) { gachaHandler.handlePull(1, 160, profile, gacha, party); SaveManager.saveGame(profile, party, stage, gacha); }
            if (btn.btn10x.contains(m)) { gachaHandler.handlePull(10, 1600, profile, gacha, party); SaveManager.saveGame(profile, party, stage, gacha); }
        } else if (state == GameState.PARTY) {
            if (btn.btnBack.contains(m)) return GameState.LOBBY;
            PartyScreen.handleClick(m, party, profile);
            SaveManager.saveGame(profile, party, stage, gacha);
        } else if (state == GameState.SETTINGS) {
            if (btn.btnBack.contains(m)) return GameState.LOBBY;
            if (btn.btnSaveGame.contains(m)) { SaveManager.saveGame(profile, party, stage, gacha); JOptionPane.showMessageDialog(canvas, "Progress saved!"); }
            if (btn.btnResStandard.contains(m) || btn.btnResHD.contains(m)) {
                Container top = SwingUtilities.getWindowAncestor(canvas);
                if (top != null) top.setSize(btn.btnResStandard.contains(m) ? 950 : 1280, btn.btnResStandard.contains(m) ? 630 : 750);
            }
        } else if (state == GameState.BATTLE && btn.btnBack.contains(m)) return GameState.LOBBY;
        return state;
    }

    private static void handleTrainHero(PlayerProfile profile, PartyManager party, StageManager stage, GachaService gacha) {
        Character leader = party.getActiveHero();
        if (leader != null && profile.getCredits() >= leader.getUpgradeCost()) {
            profile.addCredits(-leader.getUpgradeCost());
            leader.levelUpManual();
            SaveManager.saveGame(profile, party, stage, gacha);
        }
    }
}