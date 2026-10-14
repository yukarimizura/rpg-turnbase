import engine.GameCanvas;
import engine.GameLoop;
import engine.InputHandler;
import model.Character;
import service.GachaService;
import service.PartyManager;
import service.StageManager;

import javax.swing.*;
import java.awt.BorderLayout;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cyber-Chrono ARPG Gacha (Java 11 Engine)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            PartyManager partyManager = new PartyManager();
            StageManager stageManager = new StageManager();
            GachaService gachaService = new GachaService();
            InputHandler input = new InputHandler();

            // Beri 1 Hero Starter
            Character starter = gachaService.roll();
            partyManager.addHeroToParty(starter);

            // Inisialisasi Canvas
            GameCanvas canvas = new GameCanvas(partyManager, stageManager, input);
            GameLoop loop = new GameLoop(canvas);

            // Panel Atas: Tombol Summon Gacha
            JPanel topBar = new JPanel();
            JButton btnGacha = new JButton("Summon Hero (Add to Party)");
            btnGacha.setFocusable(false); // Hindari hilangnya fokus keyboard dari canvas

            btnGacha.addActionListener(e -> {
                if (partyManager.isPartyFull()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Party sudah penuh (5 Hero)! Syarat tantang boss sudah terpenuhi.", 
                        "Party Full", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Character summoned = gachaService.roll();
                    partyManager.addHeroToParty(summoned);
                    JOptionPane.showMessageDialog(frame, 
                        "Berhasil Summon: " + summoned.getName() + " [" + summoned.getRarity() + " " + summoned.getRole() + "]!\n"
                        + "Slot Party: " + partyManager.getActiveParty().size() + "/5", 
                        "Summon Result", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                canvas.requestFocusInWindow(); // Kembalikan fokus input keyboard ke WASD
            });

            topBar.add(btnGacha);

            frame.setLayout(new BorderLayout());
            frame.add(topBar, BorderLayout.NORTH);
            frame.add(canvas, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            canvas.requestFocusInWindow();
            loop.start();
        });
    }
}