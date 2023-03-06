package plantventure.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import plantventure.graphics.Window;

public class Input {

    public static boolean[] pressedKeys = new boolean[] {
            false,false,false,false,false
    };

    private static GLFWMouseButtonCallback clickCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (action == GLFW.GLFW_PRESS) {

                if(Window.mousePos == null) {
                    return;
                }

                Window.handleMouseClick();
            }
        }
    };

    private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {

        //W = 87 A = 65 S=83 D=68
        //1 = 49, 2 = 50, 3 = 51, 4 =52, 5=53
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if(action == GLFW.GLFW_PRESS) {
                switch (key) {
                    case 87: pressedKeys[0] = true; break;
                    case 65: pressedKeys[1] = true;break;
                    case 83: pressedKeys[2] = true; break;
                    case 68: pressedKeys[3] = true;break;
                    case 32: Game.resetWorld();break;
                }
            }
            if(action == GLFW.GLFW_RELEASE) {
                switch (key) {
                    case 87: pressedKeys[0] = false; break;
                    case 65: pressedKeys[1] = false; break;
                    case 83: pressedKeys[2] = false; break;
                    case 68: pressedKeys[3] = false; break;
                }
            }
        }
    };

    private Input() {

    }

    public static GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    public static GLFWMouseButtonCallback getClickCallBack() {
        return clickCallback;
    }
}
