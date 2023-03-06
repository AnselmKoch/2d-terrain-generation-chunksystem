package plantventure.game.world.tiles;

import org.joml.Vector3f;
import plantventure.graphics.game.Renderable;
import plantventure.graphics.texture.Texture;
import plantventure.utils.Position;

import java.io.Serializable;

public abstract class Tile extends Renderable implements Serializable {
    public Tile(Vector3f position, float width, float height, float size, Texture texture, Position orientation) {
        super(position, width, height, size, texture, orientation);
    }
}
