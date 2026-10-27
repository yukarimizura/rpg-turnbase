import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Toolkit;

import engine.GameCanvas;
import engine.GameLoop;
import engine.InputHandler;
import model.characters.Attacker;
import model.characters.Character;
import service.GachaService;
import service.PartyManager;
import service.PlayerProfile;
import service.StageManager;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerProfile profile = new PlayerProfile("Commander");
            PartyManager party = new PartyManager();
            StageManager stage = new StageManager();
            GachaService gacha = new GachaService();
            InputHandler input = new InputHandler();

            Character starterVanguard = new Attacker(
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
            
            // 1. Hilangkan baris title bar & border bawaan OS
            window.setUndecorated(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // 2. Pasang canvas ke dalam window
            window.setContentPane(canvas);

            // 3. Maksimalkan window ke ukuran layar monitor penuh
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            window.setSize(screenSize);
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setLocationRelativeTo(null);
            
            window.setVisible(true);

            loop.start();
        });
    }
}