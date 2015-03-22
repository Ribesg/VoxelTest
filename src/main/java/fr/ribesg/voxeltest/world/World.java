package fr.ribesg.voxeltest.world;

import fr.ribesg.voxeltest.Config;
import fr.ribesg.voxeltest.collection.octree.Octree;
import fr.ribesg.voxeltest.collection.octree.OctreeLeaf;

/**
 * @author Ribesg
 */
public final class World {

    private final Octree<Chunk> tree;

    public World() {
        this.tree = new Octree<>(Config.OCTREE_DEPTH, Config.OCTREE_RADIUS);
    }

    public void test() {
        for (int x = -Config.CHUNK_SIZE * 8; x < Config.CHUNK_SIZE * 8; x += Config.CHUNK_SIZE) {
            for (int y = -Config.CHUNK_SIZE * 8; y < Config.CHUNK_SIZE * 8; y += Config.CHUNK_SIZE) {
                for (int z = -Config.CHUNK_SIZE * 8; z < Config.CHUNK_SIZE * 8; z += Config.CHUNK_SIZE) {
                    this.loadOrCreateChunkAt(x, y, z).render();
                }
            }
        }
    }

    public Chunk loadOrCreateChunkAt(final double x, final double y, final double z) {
        final OctreeLeaf<Chunk> leaf = this.tree.get(x, y, z);
        if (leaf == null) {
            final Chunk chunk = new Chunk(x, y, z);
            this.tree.set(x, y, z, chunk);
            return chunk;
        } else {
            return leaf.getData();
        }
    }

    public void unloadChunkAt(final double x, final double y, final double z) {
        // TODO
    }
}
