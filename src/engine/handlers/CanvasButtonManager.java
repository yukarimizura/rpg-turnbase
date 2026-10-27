package engine.handlers;

import java.awt.Color;
import java.awt.Point;
import view.component.UIButton;

public class CanvasButtonManager {
    public UIButton btnStart, btnQuit, btnBattle, btnGacha, btnParty;
    public UIButton btnBack, btnReturn, btn1x, btn10x, btnTrainHero;
    public UIButton btnSettings;

    // Profile & Save Buttons
    public UIButton btnNewProfile, btnQuitProfile, btnSaveGame;

    // Settings Buttons
    public UIButton btnResStandard, btnResHD, btnToggleVfx;

    public CanvasButtonManager() {
        int centerX = 960; // Center layar virtual 1920x1080

        // --- TITLE / MAIN MENU (Pusat Layar 1920x1080) ---
        btnStart         = new UIButton(centerX - 180, 580, 360, 65, "ENTER GAME", new Color(41, 128, 185), new Color(52, 152, 219));
        btnQuit          = new UIButton(centerX - 180, 665, 360, 65, "EXIT", new Color(192, 57, 43), new Color(231, 76, 60));
        
        // --- LOBBY HUB (Bawah Layar / Navigation Bar) ---
        btnBattle        = new UIButton(160, 880, 340, 75, "BATTLE ARENA", new Color(192, 57, 43), new Color(231, 76, 60));
        btnGacha         = new UIButton(550, 880, 340, 75, "GACHA PORTAL", new Color(142, 68, 173), new Color(155, 89, 182));
        btnParty         = new UIButton(940, 880, 340, 75, "PARTY FORMATION", new Color(39, 174, 96), new Color(46, 204, 113));
        btnSettings      = new UIButton(1330, 880, 340, 75, "SETTINGS", new Color(127, 140, 141), new Color(149, 165, 166));
        
        // --- GLOBAL NAVIGATION ---
        // Posisikan Back button dari X=1520 agar sejajar rapat dengan header glasscard gacha & party
        btnBack          = new UIButton(1520, 32, 320, 60, "< BACK TO HUB", new Color(52, 73, 94), new Color(44, 62, 80));
        btnReturn        = new UIButton(centerX - 180, 720, 360, 65, "RETURN TO HUB", new Color(41, 128, 185), new Color(52, 152, 219));
        
        // --- GACHA PORTAL ---
        btn1x            = new UIButton(centerX - 360, 910, 340, 70, "PULL 1x (160 Gems)", new Color(142, 68, 173), new Color(155, 89, 182));
        btn10x           = new UIButton(centerX + 20, 910, 340, 70, "PULL 10x (1600 Gems)", new Color(211, 84, 0), new Color(230, 126, 34));
        btnTrainHero     = new UIButton(centerX - 180, 680, 360, 60, "TRAIN HERO (+1 LVL)", new Color(243, 156, 18), new Color(241, 196, 15));

        // --- PROFILE SELECT ---
        btnNewProfile    = new UIButton(centerX - 320, 820, 300, 65, "+ CREATE NEW", new Color(39, 174, 96), new Color(46, 204, 113));
        btnQuitProfile   = new UIButton(centerX + 20, 820, 300, 65, "QUIT GAME", new Color(192, 57, 43), new Color(231, 76, 60));

        // --- SETTINGS SCREEN (Terpusat Simetris di Card Settings) ---
        int cardY = 170;
        btnResStandard   = new UIButton(centerX - 260, cardY + 160, 520, 65, "PRESET: FULLSCREEN (NATIVE 1080P)", new Color(41, 128, 185), new Color(52, 152, 219));
        btnResHD         = new UIButton(centerX - 260, cardY + 245, 520, 65, "PRESET: WINDOWED (1280 x 720)", new Color(39, 174, 96), new Color(46, 204, 113));
        btnToggleVfx     = new UIButton(centerX - 260, cardY + 330, 520, 65, "VFX PARTICLES: ENABLED", new Color(142, 68, 173), new Color(155, 89, 182));
        btnSaveGame      = new UIButton(centerX - 260, cardY + 450, 520, 65, "SAVE PROGRESS MANUALLY", new Color(41, 128, 185), new Color(52, 152, 219));
    }

    public void updateHover(Point mouse) {
        if (mouse == null) return;

        btnStart.checkHover(mouse);
        btnQuit.checkHover(mouse);
        btnBattle.checkHover(mouse);
        btnGacha.checkHover(mouse);
        btnParty.checkHover(mouse);
        btnSettings.checkHover(mouse);
        btnBack.checkHover(mouse);
        btnReturn.checkHover(mouse);
        btn1x.checkHover(mouse);
        btn10x.checkHover(mouse);
        btnTrainHero.checkHover(mouse);

        btnNewProfile.checkHover(mouse);
        btnQuitProfile.checkHover(mouse);
        btnSaveGame.checkHover(mouse);

        btnResStandard.checkHover(mouse);
        btnResHD.checkHover(mouse);
        btnToggleVfx.checkHover(mouse);
    }
}