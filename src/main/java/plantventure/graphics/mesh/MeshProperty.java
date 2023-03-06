package plantventure.graphics.mesh;

import plantventure.graphics.game.Renderable;

public class MeshProperty {

    private final Renderable renderable;
    private int index;
    private boolean changed;

    public MeshProperty(Renderable renderable, int index) {
        this.renderable = renderable;
        this.index = index;
        this.changed = false;
    }


    public void setIndex(int index) {
        this.index = index;
    }
    public Renderable getRenderable() {
        return renderable;
    }

    public int getIndex() {
        return index;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
