package plantventure;

import plantventure.game.Game;
import plantventure.graphics.Window;
import plantventure.graphics.game.font.FontRenderer;
import plantventure.utils.AssetStorage;
import plantventure.utils.LoggerUtils;
import org.lwjgl.Version;
import org.slf4j.Logger;

import java.io.File;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Plantventure implements Runnable{
    private static final Logger logger = LoggerUtils.getLogger(Plantventure.class);

    public static boolean running;


    public static int ups;

    private static long lastTime = System.nanoTime();
    private static double delta = 0.0;
    private static double ns = 1000000000.0 / 60.0;
    private static long timer = System.currentTimeMillis();
    private static int updates = 0;
    private static int frames = 0;

    public void run() {
        logger.info("Starting game...");
        logger.info("LWJGL: " + Version.getVersion() + "!");

        running = true;

        File chunks = new File("./chunks");
        for(File file : chunks.listFiles()) {
            file.delete();
        }
        boolean file = new File("./chunks").mkdir();
        Window.init();
        AssetStorage.init();
        FontRenderer.init();

        Game.init();

        lastTime = System.nanoTime();
        delta = 0.0;
        ns = 1000000000.0 / 60.0;
        timer = System.currentTimeMillis();
        updates = 0;
        frames = 0;

        Game.isTicking = true;

        while(running) {
            runGameLoop();
        }
    }

    public static void runGameLoop() {
        long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now;

        if (delta >= 1.0) {
            Window.handleMouse();

            if(Game.isTicking) {
                Game.tick();
            }
            updates++;
            delta--;
        }
        Window.render();
        frames++;
        if (System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            Window.ups = updates;
            Window.fps = frames;
            logger.info(updates + " ups, " + frames + " fps");
            updates = 0;
            frames = 0;
        }

        if(glfwWindowShouldClose(Window.window)) {
            // Free the window callbacks and destroy the window
            running = false;
            glfwFreeCallbacks(Window.window);
            glfwDestroyWindow(Window.window);

            // Terminate GLFW and free the error callback
            glfwTerminate();
        }
    }

    public void start() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public static void main(String[] args) {
        new Plantventure().start();
    }
}
