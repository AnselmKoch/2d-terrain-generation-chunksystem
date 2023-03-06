package plantventure.graphics.renderers;

import plantventure.graphics.mesh.RenderMesh;
import plantventure.graphics.shaders.Shader;

public class WorldRenderer {

    private static RenderMesh renderMesh;

    public static void init() {
        renderMesh = new RenderMesh(Shader.TILE, 5000);
    }

    public static void render() {
        renderMesh.render();
    }

    public static RenderMesh getRenderMesh() {
        return renderMesh;
    }

}

