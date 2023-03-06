package plantventure.graphics;

import plantventure.Plantventure;
import plantventure.game.Game;
import plantventure.game.Input;
import plantventure.game.world.World;
import plantventure.graphics.renderers.EntityRenderer;
import plantventure.graphics.renderers.WorldRenderer;
import plantventure.graphics.shaders.Shader;
import plantventure.utils.LoggerUtils;
import plantventure.utils.ResizeCallback;
import plantventure.utils.font.TextFont;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.slf4j.Logger;

import java.awt.*;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final Logger logger = LoggerUtils.getLogger(Window.class);
    public static long window;
    private static GLFWVidMode glfwGetVideoMode;
    public static int WIDTH = 1060, HEIGHT = 800, TARGETWITDTH = 1920, TARGETHEIGHT = 1020;
    public static float aspectRatio;
    public static float targetAspectRatio;
    public static float minX = -100.0f * aspectRatio;
    public static final int DISPLAYWITH = 400, DISPLAYHEIGHT = 200;
    public static final int orthoMinX = 0, orthoMinY = 0, orthoMaxY = 200, orthoMaxX = 400;

    public static Matrix4f perspective;
    public static Matrix4f view = new Matrix4f().identity();


    public static int fps, ups;
    private static TextFont textFont;

    public static Vector2f mousePos;
    private static DoubleBuffer posX = BufferUtils.createDoubleBuffer(1), posY = BufferUtils.createDoubleBuffer(1);

    public static void render() {
         perspective = new Matrix4f().ortho(orthoMinX,
                orthoMaxX , orthoMinY,
               orthoMaxY, 100.0f, -100.0f);
         Vector3f cameraUp = new Vector3f(0.0f, 2.0f, 0.0f);
         view = new Matrix4f().lookAt(new Vector3f(Game.player.getPosition().x - (float)(DISPLAYWITH / 2), Game.player.getPosition().y - (float) (DISPLAYHEIGHT / 2), 20.0f),
                 new Vector3f(Game.player.getPosition().x - (float)(DISPLAYWITH / 2), Game.player.getPosition().y  - (float) (DISPLAYHEIGHT / 2), 1.0f),
                 cameraUp);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        WorldRenderer.render();
        EntityRenderer.render();

        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public static void init() {
        logger.info("Initializing window...");
        if(!glfwInit()) {
            logger.info("Could not initialize GLFW");
        }
        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Window.WIDTH = (int)screenSize.getWidth();
        Window.HEIGHT = (int)screenSize.getHeight();
        window = glfwCreateWindow(WIDTH,HEIGHT - 100, "v0.01", NULL, NULL);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        if (window == NULL) {
            System.err.println("Could not create window...");
            return;
        }
        glfwSetKeyCallback(window, Input.getKeyCallback());
        glfwSetMouseButtonCallback(window, Input.getClickCallBack());

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();

        glfwSetWindowSizeCallback(window, ResizeCallback::resizeCallback);
        glfwSetFramebufferSizeCallback(window, ResizeCallback::frameResizeCallback);
        glfwSetWindowPosCallback(window, ResizeCallback::windowPosCallback);
        glClearDepth(1.0f);

        glfwGetVideoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        TARGETHEIGHT = glfwGetVideoMode.height();
        TARGETWITDTH = glfwGetVideoMode.width();
        aspectRatio = (float)TARGETWITDTH/ (float)TARGETHEIGHT;
        logger.info(String.valueOf(TARGETHEIGHT));
        logger.info(String.valueOf(TARGETWITDTH));
        targetAspectRatio = aspectRatio;

        Shader.init();
        EntityRenderer.init();
        WorldRenderer.init();


        glDepthFunc(GL_LEQUAL);
        glActiveTexture(GL_TEXTURE1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        logger.info("OpenGL: " + glGetString(GL_VERSION) + "...");

        glViewport(0,0,WIDTH, HEIGHT);
    }


    public static void handleMouse() {
        GLFW.glfwGetCursorPos(Window.window, posX,posY);
        mousePos = new Vector2f((float)posX.get(0), Window.HEIGHT - (float)posY.get(0));

    }

    public static void handleMouseClick() {

        Vector3f world = new Matrix4f(Window.view).mul(Window.perspective).unproject(Window.mousePos.x, Window.mousePos.y, 0.0f,
                new int[]{0, 0, Window.TARGETWITDTH, Window.TARGETHEIGHT}, new Vector3f());
    }

}
