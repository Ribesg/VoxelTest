package fr.ribesg.voxeltest.old.collection.octree;

/**
 * @author Ribesg
 */
public final class OctreeInternalNode<T> extends OctreeNode<T> {

    private final int             depth;
    private final OctreeNode<T>[] nodes;

    @SuppressWarnings("unchecked")
    /* package */ OctreeInternalNode(final Octree<T> octree, final double centerX, final double centerY, final double centerZ, final int depth) {
        super(octree, centerX, centerY, centerZ);
        this.depth = depth;
        this.nodes = new OctreeNode[8];
    }

    /* package */ OctreeNode<T> createNode(final int i) {
        // Check that we're not erasing an existing node
        if (this.nodes[i] != null) {
            throw new IllegalStateException("Node " + i + " already exists");
        }

        // Compute center of node
        final double centerX, centerY, centerZ;
        final double diff = this.octree.getRadius() / (2 << (this.depth + 1));
        switch (i) {
            case Octree.TOP_NORTH_WEST:
                centerX = this.centerX - diff;
                centerY = this.centerY + diff;
                centerZ = this.centerZ + diff;
                break;
            case Octree.TOP_NORTH_EAST:
                centerX = this.centerX + diff;
                centerY = this.centerY + diff;
                centerZ = this.centerZ + diff;
                break;
            case Octree.TOP_SOUTH_WEST:
                centerX = this.centerX - diff;
                centerY = this.centerY - diff;
                centerZ = this.centerZ + diff;
                break;
            case Octree.TOP_SOUTH_EAST:
                centerX = this.centerX + diff;
                centerY = this.centerY - diff;
                centerZ = this.centerZ + diff;
                break;
            case Octree.BOTTOM_NORTH_WEST:
                centerX = this.centerX - diff;
                centerY = this.centerY + diff;
                centerZ = this.centerZ - diff;
                break;
            case Octree.BOTTOM_NORTH_EAST:
                centerX = this.centerX + diff;
                centerY = this.centerY + diff;
                centerZ = this.centerZ - diff;
                break;
            case Octree.BOTTOM_SOUTH_WEST:
                centerX = this.centerX - diff;
                centerY = this.centerY - diff;
                centerZ = this.centerZ - diff;
                break;
            case Octree.BOTTOM_SOUTH_EAST:
                centerX = this.centerX + diff;
                centerY = this.centerY - diff;
                centerZ = this.centerZ - diff;
                break;
            default:
                throw new IllegalArgumentException("Invalid child identifier: " + i);
        }

        if (this.depth == this.octree.getMaxDepth() - 1) {
            // We're at the deepest Internal Node level, create a leaf
            this.nodes[i] = new OctreeLeaf<>(this.octree, centerX, centerY, centerZ);
        } else {
            // We creat another Internal Node level
            this.nodes[i] = new OctreeInternalNode<>(this.octree, centerX, centerY, centerZ, this.depth + 1);
        }

        // Return built node
        return this.nodes[i];
    }

    private int select(final double x, final double y, final double z) {
        if (x < this.centerX) {
            if (y < this.centerY) {
                if (z < this.centerZ) {
                    return Octree.BOTTOM_SOUTH_WEST;
                } else {
                    return Octree.TOP_SOUTH_WEST;
                }
            } else {
                if (z < this.centerZ) {
                    return Octree.BOTTOM_NORTH_WEST;
                } else {
                    return Octree.TOP_NORTH_WEST;
                }
            }
        } else {
            if (y < this.centerY) {
                if (z < this.centerZ) {
                    return Octree.BOTTOM_SOUTH_EAST;
                } else {
                    return Octree.TOP_SOUTH_EAST;
                }
            } else {
                if (z < this.centerZ) {
                    return Octree.BOTTOM_NORTH_EAST;
                } else {
                    return Octree.TOP_NORTH_EAST;
                }
            }
        }
    }

    @Override
    /* package */ OctreeLeaf<T> get(final double x, final double y, final double z) {
        return this.nodes[this.select(x, y, z)].get(x, y, z);
    }

    @Override
    /* package */ void set(final double x, final double y, final double z, final T data) {
        this.nodes[this.select(x, y, z)].set(x, y, z, data);
    }

    /* package */ OctreeNode<T> getNode(final int i) {
        return this.nodes[i];
    }
}
