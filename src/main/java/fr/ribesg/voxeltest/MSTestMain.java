package fr.ribesg.voxeltest;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * @author Ribesg
 */
public class MSTestMain {

    private static final float zoom = 12;

    private static byte[][]    voxels;
    private static boolean[][] image;
    private static byte[][]    cells;

    public static void main(final String[] args) {
        // Display init
        try {
            Display.setDisplayMode(new DisplayMode(X.WINDOW_WIDTH, X.WINDOW_HEIGHT));
            Display.setTitle(X.WINDOW_TITLE);
            Display.create();
        } catch (final LWJGLException e) {
            System.err.println("Display wasn't initialized correctly.");
            System.exit(1);
        }

        // OpenGL init
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // Resets any previous projection matrices
        glOrtho(0, X.WINDOW_WIDTH, X.WINDOW_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        // Data init
        final int chunkSize = 8 * 8;
        final float[][] contourLinesTable = new float[][]{
            /*  0 */ null,
            /*  1 */ new float[]{-1, 0, 0, -1},
            /*  2 */ new float[]{1, 0, 0, -1},
            /*  3 */ new float[]{-1, 0, 1, 0},
            /*  4 */ new float[]{0, 1, 1, 0},
            /*  5 */ new float[]{-1, 0, 0, 1, 0, -1, 1, 0},
            /*  6 */ new float[]{0, 1, 0, -1},
            /*  7 */ new float[]{-1, 0, 0, 1},
            /*  8 */ new float[]{-1, 0, 0, 1},
            /*  9 */ new float[]{0, -1, 0, 1},
            /* 10 */ new float[]{-1, 0, 0, -1, 0, 1, 1, 0},
            /* 11 */ new float[]{1, 0, 0, 1},
            /* 12 */ new float[]{1, 0, -1, 0},
            /* 13 */ new float[]{0, -1, 1, 0},
            /* 14 */ new float[]{0, -1, -1, 0},
            /* 15 */ null
        };

        final List<Float> vertexList = new LinkedList<>();

        MSTestMain.voxels = new byte[chunkSize][chunkSize];
        for (int i = 0; i < MSTestMain.voxels.length; i++) {
            for (int j = 0; j < MSTestMain.voxels.length; j++) {
                //MSTestMain.voxels[i][j] = (byte) ((i - chunkSize / 2) * (i - chunkSize / 2) + (j - chunkSize / 2) * (j - chunkSize / 2) - (chunkSize / 2 * chunkSize / 2));
                MSTestMain.voxels[i][j] = (byte) X.RANDOM.nextInt();
            }
        }

        MSTestMain.image = new boolean[chunkSize][chunkSize];
        for (int x = 0; x < MSTestMain.voxels.length; x++) {
            for (int y = 0; y < MSTestMain.voxels.length; y++) {
                MSTestMain.image[x][y] = Math.abs(MSTestMain.voxels[x][y]) > 64;
            }
        }

        MSTestMain.cells = new byte[chunkSize - 1][chunkSize - 1];
        for (int x = 0; x < MSTestMain.cells.length; x++) {
            for (int y = 0; y < MSTestMain.cells.length; y++) {
                MSTestMain.cells[x][y] = 0;
                if (MSTestMain.image[x][y]) {
                    MSTestMain.cells[x][y] += 1;
                }
                if (MSTestMain.image[x + 1][y]) {
                    MSTestMain.cells[x][y] += 2;
                }
                if (MSTestMain.image[x + 1][y + 1]) {
                    MSTestMain.cells[x][y] += 4;
                }
                if (MSTestMain.image[x][y + 1]) {
                    MSTestMain.cells[x][y] += 8;
                }
            }
        }

        for (int x = 0; x < MSTestMain.cells.length; x++) {
            for (int y = 0; y < MSTestMain.cells.length; y++) {
                final float[] f = contourLinesTable[MSTestMain.cells[x][y]];
                if (f != null) {
                    boolean isX = true;
                    for (final float v : f) {
                        vertexList.add(MSTestMain.zoom * (isX ? x : y) + v * MSTestMain.zoom / 2);
                        isX = !isX;
                    }
                }
            }
        }

        final int vboVertexHandle = glGenBuffers();
        final int vboColorHandle = glGenBuffers();
        final FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexList.size());
        final FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(vertexList.size() / 2 * 3);
        vertexList.forEach(vertexBuffer::put);
        for (int i = 0; i < vertexList.size() / 2 * 3; i++) {
            colorBuffer.put(1f);
        }
        vertexBuffer.flip();
        colorBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        // Main loop
        glTranslatef(10, 10, 0);
        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT);

            glLineWidth(2f);
            glPushMatrix();
            {
                glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
                glVertexPointer(2, GL_FLOAT, 0, 0L);
                glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
                glColorPointer(3, GL_FLOAT, 0, 0L);
                glDrawArrays(GL_LINES, 0, vertexList.size());
            }
            glPopMatrix();

            glPointSize(MSTestMain.zoom / 2);
            glBegin(GL_POINTS);
            {
                for (int x = 0; x < MSTestMain.voxels.length; x++) {
                    for (int y = 0; y < MSTestMain.voxels.length; y++) {
                        glColor4f(
                            (Math.abs(MSTestMain.voxels[x][y]) * 2) / 255f,
                            (255 - Math.abs(MSTestMain.voxels[x][y]) * 2) / 255f,
                            0,
                            .25f
                                 );
                        glVertex2f(MSTestMain.zoom * x, MSTestMain.zoom * y);
                    }
                }
            }
            glEnd();

            Display.update();
            Display.sync(10);
            Timer.updateFps();
        }

        Display.destroy();
        System.exit(0);
    }
}
