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
        btnStart         = new UIButton(375, 320, 200, 45, "ENTER GAME", new Color(41, 128, 185), new Color(52, 152, 219));
        btnQuit          = new UIButton(375, 380, 200, 45, "EXIT", new Color(192, 57, 43), new Color(231, 76, 60));
        
        btnBattle        = new UIButton(60, 470, 200, 50, "BATTLE ARENA", new Color(192, 57, 43), new Color(231, 76, 60));
        btnGacha         = new UIButton(280, 470, 200, 50, "GACHA PORTAL", new Color(142, 68, 173), new Color(155, 89, 182));
        btnParty         = new UIButton(500, 470, 200, 50, "PARTY FORMATION", new Color(39, 174, 96), new Color(46, 204, 113));
        btnSettings      = new UIButton(720, 470, 180, 50, "SETTINGS", new Color(127, 140, 141), new Color(149, 165, 166));
        
        btnBack          = new UIButton(780, 25, 140, 35, "< BACK TO HUB", new Color(52, 73, 94), new Color(44, 62, 80));
        btnReturn        = new UIButton(375, 400, 200, 45, "RETURN TO HUB", new Color(41, 128, 185), new Color(52, 152, 219));
        
        btn1x            = new UIButton(270, 520, 195, 45, "PULL 1x (160 Gems)", new Color(142, 68, 173), new Color(155, 89, 182));
        btn10x           = new UIButton(485, 520, 195, 45, "PULL 10x (1600 Gems)", new Color(211, 84, 0), new Color(230, 126, 34));
        btnTrainHero     = new UIButton(375, 378, 200, 40, "TRAIN HERO (+1 LVL)", new Color(243, 156, 18), new Color(241, 196, 15));

        // Profile select
        btnNewProfile    = new UIButton(280, 450, 180, 45, "+ CREATE NEW", new Color(39, 174, 96), new Color(46, 204, 113));
        btnQuitProfile   = new UIButton(490, 450, 180, 45, "QUIT GAME", new Color(192, 57, 43), new Color(231, 76, 60));
        btnSaveGame      = new UIButton(325, 375, 300, 45, "SAVE PROGRESS MANUALLY", new Color(41, 128, 185), new Color(52, 152, 219));

        // Settings Buttons
        btnResStandard   = new UIButton(325, 180, 300, 45, "PRESET: 950 x 600 (DEFAULT)", new Color(41, 128, 185), new Color(52, 152, 219));
        btnResHD         = new UIButton(325, 245, 300, 45, "PRESET: 1280 x 720 (HD)", new Color(39, 174, 96), new Color(46, 204, 113));
        btnToggleVfx     = new UIButton(325, 310, 300, 45, "VFX PARTICLES: ENABLED", new Color(142, 68, 173), new Color(155, 89, 182));
    }

    public void updateHover(Point mouse) {
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