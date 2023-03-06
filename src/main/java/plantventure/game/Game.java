package plantventure.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import plantventure.game.entities.player.Player;
import plantventure.game.world.World;
import plantventure.graphics.Window;
import plantventure.graphics.renderers.EntityRenderer;
import plantventure.graphics.renderers.WorldRenderer;

public class Game{

    public static Player player;
    public static boolean isTicking = false;

    public static World world;

    public static void init() {
        player = new Player(new Vector3f(0.0f , 0.0f, 1.0f));
        EntityRenderer.getRenderMesh().addRenderable(player);
        world = new World();
    }

    public static void tick() {
        movePlayer();
        world.handlePlayerMove();
        world.tick();
        player.tick();
    }

    public static void resetWorld() {
        WorldRenderer.getRenderMesh().clear();
        world = new World();
    }

    private static void movePlayer() {
        if(Input.pressedKeys[0]) {
            player.move(new Vector2f(0.0f,0.25f));
        }
        if(Input.pressedKeys[1]) {
            player.move(new Vector2f(-0.25f,0.0f));
        }
        if(Input.pressedKeys[2]) {
            player.move(new Vector2f(0.0f,-0.25f));
        }
        if(Input.pressedKeys[3]) {
            player.move(new Vector2f(0.25f, 0.0f));
        }
    }
}
