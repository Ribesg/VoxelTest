package fr.ribesg.voxeltest.collection.octree;

/**
 * @author Ribesg
 */
public final class OctreeLeaf<T> extends OctreeNode<T> {

    private T data;

    /* package */ OctreeLeaf(final Octree<T> octree, final double centerX, final double centerY, final double centerZ) {
        super(octree, centerX, centerY, centerZ);
        this.data = null;
    }

    @Override
    /* package */ OctreeLeaf<T> get(final double x, final double y, final double z) {
        // TODO Check x,y,posZ
        return this;
    }

    @Override
    /* package */ void set(final double x, final double y, final double z, final T data) {
        // TODO Check x,y,posZ
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    /* package */ void setData(final T data) {
        this.data = data;
    }
}
