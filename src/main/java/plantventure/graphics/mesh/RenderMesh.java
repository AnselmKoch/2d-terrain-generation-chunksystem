package plantventure.graphics.mesh;

import plantventure.graphics.Window;
import plantventure.graphics.game.Renderable;
import plantventure.graphics.shaders.Shader;
import plantventure.graphics.texture.Texture;
import plantventure.utils.LoggerUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderMesh {
    private static final Logger logger = LoggerUtils.getLogger(RenderMesh.class);

    private static final int POS_SIZE = 3;
    private static final int TEX_COORDS_SIZE = 2;
    private static final int TEX_ID_SIZE = 1;
    private static final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int TEX_COORDS_OFFSET = 3 * Float.BYTES;
    private final int TEX_ID_OFFSET = 5 * Float.BYTES;
    private final int COLOR_OFFSET = 6 * Float.BYTES;
    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final int meshSize;
    private Shader shader;
    public int amount;
    public float[] vertices;
    private int[] textureSlots = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
    private List<String> freeSlots;
    private List<Texture> textureList;
    public Map<Renderable, MeshProperty> renderables;
    private int vaoId, vboId;

    public RenderMesh(Shader shader, int size) {
        this.meshSize = size;
        this.shader = shader;
        this.renderables = new HashMap<>();
        this.vertices = new float[meshSize * 4 * VERTEX_SIZE];
        this.amount = 0;
        this.textureList = new ArrayList<>();
        freeSlots = new ArrayList<>();

        for(int i = 0; i < meshSize; i++) {
            freeSlots.add(String.valueOf(i));
        }

        this.start();
    }

    public void start() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = createIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(Shader.VERTEX_ATTRIB, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

        glVertexAttribPointer(Shader.TCOORD_ATTRIB, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

        glVertexAttribPointer(Shader.TEXID_ATTRIB, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(Shader.TEXID_ATTRIB);


        glVertexAttribPointer(Shader.COLOR_ATTRIB, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(Shader.COLOR_ATTRIB);
    }

    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.enable();
        shader.setUniformMat4f("uPersp", Window.perspective);
        shader.setUniformMat4f("uModel", Window.view);

        if(!textureList.isEmpty()) {
            for (int i = 0; i < textureList.size(); i++) {
                glActiveTexture(GL_TEXTURE0 + i + 1);
                textureList.get(i).bind();
            }

            shader.setIntArray("tex_sample", textureSlots);
        }


        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glDrawElements(GL_TRIANGLES,  this.amount * 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        glBindVertexArray(0);


        for(int i = 0; i < textureList.size(); i++) {
            textureList.get(i).unbind();
        }
        shader.disable();
    }

    public void changeRenderable(Renderable renderable) {
        MeshProperty meshProperty = renderables.get(renderable);

        loadVertexProperties(meshProperty);
    }

    public void clear() {
        this.renderables.clear();
        this.amount = 0;

        freeSlots.clear();
        for(int i = 0; i < meshSize; i++) {
            freeSlots.add(String.valueOf(i));
        }

        this.textureList.clear();

        this.vertices = new float[meshSize * 4 * VERTEX_SIZE];
    }

    public void removeRenderable(Renderable renderable) {
        MeshProperty meshProperty = renderables.get(renderable);

        int index = meshProperty.getIndex() * VERTEX_SIZE * 4;
        freeSlots.add(String.valueOf(meshProperty.getIndex()));
        this.renderables.remove(renderable);

        for(int i = index; i < index + VERTEX_SIZE * 4; i++) {
            vertices[i] = 0.0f;
        }

        for(MeshProperty meshProperty1 : renderables.values()) {
            if(meshProperty.getIndex() < meshProperty1.getIndex()) {
                meshProperty1.setIndex(meshProperty1.getIndex() - 1);
            }
            loadVertexProperties(meshProperty1);
        }

        amount--;
    }

    public synchronized void addRenderable(Renderable renderable) {

        if(freeSlots.isEmpty()) {
            throw new IllegalStateException("RenderBatch is full...");
        }

        int index = amount;

        renderables.put(renderable, new MeshProperty(renderable, index));


        loadVertexProperties(renderables.get(renderable));
        freeSlots.remove(String.valueOf(renderables.get(renderable).getIndex()));
        amount++;
    }


    private void loadVertexProperties(MeshProperty meshProperty) {
        int index = meshProperty.getIndex();
        Renderable renderable = meshProperty.getRenderable();

        int offset = index * 4 * VERTEX_SIZE;
        Vector2f[] texCoords = renderable.getTextureCords();

        if(renderable.getTexture() != null) {
            if (!textureList.contains(renderable.getTexture())) {
                textureList.add(renderable.getTexture());
            }
        }

        int texId = 0;
            for (int i = 0; i < textureList.size(); i++) {
                if (textureList.get(i) == renderable.getTexture()) {
                    texId = i + 1;
                    break;
                }
        }

        Vector3f[] corners = renderable.getPositions();
        for(int i = 0; i < 4; i++) {

            vertices[offset++] = corners[i].x;
            vertices[offset++] = corners[i].y;
            vertices[offset++] = corners[i].z;

            vertices[offset++] = texCoords[i].x;
            vertices[offset++] = texCoords[i].y;

            vertices[offset++] = texId;

            vertices[offset++] = renderable.getColor().x;
            vertices[offset++] = renderable.getColor().y;
            vertices[offset++] = renderable.getColor().z;
            vertices[offset++] = renderable.getColor().w;

        }
    }

    private int[] createIndices() {
            // 6 indices per quad (3 per triangle)
            int[] elements = new int[6 * meshSize];
            for (int i=0; i < meshSize; i++) {
                loadElementIndices(elements, i);

            }
            return elements;
    }

    public void removeTexture(Texture texture) {
        this.textureList.remove(texture);
    }


    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //0,1,3, 1,2,3
        // Triangle 1
        elements[offsetArrayIndex] = offset + 0;
        elements[offsetArrayIndex + 1] = offset + 1;
        elements[offsetArrayIndex + 2] = offset + 3;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 1;
        elements[offsetArrayIndex + 4] = offset +2;
        elements[offsetArrayIndex + 5] = offset +3;
    }
}
