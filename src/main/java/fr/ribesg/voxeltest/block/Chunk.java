package fr.ribesg.voxeltest.block;

import fr.ribesg.voxeltest.X;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * @author Ribesg
 */
public class Chunk {

    private final Block[][][] blocks;

    public Chunk() {
        this.blocks = new Block[X.CHUNK_SIZE][X.CHUNK_SIZE][X.CHUNK_SIZE];
        for (int i = 0; i < X.CHUNK_SIZE; i++) {
            for (int j = 0; j < X.CHUNK_SIZE; j++) {
                for (int k = 0; k < X.CHUNK_SIZE; k++) {
                    this.blocks[i][j][k] = new Block();
                    this.blocks[i][j][k].setActive(X.RANDOM.nextFloat() > .025f);
                }
            }
        }
    }

    public void render() {
        for (int i = 0; i < X.CHUNK_SIZE; i++) {
            for (int j = 0; j < X.CHUNK_SIZE; j++) {
                for (int k = 0; k < X.CHUNK_SIZE; k++) {
                    this.blocks[i][j][k].render(this.blocks, i, j, k);
                    glTranslatef(0.0f, 0.0f, 1f * X.BLOCK_SIZE);
                }
                glTranslatef(0f, 1f * X.BLOCK_SIZE, -1f * X.BLOCK_SIZE * X.CHUNK_SIZE);
            }
            glTranslatef(1f * X.BLOCK_SIZE, -1f * X.BLOCK_SIZE * X.CHUNK_SIZE, 0);
        }
    }

    public void render(final FloatBuffer vertexPositionData, final FloatBuffer vertexColorData) {
        for (int i = 0; i < X.CHUNK_SIZE; i++) {
            for (int j = 0; j < X.CHUNK_SIZE; j++) {
                for (int k = 0; k < X.CHUNK_SIZE; k++) {
                    this.blocks[i][j][k].render(this.blocks, i, j, k, vertexPositionData, vertexColorData);
                }
            }
        }
    }
}
