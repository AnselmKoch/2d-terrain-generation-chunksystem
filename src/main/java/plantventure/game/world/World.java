package plantventure.game.world;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import plantventure.game.Game;
import plantventure.game.world.chunk.Chunk;
import plantventure.game.world.chunk.ChunkCoordinate;
import plantventure.game.world.tiles.DirtTile;
import plantventure.game.world.tiles.GrassTile;
import plantventure.game.world.tiles.StoneTile;
import plantventure.game.world.tiles.Tile;
import plantventure.game.world.tiles.WaterTile;
import plantventure.graphics.Window;
import plantventure.graphics.renderers.WorldRenderer;
import plantventure.utils.Position;
import plantventure.utils.Serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class World {
    public static int tilesXOnScreen = 20;
    public static int tilesYOnScreen = 10;

    public static int tileWidth = Window.DISPLAYWITH / tilesXOnScreen;
    public static int tileHeight = Window.DISPLAYHEIGHT / tilesYOnScreen;

    public static final ChunkCoordinate spawnChunkCoordinate = new ChunkCoordinate(0, 0, new Vector2i(0,0));

    public static final int tilesXInChunk = 10;
    public static final int tilesYInChunk = 10;


    public static final int renderDistance = 5;

    public static final int chunkWidth = tilesXInChunk *  tileWidth;
    public static final int chunkHeight = tilesYInChunk * tileHeight;

    public static final double noiseStepSize = 0.5d;
    public static final double noiseStepSizeX = noiseStepSize / tilesXInChunk;
    public static final double noiseStepSizeY = noiseStepSize / tilesYInChunk;

    public static int seed;

    private final Chunk spawnChunk;

    private HashMap<String, Chunk> loadedChunks;
    private HashMap<Chunk, Tile[][]> loadedTiles;

    private ChunkCoordinate lastBaseChunkCoordinate;

    private final Queue<ChunkCoordinate> chunksToGenerate;
    private final Queue<Chunk> chunksToDelete;

    private int tickCounter = 0;

    public World() {
        seed = new Random().nextInt(5000000);
        chunksToGenerate = new LinkedList<>();
        chunksToDelete = new LinkedList<>();
        loadedChunks = new HashMap<>();
        loadedTiles = new HashMap<>();
        spawnChunk = new Chunk(spawnChunkCoordinate);
        spawnChunk.generateTiles();
        HashMap<Chunk, Tile[][]> temp = new HashMap();
        Tile[][] tiles = this.getChunkTiles(spawnChunk);

        temp.put(spawnChunk, tiles);
        loadedChunks.put(spawnChunkCoordinate.toString(), spawnChunk);
        loadedTiles.put(spawnChunk, tiles);


        this.lastBaseChunkCoordinate = spawnChunkCoordinate;
        this.generateChunks(spawnChunk);
    }

    /**
     * Works on the queue to load and unload chunks so all the work is split along ticks
     */
    public void tick() {
        tickCounter++;
        if (chunksToGenerate.size() != 0 && tickCounter % 3 == 0) {
            ChunkCoordinate generateChunk = chunksToGenerate.poll();
            Chunk tempChunk = this.loadChunk(generateChunk);
            Tile[][] tiles = this.getChunkTiles(tempChunk);
            HashMap<Chunk, Tile[][]> temp = new HashMap<>();
            temp.put(tempChunk, tiles);
            loadedChunks.put(tempChunk.getChunkCoordinate().toString(), tempChunk);
            loadedTiles.put(tempChunk, tiles);
        }
        if (chunksToDelete.size() != 0 && tickCounter % 2 == 0) {
            Chunk deleteChunk = chunksToDelete.poll();
            destroyChunk(deleteChunk);
            try {
                Serializer.deserializeChunk(deleteChunk);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates chunk via chunk coordinates based on the players position
     * @param baseChunk
     */
    private void generateChunks(Chunk baseChunk) {
        if (baseChunk == null) {
            return;
        }
        int baseChunkX = baseChunk.getChunkCoordinate().getMapCoordinate().x;
        int baseChunkY = baseChunk.getChunkCoordinate().getMapCoordinate().y;

        int baseChunkXCoord = baseChunk.getChunkCoordinate().getX();
        int baseChunkYCoord = baseChunk.getChunkCoordinate().getY();

        Chunk tempChunk;
        ChunkCoordinate tempChunkCoordinate;
        // Topleft C
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord - 1, baseChunkYCoord + 1,
                new Vector2i(baseChunkX - chunkWidth, baseChunkY + chunkHeight));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            chunksToGenerate.add(tempChunkCoordinate);
        }

        // Topmid Chunk
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord, baseChunkYCoord + 1, new Vector2i(baseChunkX, baseChunkY + chunkHeight));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            /*
            tempChunk = this.loadChunk(tempChunkCoordinate);
            loadedChunks.put(tempChunk.getChunkCoordinate().toString(), tempChunk);

             */
            chunksToGenerate.add(tempChunkCoordinate);
        }

        // Topright Chunk
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord + 1, baseChunkYCoord + 1, new Vector2i(baseChunkX + chunkWidth, baseChunkY + chunkHeight));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            chunksToGenerate.add(tempChunkCoordinate);
        }

        // Midleft Chunk
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord - 1, baseChunkYCoord, new Vector2i(baseChunkX - chunkWidth, baseChunkY));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            chunksToGenerate.add(tempChunkCoordinate);
        }

        // Midright Chunk
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord + 1, baseChunkYCoord, new Vector2i(baseChunkX + chunkWidth, baseChunkY));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            chunksToGenerate.add(tempChunkCoordinate);
        }


        // Bottomleft Chunk
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord - 1, baseChunkYCoord - 1, new Vector2i(baseChunkX - chunkWidth, baseChunkY - chunkHeight));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            chunksToGenerate.add(tempChunkCoordinate);
        }


        // Bottommid Chunk
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord, baseChunkYCoord - 1, new Vector2i(baseChunkX, baseChunkY - chunkHeight));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            chunksToGenerate.add(tempChunkCoordinate);
        }

        // Bottomright Chunk
        tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord + 1,baseChunkYCoord - 1, new Vector2i(baseChunkX + chunkWidth, baseChunkY - chunkHeight));
        if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
            chunksToGenerate.add(tempChunkCoordinate);
        }

        for (int i = 0; i < 3; i++) {
            tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord - 2, baseChunkYCoord - 1 + i, new Vector2i(baseChunkX - chunkWidth * 2, baseChunkY + chunkHeight * (-1 + i)));
            if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
                chunksToGenerate.add(tempChunkCoordinate);
            }
        }
        for (int i = 0; i < 3; i++) {
            tempChunkCoordinate = new ChunkCoordinate(baseChunkXCoord + 2, baseChunkYCoord - 1 + i, new Vector2i(baseChunkX + chunkWidth * 2, baseChunkY + chunkHeight * (-1 + i)));
            if (loadedChunks.get(tempChunkCoordinate.toString()) == null) {
                chunksToGenerate.add(tempChunkCoordinate);
            }
        }
    }


    /**
     * Loads a chunk from a chunk-file
     * @param chunkCoordinate
     * @return
     */
    private Chunk loadChunk(ChunkCoordinate chunkCoordinate) {
        Chunk readChunk = null;
        try {
            readChunk = Serializer.readChunk(chunkCoordinate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (readChunk == null) {
            readChunk = new Chunk(chunkCoordinate);
            readChunk.generateTiles();
        }
        return readChunk;
    }

    /**
     * Checks if player is in a new chunk and which chunk should be loaded / unloaded
     */
    public void handlePlayerMove() {
        int playerX = (int)Game.player.getPosition().x;
        int playerY = (int)Game.player.getPosition().y;
        int currentChunkX = playerX / chunkWidth;
        int currentChunkY = playerY / chunkHeight;
        int test = chunkWidth;
        int test1 = chunkHeight;
        if (this.lastBaseChunkCoordinate.getX() != currentChunkX || this.lastBaseChunkCoordinate.getY() != currentChunkY) {
            Chunk foundChunk = loadedChunks.get((currentChunkX) + "," + currentChunkY);
            if (foundChunk == null) {
                chunksToGenerate.add(new ChunkCoordinate(currentChunkX, currentChunkY, new Vector2i(currentChunkX * chunkWidth, currentChunkY * chunkHeight)));
                return;
            }
            this.generateChunks(foundChunk);
            this.lastBaseChunkCoordinate = foundChunk.getChunkCoordinate();
            this.isChunkOutOfSight(spawnChunk);

            for (Chunk chunk : loadedChunks.values()) {
                if (this.isChunkOutOfSight(chunk)) {
                    chunksToDelete.add(chunk);
                }
            }
        }

    }

    /**
     * Generates Tiles for Chunk based on the value in the chunk 2d Array
     * @param chunk
     * @return
     */
    private Tile[][] getChunkTiles(Chunk chunk) {
        Tile[][] tiles = new Tile[tilesXInChunk][tilesYInChunk];
        double[][] noiseValues = chunk.getTiles();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                double noiseValue = noiseValues[x][y];
                float currentPositionX = chunk.startPositionX + (World.tileWidth * x);
                float currentPositionY = chunk.startPositionY + (World.tileHeight * y);
                Tile tile;
                if (noiseValue > 0.5) {
                    tile = new GrassTile(new Vector3f(currentPositionX, currentPositionY, 0.0f), World.tileWidth, World.tileHeight, 1.0f, Position.BOTTOMLEFT);
                } else if ( noiseValue < 0.5 && noiseValue > 0.1) {
                    tile = new DirtTile(new Vector3f(currentPositionX, currentPositionY, 0.0f), World.tileWidth, World.tileHeight, 1.0f, Position.BOTTOMLEFT);
                } else if (noiseValue < 0.1 && noiseValue > -0.4) {
                    tile = new StoneTile(new Vector3f(currentPositionX, currentPositionY, 0.0f), World.tileWidth, World.tileHeight, 1.0f, Position.BOTTOMLEFT);
                } else {
                    tile = new WaterTile(new Vector3f(currentPositionX, currentPositionY, 0.0f), World.tileWidth, World.tileHeight, 1.0f, Position.BOTTOMLEFT);
                }
                tiles[x][y] = tile;
                WorldRenderer.getRenderMesh().addRenderable(tile);
            }
        }
        return tiles;
    }

    /**
     * Unloads chunk, saves it to the chunk-file and removes Tiles from the WorldRenderer
     * @param chunk
     */
    private void destroyChunk(Chunk chunk) {
        loadedChunks.remove(chunk.getChunkCoordinate().toString());
        Tile[][] tiles = loadedTiles.get(chunk);
        loadedTiles.remove(chunk);
        if (tiles == null) {
            return;
        }
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                WorldRenderer.getRenderMesh().removeRenderable(tiles[x][y]);
            }
        }
    }

    /**
     * Calculates if the chunk is out of sight to unload it
     * @param chunk
     * @return
     */
    private boolean isChunkOutOfSight(Chunk chunk) {
        ChunkCoordinate chunkCoordinate = chunk.getChunkCoordinate();
        Vector2i chunkCoord = new Vector2i(chunkCoordinate.getMapCoordinate().x, chunkCoordinate.getMapCoordinate().y);
        Vector2i playerCoord = new Vector2i((int)Game.player.getPosition().x, (int)Game.player.getPosition().y);
        int distance = (int)chunkCoord.distance(playerCoord);
        return distance > 300;
    }
    public Chunk getSpawnChunk() {
        return spawnChunk;
    }
}
