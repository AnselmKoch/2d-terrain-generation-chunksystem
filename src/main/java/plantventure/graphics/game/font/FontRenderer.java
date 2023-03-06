package plantventure.graphics.game.font;

import plantventure.graphics.shaders.Shader;
import plantventure.utils.font.RenderChar;
import plantventure.utils.font.TextFont;

import java.util.HashMap;

public class FontRenderer {

    private static FontMesh renderMesh;
    public static TextFont textFont;
    private static HashMap cachedTexts;

    public static void render() {
        renderMesh.render();
        renderMesh.clear();

    }

    public static void init() {
        renderMesh = new FontMesh(Shader.STANDART, 500);
        textFont = new TextFont("arial");
        cachedTexts = new HashMap<String, RenderChar[]>();
    }

    public static void addText(RenderChar[] renderChars) {
        for (RenderChar renderChar : renderChars) {
            if (renderChar == null) {
                continue;
            }
            try {
                renderMesh.addRenderable(renderChar);

            } catch (Exception e) {
                renderMesh.clear();
                renderMesh.addRenderable(renderChar);
            }
        }
    }
}
