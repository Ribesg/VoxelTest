package fr.ribesg.voxeltest.old.block;

import fr.ribesg.voxeltest.old.X;

/**
 * @author Ribesg
 */
public class Chunk {

    public final byte[][] blocks;

    public Chunk(final int size) {
        this.blocks = new byte[size][size];
        for (int i = 0; i < size; i++) {
            X.RANDOM.nextBytes(this.blocks[i]);
        }
    }

    public int getSize() {
        return this.blocks.length;
    }
}
