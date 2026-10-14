package engine;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputHandler extends KeyAdapter {
    // Keyboard State
    public boolean up, down, left, right;
    public boolean skillCast, ultCast, bossChallengeKey;
    public int selectedHeroSlot = -1;
    public boolean escPressed = false;

    // Mouse State untuk UI Menu
    public Point mousePosition = new Point(0, 0);
    public boolean mouseClicked = false;

    // Mouse Listener terpisah untuk dipasang ke GameCanvas
    public MouseAdapter getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseClicked = true;
                mousePosition = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseClicked = false;
            }
        };
    }

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
        if (code == KeyEvent.VK_ESCAPE) escPressed = true;

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
        if (code == KeyEvent.VK_ESCAPE) escPressed = false;
    }
}