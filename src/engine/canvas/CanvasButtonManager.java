package engine.canvas;

import view.UIButton;
import java.awt.Color;
import java.awt.Point;

public class CanvasButtonManager {
    public UIButton btnStart, btnQuit, btnBattle, btnGacha, btnParty;
    public UIButton btnBack, btnReturn, btn1x, btn10x;
    public UIButton btnTrainHero; // Tombol upgrade hero di lobby

    public CanvasButtonManager() {
        btnStart     = new UIButton(375, 320, 200, 45, "ENTER GAME", new Color(41, 128, 185), new Color(52, 152, 219));
        btnQuit      = new UIButton(375, 380, 200, 45, "EXIT", new Color(192, 57, 43), new Color(231, 76, 60));
        
        btnBattle    = new UIButton(100, 460, 220, 50, "ENTER BATTLE ARENA", new Color(192, 57, 43), new Color(231, 76, 60));
        btnGacha     = new UIButton(365, 460, 220, 50, "GACHA PORTAL", new Color(142, 68, 173), new Color(155, 89, 182));
        btnParty     = new UIButton(630, 460, 220, 50, "PARTY FORMATION", new Color(39, 174, 96), new Color(46, 204, 113));
        
        btnBack      = new UIButton(780, 25, 140, 35, "< BACK TO HUB", new Color(52, 73, 94), new Color(44, 62, 80));
        btnReturn    = new UIButton(375, 400, 200, 45, "RETURN TO HUB", new Color(41, 128, 185), new Color(52, 152, 219));
        
        btn1x        = new UIButton(280, 450, 180, 45, "PULL 1x (160 Gems)", new Color(142, 68, 173), new Color(155, 89, 182));
        btn10x       = new UIButton(490, 450, 180, 45, "PULL 10x (1600 Gems)", new Color(211, 84, 0), new Color(230, 126, 34));
        
        btnTrainHero = new UIButton(375, 380, 200, 40, "TRAIN HERO (+1 LVL)", new Color(243, 156, 18), new Color(241, 196, 15));
    }

    public void updateHover(Point mouse) {
        btnStart.checkHover(mouse);
        btnQuit.checkHover(mouse);
        btnBattle.checkHover(mouse);
        btnGacha.checkHover(mouse);
        btnParty.checkHover(mouse);
        btnBack.checkHover(mouse);
        btnReturn.checkHover(mouse);
        btn1x.checkHover(mouse);
        btn10x.checkHover(mouse);
        btnTrainHero.checkHover(mouse);
    }
}