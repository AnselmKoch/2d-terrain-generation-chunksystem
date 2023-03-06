package plantventure.utils;

import plantventure.game.world.chunk.Chunk;
import plantventure.game.world.chunk.ChunkCoordinate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

    public static void deserializeChunk(Chunk chunk) throws IOException {
        File file = new File("./chunks/" + chunk.getChunkCoordinate().toString() + ".ck");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(chunk);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static Chunk readChunk(ChunkCoordinate chunkCoordinate) throws IOException, ClassNotFoundException {
        File file = new File("./chunks/" + chunkCoordinate.toString() + ".ck");
        if (!file.exists()) {
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Chunk chunk = (Chunk)objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return chunk;
    }
 }
