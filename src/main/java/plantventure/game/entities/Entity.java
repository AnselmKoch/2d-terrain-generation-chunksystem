package plantventure.game.entities;

import plantventure.game.entities.player.Player;
import plantventure.graphics.game.Renderable;
import plantventure.graphics.texture.Texture;
import plantventure.utils.LoggerUtils;
import plantventure.utils.Position;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Entity extends Renderable {
    private static final Logger logger = LoggerUtils.getLogger(Entity.class);


    private Vector2f momentum;

    private float damage, speed, shotspeed, cooldown;

    public static final float cooldownPerTick = 1.0f / 60.0f;
    private boolean doDamangeAnimation;
    private boolean isInvincible;
    private int invincTime;
    private boolean shieldActive;
    private int crtInvincTime;
    private int health;
    public static final int MAX_HEALTH = 10;
    private float currentCooldown = 0.0f;


    private Vector3f healthBarPos;

    private Texture[] textures;

    private boolean doTransparencyOnDamage;
    private float damageFrame;
    private int animationDelay;
    private int animationIndex;
    private int animationAmount;

    public Entity(Vector3f position, float width, float height, float size, Texture texture, Position center, boolean transparency, int health) {
        super(position, width, height, size, texture, center);
        this.momentum = new Vector2f(0.0f,0.0f);
        this.doDamangeAnimation = false;
        this.isInvincible = false;
        this.invincTime = 60;
        this.crtInvincTime = 0;
        this.doTransparencyOnDamage = transparency;
        this.health = health;

        this.shieldActive = false;
        if(this instanceof Player) {
            return;
        }

    }

    public abstract void onRender();

    public abstract void move(Vector2f momentum);

    public abstract void tick();

    public Vector2f getMomentum() {
        return this.momentum;
    }

    public void setMomentum(Vector2f momentum) {
        if(this.speed != 0.0f) {
            this.momentum.add(momentum).normalize(this.speed);
        }else{
            this.momentum.add(momentum).normalize(this.shotspeed);
        }
    }

    public void doAnimation() {
        if(this.textures == null) {
            return;
        }

        this.animationIndex ++;
        this.setTexture(textures[animationIndex / animationDelay]);

        if(animationIndex >= animationDelay * (animationAmount) -1) {
            this.animationIndex = 0;
        }

    }

    public void setMomentumTotal(Vector2f momentum) {
        this.momentum = momentum;
    }

    public void processTick() {
    }

    public boolean checkIfOutOfBounds() {
        if(this.getPosition().x > 400) {
            return true;
        }
        if(this.getPosition().x < 0) {
            return true;
        }

        if(this.getPosition().y < 0) {
            return true;
        }

        if(this.getPosition().y > 200) {
            return true;
        }

        return false;
    }


    public void doDamageColor() {
        if(doDamangeAnimation) {

            this.setDamageFrame(this.getDamageFrame() + 0.05f);
            if(getDamageFrame() > 1.0f) {
                this.doDamangeAnimation = false;
            }
        }else{
            if(this.getDamageFrame() > 0.05f) {
                this.setDamageFrame(this.getDamageFrame() - 0.05f);
            }
        }

        Vector4f color;
        if(this.doTransparencyOnDamage) {
            color = new Vector4f(1.0f, 1.0f - this.getDamageFrame(), 1.0f - this.getDamageFrame(), 1.0f - this.getDamageFrame());
        }else{
            color = new Vector4f(1.0f, 1.0f - this.getDamageFrame(),1.0f - this.getDamageFrame(), 1.0f);
        }
        this.setColor(color);
    }

    public void doCollition(float strength) {

    }



    public abstract void die();

    public float getDamageFrame() {
        return damageFrame;
    }

    public void setDamageFrame(float damageFrame) {
        this.damageFrame = damageFrame;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getShotspeed() {
        return shotspeed;
    }

    public void setShotspeed(float shotspeed) {
        this.shotspeed = shotspeed;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public void setInvincTime(int time) {
        this.invincTime = time;
    }

    public int getInvincTime() {
        return invincTime;
    }

    public int getCrtInvincTime() {
        return crtInvincTime;
    }

    public void setCrtInvincTime(int crtInvincTime) {
        this.crtInvincTime = crtInvincTime;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public static Entity createInstance(Class clazz, Vector3f pos) {

        try {
            Constructor constructor = clazz.getConstructor(Vector3f.class);
            try {
               return (Entity)constructor.newInstance(pos);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }


    public float getCurrentCooldown() {
        return currentCooldown;
    }

    public void setCurrentCooldown(float currentCooldown) {
        this.currentCooldown = currentCooldown;
    }


    public Vector3f getHealthBarPos() {
        return healthBarPos;
    }

    public void setHealthBarPos(Vector3f healthBarPos) {
        this.healthBarPos = healthBarPos;
    }

    public Texture[] getTextures() {
        return textures;
    }

    public void setTextures(Texture[] textures) {
        this.textures = textures;
        this.animationAmount = textures.length;
        this.animationIndex = 0;
    }

    public int getAnimationDelay() {
        return animationDelay;
    }

    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = animationDelay;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public void setShieldActive(boolean shieldActive) {
        this.shieldActive = shieldActive;
    }
}
