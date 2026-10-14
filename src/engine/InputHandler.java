package engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    public boolean up, down, left, right;
    public boolean skillCast, ultCast, bossChallengeKey;
    public int selectedHeroSlot = -1;

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) up = true;
        if (code == KeyEvent.VK_S) down = true;
        if (code == KeyEvent.VK_A) left = true;
        if (code == KeyEvent.VK_D) right = true;

        if (code == KeyEvent.VK_Q) skillCast = true;
        if (code == KeyEvent.VK_R) ultCast = true;
        if (code == KeyEvent.VK_B) bossChallengeKey = true;

        // Switch Hero 1 - 5
        if (code >= KeyEvent.VK_1 && code <= KeyEvent.VK_5) {
            selectedHeroSlot = code - KeyEvent.VK_1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) up = false;
        if (code == KeyEvent.VK_S) down = false;
        if (code == KeyEvent.VK_A) left = false;
        if (code == KeyEvent.VK_D) right = false;

        if (code == KeyEvent.VK_Q) skillCast = false;
        if (code == KeyEvent.VK_R) ultCast = false;
        if (code == KeyEvent.VK_B) bossChallengeKey = false;
    }
}