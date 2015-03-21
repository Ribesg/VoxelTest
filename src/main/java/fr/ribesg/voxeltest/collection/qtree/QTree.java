package fr.ribesg.voxeltest.collection.qtree;

/**
 * @author Ribesg
 */
public final class QTree<T> {

    // ##
    // ##               ^
    // ##               |
    // ##   NORTH_WEST  |  NORTH_EAST
    // ##               |
    // ##               |
    // ## --------------+------------->
    // ##               |<---radius--->
    // ##               |
    // ##   SOUTH_WEST  |  SOUTH_EAST
    // ##               |
    // ##               |
    // ##

    /* package */ static final int NORTH_WEST = 0;
    /* package */ static final int NORTH_EAST = 1;
    /* package */ static final int SOUTH_WEST = 2;
    /* package */ static final int SOUTH_EAST = 3;

    private final QTreeInternalNode<T> root;
    private final int                  maxDepth;
    private final long                 radius;

    public QTree(final int maxDepth, final long radius) {
        this.root = new QTreeInternalNode<>(this, 0, 0, 0);
        this.maxDepth = maxDepth;
        this.radius = radius;
    }

    public QTreeLeaf<T> get(final double x, final double y) {
        return this.root.get(x, y);
    }

    public void set(final double x, final double y, final T data) {
        this.root.set(x, y, data);
    }

    /* package */ int getMaxDepth() {
        return this.maxDepth;
    }

    /* package */ long getRadius() {
        return this.radius;
    }
}
