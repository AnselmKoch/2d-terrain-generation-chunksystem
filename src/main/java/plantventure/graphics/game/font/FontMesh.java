package plantventure.graphics.game.font;

import plantventure.graphics.mesh.RenderMesh;
import plantventure.graphics.shaders.Shader;

public class FontMesh extends RenderMesh {
    public FontMesh(Shader shader, int size) {
        super(shader, size);
    }

    public void flush() {
        for(int i = 0; i < this.vertices.length; i++) {
            vertices[i] = 0.0f;
        }
        this.renderables.clear();
        this.amount = 0;
    }
}
