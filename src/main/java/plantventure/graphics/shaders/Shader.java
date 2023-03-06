package plantventure.graphics.shaders;

import plantventure.utils.LoggerUtils;
import plantventure.utils.ShaderUtils;
import plantventure.utils.buffer.BufferUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1iv;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader {
    private static final Logger logger = LoggerUtils.getLogger(Shader.class);

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;
    public static final int TEXID_ATTRIB = 2;
    public static final int COLOR_ATTRIB = 3;

    public static Shader TILE, PLAYER, STANDART, BULLET, MENU, HEALTH;

    private static FloatBuffer matrice4x4Buffer =  BufferUtils.createFloatBuffer(new float[16]);

    private boolean enabled = false;

    private Map<String, Integer> locationCache = new HashMap<String, Integer>();
    private String name;

    private int ID;

    public Shader(String shaderName) {
        this.name = shaderName;
        ID = ShaderUtils.load(shaderName);
    }

    public static void init() {
        logger.info("Loading all shaders...");
        Shader.TILE = new Shader("tile");
        Shader.PLAYER = new Shader("livingentity");
        Shader.STANDART = new Shader("standart");
        Shader.BULLET = new Shader("bullet");
        Shader.MENU = new Shader("menu");
        Shader.HEALTH = new Shader("health");
        Shader.MENU.setUniformMat4f("uModel", new Matrix4f().identity());
        Shader.STANDART.setUniformMat4f("uModel", new Matrix4f().identity());
        Shader.BULLET.setUniformMat4f("uModel", new Matrix4f().identity());
        Shader.TILE.setUniformMat4f("uModel", new Matrix4f().identity());
        Shader.PLAYER.setUniformMat4f("uModel", new Matrix4f().identity());
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }

        int result = glGetUniformLocation(ID, name);
        if (result == -1) {
        } else {
            locationCache.put(name, result);
        }

        return result;
    }

    public void setUniform1i(String name, int value) {
        if (!enabled) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if (!enabled) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform3fArray(String name, int index, Vector3f value) {
        if (!enabled) enable();
        glUniform3f(getUniform(name + "[" + index + "]"), value.x, value.y, value.z);
    }

    public void setUniform2f(String name, float x, float y) {
        if (!enabled) enable();
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform4f(String name, float x, float y, float z, float a) {
        if (!enabled) enable();
        glUniform4f(getUniform(name), x, y, z, a);
    }

    public void setUniformBool(String name, boolean value) {
        if (!enabled) enable();
        if (value) {
            glUniform1i(getUniform(name), 1);
        } else {
            glUniform1i(getUniform(name), 0);
        }
    }

    public void setUniformV2f(String name, Vector2f vector2f) {
        if (!enabled) enable();
        glUniform2f(getUniform(name), vector2f.x, vector2f.x);
    }

    public void setIntArray(String name, int[] array) {
        if (!enabled) enable();
        glUniform1iv(getUniform(name), array);
    }

    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled) enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled) enable();
        matrix.get(matrice4x4Buffer);
        glUniformMatrix4fv(getUniform(name), false, matrice4x4Buffer);
    }

    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }


    public int getID() {
        return ID;
    }
}

