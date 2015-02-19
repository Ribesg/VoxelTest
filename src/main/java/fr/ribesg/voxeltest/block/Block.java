package fr.ribesg.voxeltest.block;

import fr.ribesg.voxeltest.X;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Ribesg
 */
public class Block {

    private boolean active;
    private float[] color;

    public Block() {
        this.active = false;
        do {
            this.color = new float[]{
                Math.round(X.RANDOM.nextFloat()),
                Math.round(X.RANDOM.nextFloat()),
                Math.round(X.RANDOM.nextFloat()),
            };
        } while (this.color[0] == 0 && this.color[1] == 0 && this.color[2] == 0
            || this.color[0] == 1 && this.color[1] == 1 && this.color[2] == 1);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public void render(final Block[][][] chunk, final int x, final int y, final int z) {
        if (this.active) {
            glBegin(X.QUADS_OR_LINES);
            {
                //glColor3f(this.color[0], this.color[1], this.color[2]);
                if (y == chunk[0].length - 1 || !chunk[x][y + 1][z].isActive()) {
                    glColor3f(0, 0, 1);
                    glVertex3f(X.BLOCK_SIZE, X.BLOCK_SIZE, 0);
                    glVertex3f(0, X.BLOCK_SIZE, 0);
                    glVertex3f(0, X.BLOCK_SIZE, X.BLOCK_SIZE);
                    glVertex3f(X.BLOCK_SIZE, X.BLOCK_SIZE, X.BLOCK_SIZE);
                }
                if (y == 0 || !chunk[x][y - 1][z].isActive()) {
                    glColor3f(0, 1, 0);
                    glVertex3f(X.BLOCK_SIZE, 0, X.BLOCK_SIZE);
                    glVertex3f(0, 0, X.BLOCK_SIZE);
                    glVertex3f(0, 0, 0);
                    glVertex3f(X.BLOCK_SIZE, 0, 0);
                }
                if (z == chunk[0][0].length - 1 || !chunk[x][y][z + 1].isActive()) {
                    glColor3f(0, 1, 1);
                    glVertex3f(X.BLOCK_SIZE, X.BLOCK_SIZE, X.BLOCK_SIZE);
                    glVertex3f(0, X.BLOCK_SIZE, X.BLOCK_SIZE);
                    glVertex3f(0, 0, X.BLOCK_SIZE);
                    glVertex3f(X.BLOCK_SIZE, 0, X.BLOCK_SIZE);
                }
                if (z == 0 || !chunk[x][y][z - 1].isActive()) {
                    glColor3f(1, 0, 0);
                    glVertex3f(X.BLOCK_SIZE, 0, 0);
                    glVertex3f(0, 0, 0);
                    glVertex3f(0, X.BLOCK_SIZE, 0);
                    glVertex3f(X.BLOCK_SIZE, X.BLOCK_SIZE, 0);
                }
                if (x == 0 || !chunk[x - 1][y][z].isActive()) {
                    glColor3f(1, 0, 1);
                    glVertex3f(0, X.BLOCK_SIZE, X.BLOCK_SIZE);
                    glVertex3f(0, X.BLOCK_SIZE, 0);
                    glVertex3f(0, 0, 0);
                    glVertex3f(0, 0, X.BLOCK_SIZE);
                }
                if (x == chunk.length - 1 || !chunk[x + 1][y][z].isActive()) {
                    glColor3f(1, 1, 0);
                    glVertex3f(X.BLOCK_SIZE, X.BLOCK_SIZE, 0);
                    glVertex3f(X.BLOCK_SIZE, X.BLOCK_SIZE, X.BLOCK_SIZE);
                    glVertex3f(X.BLOCK_SIZE, 0, X.BLOCK_SIZE);
                    glVertex3f(X.BLOCK_SIZE, 0, 0);
                }
            }
            glEnd();
        }
    }

    public void render(final Block[][][] chunk, final int x, final int y, final int z, final FloatBuffer vertexPositionData, final FloatBuffer vertexColorData) {
        if (this.active) {
            if (y == chunk[0].length - 1 || !chunk[x][y + 1][z].isActive()) {
                vertexPositionData.put(new float[]{
                    x + X.BLOCK_SIZE, y + X.BLOCK_SIZE, z,
                    x, y + X.BLOCK_SIZE, z,
                    x, y + X.BLOCK_SIZE, z + X.BLOCK_SIZE,
                    x + X.BLOCK_SIZE, y + X.BLOCK_SIZE, z + X.BLOCK_SIZE,
                });
                for (int i = 0; i < 4; i++) {
                    vertexColorData.put(this.color);
                }
            }
            if (y == 0 || !chunk[x][y - 1][z].isActive()) {
                vertexPositionData.put(new float[]{
                    x + X.BLOCK_SIZE, y, z + X.BLOCK_SIZE,
                    x, y, z + X.BLOCK_SIZE,
                    x, y, z,
                    x + X.BLOCK_SIZE, y, z,
                });
                for (int i = 0; i < 4; i++) {
                    vertexColorData.put(this.color);
                }
            }
            if (z == chunk[0][0].length - 1 || !chunk[x][y][z + 1].isActive()) {
                vertexPositionData.put(new float[]{
                    x + X.BLOCK_SIZE, y + X.BLOCK_SIZE, z + X.BLOCK_SIZE,
                    x, y + X.BLOCK_SIZE, z + X.BLOCK_SIZE,
                    x, y, z + X.BLOCK_SIZE,
                    x + X.BLOCK_SIZE, y, z + X.BLOCK_SIZE,
                });
                for (int i = 0; i < 4; i++) {
                    vertexColorData.put(this.color);
                }
            }
            if (z == 0 || !chunk[x][y][z - 1].isActive()) {
                vertexPositionData.put(new float[]{
                    x + X.BLOCK_SIZE, y, z,
                    x, y, z,
                    x, y + X.BLOCK_SIZE, z,
                    x + X.BLOCK_SIZE, y + X.BLOCK_SIZE, z,
                });
                for (int i = 0; i < 4; i++) {
                    vertexColorData.put(this.color);
                }
            }
            if (x == 0 || !chunk[x - 1][y][z].isActive()) {
                vertexPositionData.put(new float[]{
                    x, y + X.BLOCK_SIZE, z + X.BLOCK_SIZE,
                    x, y + X.BLOCK_SIZE, z,
                    x, y, z,
                    x, y, z + X.BLOCK_SIZE,
                });
                for (int i = 0; i < 4; i++) {
                    vertexColorData.put(this.color);
                }
            }
            if (x == chunk.length - 1 || !chunk[x + 1][y][z].isActive()) {
                vertexPositionData.put(new float[]{
                    x + X.BLOCK_SIZE, y + X.BLOCK_SIZE, z,
                    x + X.BLOCK_SIZE, y + X.BLOCK_SIZE, z + X.BLOCK_SIZE,
                    x + X.BLOCK_SIZE, y, z + X.BLOCK_SIZE,
                    x + X.BLOCK_SIZE, y, z
                });
                for (int i = 0; i < 4; i++) {
                    vertexColorData.put(this.color);
                }
            }
        }
    }
}
