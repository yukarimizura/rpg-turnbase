package engine;

public class GameLoop implements Runnable {
    private GameCanvas canvas;
    private volatile boolean running;
    private final int FPS = 60;
    private final long TARGET_TIME = 1_000_000_000L / FPS;

    public GameLoop(GameCanvas canvas) {
        this.canvas = canvas;
        this.running = false;
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        Thread gameThread = new Thread(this, "GameLoopThread");
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long elapsed = now - lastTime;

            if (elapsed >= TARGET_TIME) {
                lastTime = now;
                canvas.updateState();
                canvas.repaint();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}