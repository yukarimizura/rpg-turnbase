import engine.GameCanvas;
import engine.GameLoop;
import engine.InputHandler;
import model.Character;
import service.GachaService;
import service.PartyManager;
import service.PlayerProfile;
import service.StageManager;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cyber-Chrono ARPG Gacha (Java 11 Engine)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            // Inisialisasi Layanan Game & Profil Pemain
            PlayerProfile profile = new PlayerProfile("PlayerOne");
            PartyManager partyManager = new PartyManager();
            StageManager stageManager = new StageManager();
            GachaService gachaService = new GachaService();
            InputHandler input = new InputHandler();

            // Beri 1 Hero Starter
            Character starterHero = gachaService.roll();
            profile.addHero(starterHero);
            partyManager.addHeroToParty(starterHero);

            // Inisialisasi Canvas Mandiri (Tanpa tombol Swing tempelan)
            GameCanvas canvas = new GameCanvas(partyManager, stageManager, profile, gachaService, input);
            GameLoop loop = new GameLoop(canvas);

            frame.add(canvas);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            canvas.requestFocusInWindow();
            loop.start();
        });
    }
}