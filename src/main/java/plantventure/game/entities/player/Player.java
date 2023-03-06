package plantventure.game.entities.player;

import plantventure.game.Input;
import plantventure.game.entities.Entity;
import plantventure.graphics.renderers.EntityRenderer;
import plantventure.graphics.texture.Texture;
import plantventure.utils.AssetStorage;
import plantventure.utils.LoggerUtils;
import plantventure.utils.Position;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.slf4j.Logger;

public class Player extends Entity {
    private static final Logger logger = LoggerUtils.getLogger(Player.class);
    private Class currentBullet;
    private int currentTileY, currentTileX;

    public Player(Vector3f position) {
        super(position, 10.0f, 10.0f, 1.0f, AssetStorage.getTexture("player0"), Position.CENTER, true, MAX_HEALTH);
        this.setHealth(MAX_HEALTH);

        this.setCooldown(0.1f);
        this.setDamage(1.0f);
        this.setShotspeed(3.0f);
        this.setSpeed(6.0f);

        this.setAnimationDelay(50);


        this.setTextures(new Texture[]{
                AssetStorage.getTexture("player0"),   AssetStorage.getTexture("player1"),  AssetStorage.getTexture("player2"),
        });
    }

    @Override
    public void onRender() {

    }



    @Override
    public void move(Vector2f momentum) {
        this.addToPosition(momentum.mul(this.getSpeed()), 0.0f);

        this.setMomentumTotal(momentum);
        if(momentum.x < 0) {
            this.rotateY(160);
        }else{
            this.rotateY(0);
        }
        EntityRenderer.getRenderMesh().changeRenderable(this);
    }

    public void resetPosition() {
        this.setPosition(new Vector3f(200, 100, 1.0f));
    }

    @Override
    public void tick() {
    }

    @Override
    public void die() {
    }


    public Class getCurrentBullet() {
        return this.currentBullet;
    }


}
