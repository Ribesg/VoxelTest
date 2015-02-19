package fr.ribesg.voxeltest.collection.qtree;

/**
 * @author Ribesg
 */
public abstract class QTreeNode<T> {

    protected final QTree<T> qTree;
    protected final double   centerX;
    protected final double   centerY;

    protected QTreeNode(final QTree<T> qTree, final double centerX, final double centerY) {
        this.qTree = qTree;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /* package */ abstract QTreeLeaf<T> get(final double x, final double y);

    /* package */ abstract void set(final double x, final double y, final T data);

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterY() {
        return this.centerY;
    }
}
