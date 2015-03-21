package fr.ribesg.voxeltest;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * @author Ribesg
 */
public final class VBOHandler {

    private static final Map<Integer, FloatBuffer> vbos;

    static {
        vbos = new HashMap<>();
    }

    public static int createNew3D(final Collection<float[]> values) {
        final int handle = glGenBuffers();
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(values.size() * 3);
        values.forEach(buffer::put);
        buffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        VBOHandler.vbos.put(handle, buffer);
        return handle;
    }

    public static int createNew3D(final float[][] values) {
        final int handle = glGenBuffers();
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length * 3);
        Arrays.stream(values).forEachOrdered(buffer::put);
        buffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        VBOHandler.vbos.put(handle, buffer);
        return handle;
    }

    public static void render(final int verticesHandle, final int glType, final int size) {
        glEnableClientState(GL_VERTEX_ARRAY);
        {
            glBindBuffer(GL_ARRAY_BUFFER, verticesHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glDrawArrays(glType, 0, size);
        }
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    public static void render(final int verticesHandle, final int colorHandle, final int glType, final int size) {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        {
            glBindBuffer(GL_ARRAY_BUFFER, verticesHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindBuffer(GL_ARRAY_BUFFER, colorHandle);
            glColorPointer(3, GL_FLOAT, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glDrawArrays(glType, 0, size);
        }
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
    }
}
