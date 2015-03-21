package fr.ribesg.voxeltest.collection.qtree;

/**
 * @author Ribesg
 */
public final class QTreeLeaf<T> extends QTreeNode<T> {

    private T data;

    /* package */ QTreeLeaf(final QTree<T> qTree, final double centerX, final double centerY) {
        super(qTree, centerX, centerY);
        this.data = null;
    }

    @Override
    /* package */ QTreeLeaf<T> get(final double x, final double y) {
        // TODO Check x,y
        return this;
    }

    @Override
    /* package */ void set(final double x, final double y, final T data) {
        // TODO Check x,y
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    /* package */ void setData(final T data) {
        this.data = data;
    }
}
