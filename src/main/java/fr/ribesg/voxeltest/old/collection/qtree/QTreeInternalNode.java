package fr.ribesg.voxeltest.old.collection.qtree;

/**
 * @author Ribesg
 */
public final class QTreeInternalNode<T> extends QTreeNode<T> {

    private final int            depth;
    private final QTreeNode<T>[] nodes;

    @SuppressWarnings("unchecked")
    /* package */ QTreeInternalNode(final QTree<T> qTree, final double centerX, final double centerY, final int depth) {
        super(qTree, centerX, centerY);
        this.depth = depth;
        this.nodes = new QTreeNode[4];
    }

    /* package */ QTreeNode<T> createNode(final int i) {
        // Check that we're not erasing an existing node
        if (this.nodes[i] != null) {
            throw new IllegalStateException("Node " + i + " already exists");
        }

        // Compute center of node
        final double centerX, centerY;
        final double diff = this.qTree.getRadius() / (2 << (this.depth + 1));
        switch (i) {
            case QTree.NORTH_WEST:
                centerX = this.centerX - diff;
                centerY = this.centerY + diff;
                break;
            case QTree.NORTH_EAST:
                centerX = this.centerX + diff;
                centerY = this.centerY + diff;
                break;
            case QTree.SOUTH_WEST:
                centerX = this.centerX - diff;
                centerY = this.centerY - diff;
                break;
            case QTree.SOUTH_EAST:
                centerX = this.centerX + diff;
                centerY = this.centerY - diff;
                break;
            default:
                throw new IllegalArgumentException("Invalid child identifier: " + i);
        }

        if (this.depth == this.qTree.getMaxDepth() - 1) {
            // We're at the deepest Internal Node level, create a leaf
            this.nodes[i] = new QTreeLeaf<>(this.qTree, centerX, centerY);
        } else {
            // We creat another Internal Node level
            this.nodes[i] = new QTreeInternalNode<>(this.qTree, centerX, centerY, this.depth + 1);
        }

        return this.nodes[i];
    }

    private int select(final double x, final double y) {
        if (x < this.centerX) {
            if (y < this.centerY) {
                return QTree.SOUTH_WEST;
            } else {
                return QTree.NORTH_WEST;
            }
        } else {
            if (y < this.centerY) {
                return QTree.SOUTH_EAST;
            } else {
                return QTree.NORTH_EAST;
            }
        }
    }

    @Override
    /* package */ QTreeLeaf<T> get(final double x, final double y) {
        return this.nodes[this.select(x, y)].get(x, y);
    }

    @Override
    /* package */ void set(final double x, final double y, final T data) {
        this.nodes[this.select(x, y)].set(x, y, data);
    }

    /* package */ QTreeNode<T> getNode(final int i) {
        return this.nodes[i];
    }
}
