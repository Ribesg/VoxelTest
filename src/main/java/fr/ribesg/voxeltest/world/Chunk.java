package fr.ribesg.voxeltest.world;

import fr.ribesg.voxeltest.Config;
import fr.ribesg.voxeltest.Main;
import fr.ribesg.voxeltest.gfx.VBOHandler;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ribesg
 */
public final class Chunk {

    private final byte[][][] values;
    private final double     x, y, z;

    private int handle, colorHandle, size;

    public Chunk(final double x, final double y, final double z) {
        this.x = x - x % Config.CHUNK_SIZE;
        this.y = y - y % Config.CHUNK_SIZE;
        this.z = z - z % Config.CHUNK_SIZE;

        this.values = new byte[Config.CHUNK_SIZE][Config.CHUNK_SIZE][Config.CHUNK_SIZE];

        this.handle = -1;
        this.colorHandle = -1;
        this.size = -1;
    }

    public void render() {
        if (this.handle == -1) {
            final float[] vertices = this.toVertices();
            this.handle = VBOHandler.createNew3D(vertices);

            final float[] c = new float[] {
                Main.RANDOM.nextFloat(),
                Main.RANDOM.nextFloat(),
                Main.RANDOM.nextFloat()
            };
            final List<float[]> colors = new ArrayList<>(vertices.length);
            for (int i = 0; i < vertices.length / 3; i++) {
                colors.add(c);
            }
            this.colorHandle = VBOHandler.createNew3D(colors);
            this.size = vertices.length;
        }
        VBOHandler.render(this.handle, this.colorHandle, GL11.GL_LINES, this.size);
    }

    private float[] toVertices() {
        final int d = Config.CHUNK_SIZE / 2;
        final float fx = (float) this.x, fy = (float) this.y, fz = (float) this.z;
        return new float[] {
            // Bottom surface
            fx - d, fy - d, fz - d,
            fx + d, fy - d, fz - d,

            fx - d, fy - d, fz - d,
            fx - d, fy - d, fz + d,

            fx + d, fy - d, fz - d,
            fx + d, fy - d, fz + d,

            fx - d, fy - d, fz + d,
            fx + d, fy - d, fz + d,

            // Top surface
            fx - d, fy + d, fz - d,
            fx + d, fy + d, fz - d,

            fx - d, fy + d, fz - d,
            fx - d, fy + d, fz + d,

            fx + d, fy + d, fz - d,
            fx + d, fy + d, fz + d,

            fx - d, fy + d, fz + d,
            fx + d, fy + d, fz + d,

            // Vertical edges
            fx - d, fy - d, fz - d,
            fx - d, fy + d, fz - d,

            fx - d, fy - d, fz + d,
            fx - d, fy + d, fz + d,

            fx + d, fy - d, fz - d,
            fx + d, fy + d, fz - d,

            fx + d, fy - d, fz + d,
            fx + d, fy + d, fz + d,
        };
    }
}
