package engine.handlers;

import engine.GameState;
import engine.handlers.click.ProfileClickHandler;
import service.*;
import view.component.CyberUIEffects;
import view.screen.PartyScreen;

import javax.swing.*;
import java.awt.Container;
import java.awt.Point;
import java.util.List;

public class CanvasClickHandler {
    public static GameState handleClick(GameState state, Point m, CanvasButtonManager btn,
                                     PlayerProfile profile, PartyManager party, StageManager stage,
                                     GachaService gacha, GachaHandler gachaHandler,
                                     List<String> savedProfiles, JPanel canvas, Runnable onProfileCreated) {
        if (CyberUIEffects.isTransitioning()) return state;

        if (state == GameState.PROFILE_SELECT) {
            ProfileClickHandler.handleProfileSelectClick(m, btn, profile, party, stage, gacha, savedProfiles, canvas, onProfileCreated);
        } else if (state == GameState.TITLE) {
            if (btn.btnStart.contains(m)) CyberUIEffects.startTransition(GameState.LOBBY, "ENTERING COMMAND HUB...");
            if (btn.btnQuit.contains(m)) System.exit(0);
        } else if (state == GameState.LOBBY) {
            if (btn.btnBattle.contains(m)) {
                canvas.requestFocusInWindow();
                CyberUIEffects.startTransition(GameState.BATTLE, "DEPLOYING SQUAD TO ARENA...");
            } else if (btn.btnGacha.contains(m)) {
                CyberUIEffects.startTransition(GameState.GACHA, "OPENING STELLAR PORTAL...");
            } else if (btn.btnParty.contains(m)) {
                CyberUIEffects.startTransition(GameState.PARTY, "LOADING SQUAD FORMATION...");
            } else if (btn.btnSettings.contains(m)) {
                CyberUIEffects.startTransition(GameState.SETTINGS, "ACCESSING SYSTEM CONFIG...");
            } else if (btn.btnTrainHero.contains(m)) {
                ProfileClickHandler.handleTrainHero(profile, party, stage, gacha);
            }
        } else if (state == GameState.GACHA) {
            if (btn.btnBack.contains(m)) CyberUIEffects.startTransition(GameState.LOBBY, "RETURNING TO COMMAND HUB...");
            if (btn.btn1x.contains(m)) { gachaHandler.handlePull(1, 160, profile, gacha, party); SaveManager.saveGame(profile, party, stage, gacha); }
            if (btn.btn10x.contains(m)) { gachaHandler.handlePull(10, 1600, profile, gacha, party); SaveManager.saveGame(profile, party, stage, gacha); }
        } else if (state == GameState.PARTY) {
            if (btn.btnBack.contains(m)) {
                CyberUIEffects.startTransition(GameState.LOBBY, "SAVING SQUAD FORMATION...");
                return state;
            }
            PartyScreen.handleClick(m, party, profile);
            SaveManager.saveGame(profile, party, stage, gacha);
        } else if (state == GameState.SETTINGS) {
            if (btn.btnBack.contains(m)) CyberUIEffects.startTransition(GameState.LOBBY, "RETURNING TO COMMAND HUB...");
            if (btn.btnToggleVfx.contains(m)) {
                CyberUIEffects.vfxEnabled = !CyberUIEffects.vfxEnabled;
                btn.btnToggleVfx.setText("VFX PARTICLES: " + (CyberUIEffects.vfxEnabled ? "ENABLED" : "DISABLED"));
            }
            if (btn.btnSaveGame.contains(m)) { SaveManager.saveGame(profile, party, stage, gacha); JOptionPane.showMessageDialog(canvas, "Progress saved!"); }
            if (btn.btnResStandard.contains(m) || btn.btnResHD.contains(m)) {
                Container top = SwingUtilities.getWindowAncestor(canvas);
                if (top != null) top.setSize(btn.btnResStandard.contains(m) ? 950 : 1280, btn.btnResStandard.contains(m) ? 630 : 750);
            }
        } else if (state == GameState.BATTLE && btn.btnBack.contains(m)) {
            CyberUIEffects.startTransition(GameState.LOBBY, "RETREATING TO COMMAND HUB...");
        }
        return state;
    }
}