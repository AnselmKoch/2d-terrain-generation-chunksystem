package plantventure.game.world.tiles;

import org.joml.Vector3f;
import plantventure.graphics.texture.Texture;
import plantventure.utils.AssetStorage;
import plantventure.utils.Position;

import java.io.Serializable;

public class GrassTile extends Tile implements Serializable {
    public GrassTile(Vector3f position, float width, float height, float size, Position orientation) {
        super(position, width, height, size, AssetStorage.getTexture("grass"), orientation);
    }
}
