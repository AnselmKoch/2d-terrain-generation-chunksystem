package plantventure.utils.font;

import plantventure.graphics.game.Renderable;
import plantventure.graphics.texture.Texture;
import plantventure.utils.Position;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RenderChar extends Renderable {

    private final char renderChar;

    public RenderChar(char renderChar, Vector3f position, Vector2f[] texCords, float width, float height, float size, Texture texture, Position orientation, Vector4f color) {
        super(position, texCords, width, height, size, texture, orientation);
        this.setColor(color);
        this.renderChar = renderChar;
    }
}
