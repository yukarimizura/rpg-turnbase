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
                lastTime += TARGET_TIME;
                if (now - lastTime > TARGET_TIME * 2) {
                    lastTime = now;
                }
                canvas.updateState();
                canvas.repaint();
            }

            long sleepMs = (TARGET_TIME - (System.nanoTime() - lastTime)) / 1_000_000L;
            if (sleepMs > 1) {
                try {
                    Thread.sleep(sleepMs - 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}