package fr.ribesg.voxeltest.old.collection.octree;

/**
 * @author Ribesg
 */
public abstract class OctreeNode<T> {

    protected final Octree<T> octree;
    protected final double    centerX;
    protected final double    centerY;
    protected final double    centerZ;

    protected OctreeNode(final Octree<T> octree, final double centerX, final double centerY, final double centerZ) {
        this.octree = octree;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
    }

    /* package */ abstract OctreeLeaf<T> get(final double x, final double y, final double z);

    /* package */ abstract void set(final double x, final double y, final double z, final T data);

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterY() {
        return this.centerY;
    }

    public double getCenterZ() {
        return this.centerZ;
    }
}
