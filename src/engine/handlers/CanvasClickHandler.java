package engine.handlers;

import engine.GameCanvas;
import engine.GameState;
import engine.handlers.click.ProfileClickHandler;
import service.*;
import view.component.CyberUIEffects;
import view.screen.PartyScreen;

import javax.swing.*;
import java.awt.*;
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
            
            if (btn.btnSaveGame.contains(m)) { 
                SaveManager.saveGame(profile, party, stage, gacha); 
                JOptionPane.showMessageDialog(canvas, "Progress saved!"); 
            }

            // PERBAIKAN: Penanganan Resolusi Fullscreen Native (1080p) vs Windowed HD (1280x720)
            if (btn.btnResStandard.contains(m) || btn.btnResHD.contains(m)) {
                Window top = SwingUtilities.getWindowAncestor(canvas);
                if (top instanceof JFrame) {
                    JFrame frame = (JFrame) top;
                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

                    boolean isFullscreenTarget = btn.btnResStandard.contains(m); // Tombol Fullscreen Native 1080p

                    frame.dispose(); // Wajib dispose() sebelum mengubah status undecorated frame

                    if (isFullscreenTarget) {
                        // 1. FULLSCREEN NATIVE MODE
                        frame.setUndecorated(true);
                        frame.setResizable(false);
                        
                        if (gd.isFullScreenSupported()) {
                            gd.setFullScreenWindow(frame);
                        } else {
                            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                            frame.setBounds(0, 0, screenSize.width, screenSize.height);
                        }
                    } else {
                        // 2. WINDOWED MODE (1280x720)
                        if (gd.getFullScreenWindow() == frame) {
                            gd.setFullScreenWindow(null); // Keluar dari Hardware Fullscreen
                        }
                        frame.setUndecorated(false);
                        frame.setResizable(true);
                        
                        canvas.setPreferredSize(new Dimension(1280, 720));
                        frame.pack();
                        frame.setLocationRelativeTo(null); // Posisikan window di tengah layar
                        frame.setVisible(true);
                    }

                    // Re-validate & Re-paint canvas Swing agar viewport langsung update penuh
                    if (canvas instanceof GameCanvas) {
                        ((GameCanvas) canvas).updateCanvasBounds(frame.getWidth(), frame.getHeight());
                    } else {
                        canvas.revalidate();
                        canvas.repaint();
                    }
                    canvas.requestFocusInWindow();
                }
            }
        } else if (state == GameState.BATTLE && btn.btnBack.contains(m)) {
            CyberUIEffects.startTransition(GameState.LOBBY, "RETREATING TO COMMAND HUB...");
        }
        return state;
    }
}