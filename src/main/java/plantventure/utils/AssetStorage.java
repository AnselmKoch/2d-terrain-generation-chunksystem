package plantventure.utils;

import plantventure.graphics.texture.Texture;
import org.slf4j.Logger;

import java.util.HashMap;

public class AssetStorage {
    private static final Logger logger = LoggerUtils.getLogger(AssetStorage.class);

    private static HashMap<String, Texture> textureHashMap = new HashMap<>();

    public static void init() {
        textureHashMap.put("empty", new Texture("empty"));
        textureHashMap.put("basicbullet", new Texture("player/bullets/basicBullet"));
        textureHashMap.put("stonebullet", new Texture("player/bullets/stonebullet"));
        textureHashMap.put("player0", new Texture("player/player"));
        textureHashMap.put("player1", new Texture("player/player1"));
        textureHashMap.put("player2", new Texture("player/player2"));
        textureHashMap.put("grass", new Texture("tiles/grass"));
        textureHashMap.put("dirt", new Texture("tiles/dirt"));
        textureHashMap.put("itemContainer".toLowerCase(), new Texture("HUD/ItemContainer"));
        textureHashMap.put("zombie", new Texture("enemy/zombie"));
        textureHashMap.put("zombie1", new Texture("enemy/zombie1"));
        textureHashMap.put("zombie2", new Texture("enemy/zombie2"));
        textureHashMap.put("shovel", new Texture("tiles/shovel"));
        textureHashMap.put("arrow", new Texture("util/arrow"));
        textureHashMap.put("arrowup", new Texture("util/arrowup"));
        textureHashMap.put("fullheart", new Texture("player/heart/fullheart"));
        textureHashMap.put("emptyheart", new Texture("player/heart/emptyheart"));
        textureHashMap.put("halfheart", new Texture("player/heart/halfheart"));
        textureHashMap.put("background", new Texture("util/background"));
        textureHashMap.put("button", new Texture("util/button"));
        textureHashMap.put("puddle", new Texture("tiles/puddle"));
        textureHashMap.put("waterbullet", new Texture("player/bullets/waterBullet"));
        textureHashMap.put("basicitem", new Texture("powerups/basicItem"));
        textureHashMap.put("stone", new Texture("tiles/stone"));
        textureHashMap.put("poison", new Texture("tiles/poison"));
        textureHashMap.put("treasure", new Texture("tiles/treasure"));
        textureHashMap.put("poisonbullet", new Texture("player/bullets/poisonBullet"));
        textureHashMap.put("beetle", new Texture("enemy/beetle"));
        textureHashMap.put("igel", new Texture("enemy/igel"));
        textureHashMap.put("igelproj", new Texture("enemy/igel/igelprojectile"));
        textureHashMap.put("bat", new Texture("enemy/bat"));
        textureHashMap.put("bat1", new Texture("enemy/bat1"));
        textureHashMap.put("bat2", new Texture("enemy/bat2"));
        textureHashMap.put("beetle1", new Texture("enemy/beetle1"));
        textureHashMap.put("beetle2", new Texture("enemy/beetle2"));
        textureHashMap.put("brain", new Texture("enemy/brain"));
        textureHashMap.put("shield", new Texture("util/shield"));

    }

    public static void addToHashmap(String key, Texture texture) {
        textureHashMap.put(key.toLowerCase(), texture);
    }

    public static Texture getTexture(String key) {
        return textureHashMap.get(key.toLowerCase());
    }
}
