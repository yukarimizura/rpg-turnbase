package engine.handlers;

import engine.GameCanvas;
import engine.GameState;
import engine.canvas.CombatEngine;
import model.npc.Enemy;
import service.*;
import view.screen.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class CanvasRenderer {
    public static void renderCurrentScreen(Graphics2D g2, GameState state, List<String> savedProfiles,
                                           PlayerProfile profile, PartyManager party, StageManager stage,
                                           GachaService gacha, GachaHandler gachaHandler,
                                           CanvasButtonManager btn, CombatEngine combat, List<Enemy> enemies) {
        
        // Reset Clip Bounds ke Full HD Virtual Canvas (1920x1080) agar rendering tidak terpotong
        g2.setClip(new Rectangle(0, 0, GameCanvas.BASE_WIDTH, GameCanvas.BASE_HEIGHT));

        switch (state) {
            case PROFILE_SELECT: 
                ProfileSelectScreen.render(g2, savedProfiles, btn.btnNewProfile, btn.btnQuitProfile); 
                break;
            case TITLE:          
                TitleScreen.render(g2, btn.btnStart, btn.btnQuit); 
                break;
            case LOBBY:          
                LobbyScreen.render(g2, profile, party, btn.btnBattle, btn.btnGacha, btn.btnParty, btn.btnTrainHero, btn.btnSettings); 
                break;
            case GACHA:          
                GachaScreen.render(g2, profile, gacha, gachaHandler.getLastNotice(), btn.btnBack, btn.btn1x, btn.btn10x); 
                break;
            case PARTY:          
                PartyScreen.render(g2, party, profile, btn.btnBack); 
                break;
            case SETTINGS:       
                SettingsScreen.render(g2, btn.btnBack, btn.btnResStandard, btn.btnResHD, btn.btnToggleVfx, btn.btnSaveGame); 
                break;
            case BATTLE:         
                BattleScreen.render(g2, enemies, party, stage, btn.btnBack, combat.damageTexts, combat.vfxList); 
                break;
            case VICTORY:        
                ResultScreen.render(g2, true, stage, combat.victoryGems, combat.victoryCredits, btn.btnReturn); 
                break;
            case GAME_OVER:      
                ResultScreen.render(g2, false, stage, 0, 0, btn.btnReturn); 
                break;
            default: 
                break;
        }
    }
}