package fr.ribesg.voxeltest;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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

    private static float[][]   contourLinesTable;
    private static byte[][]    voxels;
    private static boolean[][] image;
    private static byte[][]    cells;

    private static int vboVertexHandle, vboColorHandle;
    private static FloatBuffer vertexBuffer, colorBuffer;

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
        MSTestMain.contourLinesTable = new float[][]{
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

        MSTestMain.voxels = new byte[chunkSize][chunkSize];
        for (int i = 0; i < MSTestMain.voxels.length; i++) {
            for (int j = 0; j < MSTestMain.voxels.length; j++) {
                //MSTestMain.voxels[i][j] = (byte) ((i - chunkSize / 2) * (i - chunkSize / 2) + (j - chunkSize / 2) * (j - chunkSize / 2) - (chunkSize / 2 * chunkSize / 2));
                //MSTestMain.voxels[i][j] = (byte) X.RANDOM.nextInt();
                MSTestMain.voxels[i][j] = i % 16 >= 8 ? (j % 16 >= 8 ? Byte.MIN_VALUE : Byte.MAX_VALUE) : (j % 16 < 8 ? Byte.MIN_VALUE : Byte.MAX_VALUE);
            }
        }

        int size = MSTestMain.fillVBO(chunkSize);

        // Main loop
        glTranslatef(10, 10, 0);
        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT);

            glPointSize(3);
            glBegin(GL_POINTS);
            {
                for (int x = 0; x < MSTestMain.voxels.length; x++) {
                    for (int y = 0; y < MSTestMain.voxels.length; y++) {
                        glColor4f(
                            (MSTestMain.voxels[x][y] + 128) / 255f,
                            (255 - (MSTestMain.voxels[x][y] + 128)) / 255f,
                            0,
                            .25f
                                 );
                        glVertex2f(MSTestMain.zoom * x, MSTestMain.zoom * y);
                    }
                }
            }
            glEnd();

            glLineWidth(4);
            glPushMatrix();
            {
                glBindBuffer(GL_ARRAY_BUFFER, MSTestMain.vboVertexHandle);
                glVertexPointer(2, GL_FLOAT, 0, 0L);
                glBindBuffer(GL_ARRAY_BUFFER, MSTestMain.vboColorHandle);
                glColorPointer(3, GL_FLOAT, 0, 0L);
                glDrawArrays(GL_LINES, 0, size);
            }
            glPopMatrix();

            if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
                final int radius = 10;
                final int x = (int) (Mouse.getX() / MSTestMain.zoom);
                final int y = (int) ((X.WINDOW_HEIGHT - Mouse.getY()) / MSTestMain.zoom);
                for (int dx = Math.max(0, x - radius); dx <= Math.min(MSTestMain.voxels.length - 1, x + radius); dx++) {
                    for (int dy = Math.max(0, y - radius); dy <= Math.min(MSTestMain.voxels.length - 1, y + radius); dy++) {
                        final int d = (int) (radius * radius / (double) Math.max((dx - x) * (dx - x) + (dy - y) * (dy - y), 1));
                        if (Mouse.isButtonDown(0)) {
                            MSTestMain.voxels[dx][dy] = (byte) Math.min(MSTestMain.voxels[dx][dy] + d, Byte.MAX_VALUE);
                        } else {
                            MSTestMain.voxels[dx][dy] = (byte) Math.max(MSTestMain.voxels[dx][dy] - d, Byte.MIN_VALUE);
                        }
                    }
                }
                size = MSTestMain.fillVBO(chunkSize);
            } else if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                for (int x = 0; x < MSTestMain.voxels.length; x++) {
                    X.RANDOM.nextBytes(MSTestMain.voxels[x]);
                }
                size = MSTestMain.fillVBO(chunkSize);
            }

            Display.update();
            Display.sync(60);
            Timer.updateFps();
        }

        Display.destroy();
        System.exit(0);
    }

    private static int fillVBO(final int chunkSize) {
        MSTestMain.image = new boolean[chunkSize][chunkSize];
        for (int x = 0; x < MSTestMain.voxels.length; x++) {
            for (int y = 0; y < MSTestMain.voxels.length; y++) {
                MSTestMain.image[x][y] = MSTestMain.voxels[x][y] > 0;
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

        final List<Float> vertexList = new LinkedList<>();
        for (int x = 0; x < MSTestMain.cells.length; x++) {
            for (int y = 0; y < MSTestMain.cells.length; y++) {
                final float[] f = MSTestMain.contourLinesTable[MSTestMain.cells[x][y]];
                if (f != null) {
                    boolean isX = true;
                    for (final float v : f) {
                        vertexList.add(MSTestMain.zoom * (isX ? x : y) + v * MSTestMain.zoom / 2 + 6f);
                        isX = !isX;
                    }
                }
            }
        }

        MSTestMain.vboVertexHandle = glGenBuffers();
        MSTestMain.vboColorHandle = glGenBuffers();
        MSTestMain.vertexBuffer = BufferUtils.createFloatBuffer(vertexList.size());
        MSTestMain.colorBuffer = BufferUtils.createFloatBuffer(vertexList.size() / 2 * 3);
        vertexList.forEach(MSTestMain.vertexBuffer::put);
        for (int i = 0; i < vertexList.size() / 2 * 3; i++) {
            MSTestMain.colorBuffer.put(1f);
        }
        MSTestMain.vertexBuffer.flip();
        MSTestMain.colorBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, MSTestMain.vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, MSTestMain.vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, MSTestMain.vboColorHandle);
        glBufferData(GL_ARRAY_BUFFER, MSTestMain.colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return vertexList.size();
    }
}
