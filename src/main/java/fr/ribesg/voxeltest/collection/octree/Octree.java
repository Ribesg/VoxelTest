package fr.ribesg.voxeltest.collection.octree;

/**
 * @author Ribesg
 */
public final class Octree<T> {

    // ##                                #
    // ## TOP                            # BOTTOM
    // ##               ^                #               ^
    // ##               |                #               |
    // ##   NORTH_WEST  |  NORTH_EAST    #   NORTH_WEST  |  NORTH_EAST
    // ##               |                #               |
    // ##               |                #               |
    // ## --------------+------------->  # --------------+------------->
    // ##               |<---radius--->  #               |<---radius--->
    // ##               |                #               |
    // ##   SOUTH_WEST  |  SOUTH_EAST    #   SOUTH_WEST  |  SOUTH_EAST
    // ##               |                #               |
    // ##               |                #               |
    // ##                                #

    /* package */ static final int TOP_NORTH_WEST    = 0;
    /* package */ static final int TOP_NORTH_EAST    = 1;
    /* package */ static final int TOP_SOUTH_WEST    = 2;
    /* package */ static final int TOP_SOUTH_EAST    = 3;
    /* package */ static final int BOTTOM_NORTH_WEST = 4;
    /* package */ static final int BOTTOM_NORTH_EAST = 5;
    /* package */ static final int BOTTOM_SOUTH_WEST = 6;
    /* package */ static final int BOTTOM_SOUTH_EAST = 7;

    private final OctreeInternalNode<T> root;
    private final int                   maxDepth;
    private final long                  radius;

    public Octree(final int maxDepth, final long radius) {
        this.root = new OctreeInternalNode<>(this, 0, 0, 0, 0);
        this.maxDepth = maxDepth;
        this.radius = radius;
    }

    public OctreeLeaf<T> get(final double x, final double y, final double z) {
        return this.root.get(x, y, z);
    }

    public void set(final double x, final double y, final double z, final T data) {
        this.root.set(x, y, z, data);
    }

    /* package */ int getMaxDepth() {
        return this.maxDepth;
    }

    /* package */ long getRadius() {
        return this.radius;
    }
}
