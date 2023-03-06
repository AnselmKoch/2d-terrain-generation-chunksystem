package plantventure.game.world.chunk;

import org.joml.Vector3f;

import plantventure.game.world.World;
import plantventure.game.world.tiles.DirtTile;
import plantventure.game.world.tiles.GrassTile;
import plantventure.game.world.tiles.StoneTile;
import plantventure.game.world.tiles.Tile;
import plantventure.game.world.tiles.WaterTile;
import plantventure.graphics.renderers.WorldRenderer;
import plantventure.utils.Position;
import plantventure.utils.SimplexNoise;

import java.io.Serializable;

public class Chunk implements Serializable {

    private final ChunkCoordinate chunkCoordinate;
    private final double[][] tiles;
    public int startPositionX, startPositionY;

    public Chunk(ChunkCoordinate chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
        tiles = new double[World.tilesXInChunk][World.tilesYInChunk];
        startPositionX = this.chunkCoordinate.getMapCoordinate().x - ((World.tilesXInChunk / 2) * World.tileWidth);
        startPositionY = this.chunkCoordinate.getMapCoordinate().y - ((World.tilesYInChunk / 2) * World.tileHeight);
    }

    public void generateTiles() {
        double noiseXPos, noiseYPos;
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                noiseXPos = (chunkCoordinate.getX() * World.noiseStepSize) + (x * World.noiseStepSizeX);
                noiseYPos = (chunkCoordinate.getY() * World.noiseStepSize) + (y * World.noiseStepSizeY);
                double noiseValue = SimplexNoise.noise(noiseXPos, noiseYPos, World.seed);
                tiles[x][y] = noiseValue;
            }
        }
    };



    public ChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public double[][] getTiles() {
        return tiles;
    }
}
