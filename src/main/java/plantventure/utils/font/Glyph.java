package plantventure.utils.font;

public class Glyph {

    public float width;
    public float height;
    public final float x;
    public final float y;
    public final float xOffset;
    public final float yOffset;
    /**
     * Creates a font Glyph.
     *
     * @param width   Width of the Glyph
     * @param height  Height of the Glyph
     * @param x       X coordinate on the font texture
     * @param y       Y coordinate on the font texture
     */
    public Glyph(float x, float y, float width, float height, float xOffset, float yOffset) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.yOffset = yOffset;
        this.xOffset = xOffset;
    }
}