import engine.GameCanvas;
import engine.GameLoop;
import engine.InputHandler;
import model.Attacker;
import service.GachaService;
import service.PartyManager;
import service.PlayerProfile;
import service.StageManager;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerProfile profile = new PlayerProfile("Commander");
            PartyManager party = new PartyManager();
            StageManager stage = new StageManager();
            GachaService gacha = new GachaService();
            InputHandler input = new InputHandler();

            // GUARANTEED STARTER: 1 Vanguard Attacker (Blade Dancer)
            Attacker starterVanguard = new Attacker(
                "HERO_STARTER", 
                "Dawn Blade Dancer", 
                "SR", 
                "Blade Dancer", 
                1600, 
                180, 
                50
            );
            profile.addHero(starterVanguard);
            party.addHeroToParty(starterVanguard);

            GameCanvas canvas = new GameCanvas(party, stage, profile, gacha, input);
            GameLoop loop = new GameLoop(canvas);

            JFrame window = new JFrame("Cyber-Chrono ARPG Engine");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(true);
            window.setContentPane(canvas);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            loop.start();
        });
    }
}