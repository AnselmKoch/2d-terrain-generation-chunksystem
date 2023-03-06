package plantventure.game.world.chunk;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.io.Serializable;

public class ChunkCoordinate implements Serializable {

    private final int x;
    private final int y;
    private final Vector2i mapCoordinate;

    public ChunkCoordinate(int x, int y, Vector2i mapCoordinate) {
        this.x = x;
        this.y = y;
        this.mapCoordinate = mapCoordinate;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2i getMapCoordinate() {
        return mapCoordinate;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
