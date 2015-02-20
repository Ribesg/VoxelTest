package fr.ribesg.voxeltest;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * @author Ribesg
 */
public class MSTestMain {

    private static final Map<Integer, Float> zooms;

    static {
        zooms = new HashMap<>();
        MSTestMain.zooms.put(4, 14f);
        MSTestMain.zooms.put(8, 9f);
        MSTestMain.zooms.put(16, 6f);
    }

    private static int   chunkSize = 8;
    private static float zoom      = MSTestMain.zooms.get(MSTestMain.chunkSize);

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
        MSTestMain.contourLinesTable = new float[][]{
            /*  0 */
            null,
            /*  1 */
            new float[]{
                -1, -1,
                -1, 0,
                0, -1
            },
            /*  2 */
            new float[]{
                1, -1,
                1, 0,
                0, -1
            },
            /*  3 */
            new float[]{
                -1, 0,
                1, 0,
                -1, -1,

                -1, -1,
                1, -1,
                1, 0
            },
            /*  4 */
            new float[]{
                0, 1,
                1, 0,
                1, 1
            },
            /*  5 */
            new float[]{
                -1, 0,
                0, -1,
                -1, -1,

                0, 1,
                1, 0,
                1, 1
            },
            /*  6 */
            new float[]{
                0, 1,
                0, -1,
                1, -1,

                1, -1,
                1, 1,
                0, 1
            },
            /*  7 */
            new float[]{
                -1, 0,
                0, 1,
                1, -1,

                -1, 0,
                -1, -1,
                1, -1,

                0, 1,
                1, 1,
                1, -1
            },
            /*  8 */
            new float[]{
                0, 1,
                -1, 0,
                -1, 1
            },
            /*  9 */
            new float[]{
                0, -1,
                0, 1,
                -1, 1,

                -1, 1,
                0, -1,
                -1, -1
            },
            /* 10 */
            new float[]{
                -1, 1,
                -1, 0,
                0, 1,

                0, -1,
                1, 0,
                1, -1
            },
            /* 11 */
            new float[]{
                1, 0,
                0, 1,
                -1, -1,

                1, 0,
                1, -1,
                -1, -1,

                0, 1,
                -1, 1,
                -1, -1
            },
            /* 12 */
            new float[]{
                1, 0,
                -1, 0,
                -1, 1,

                -1, 1,
                1, 1,
                1, 0
            },
            /* 13 */
            new float[]{
                0, -1,
                1, 0,
                -1, 1,

                0, -1,
                -1, -1,
                -1, 1,

                1, 0,
                1, 1,
                -1, 1
            },
            /* 14 */
            new float[]{
                0, -1,
                -1, 0,
                1, 1,

                0, -1,
                1, -1,
                1, 1,

                -1, 0,
                -1, 1,
                1, 1
            },
            /* 15 */
            new float[]{
                -1, -1,
                -1, 1,
                1, 1,

                -1, -1,
                1, -1,
                1, 1
            }
        };

        MSTestMain.voxels = new byte[MSTestMain.chunkSize][MSTestMain.chunkSize];
        for (int i = 0; i < MSTestMain.voxels.length; i++) {
            for (int j = 0; j < MSTestMain.voxels.length; j++) {
                //MSTestMain.voxels[i][j] = (byte) ((i - chunkSize / 2) * (i - chunkSize / 2) + (j - chunkSize / 2) * (j - chunkSize / 2) - (chunkSize / 2 * chunkSize / 2));
                //MSTestMain.voxels[i][j] = (byte) X.RANDOM.nextInt();
                //MSTestMain.voxels[i][j] = i % 16 >= 8 ? (j % 16 >= 8 ? Byte.MIN_VALUE : Byte.MAX_VALUE) : (j % 16 < 8 ? Byte.MIN_VALUE : Byte.MAX_VALUE);
                MSTestMain.voxels[i][j] = i + j + 1 >= MSTestMain.chunkSize ? Byte.MAX_VALUE : Byte.MIN_VALUE;
            }
        }

        int size = MSTestMain.fillVBO(MSTestMain.chunkSize);

        // Main loop
        final float diffX = X.WINDOW_WIDTH / 2 - MSTestMain.zoom;
        final float diffY = X.WINDOW_HEIGHT / 2 - MSTestMain.zoom;
        glTranslatef(diffX, diffY, 0);
        glScalef(MSTestMain.zoom, MSTestMain.zoom, 1f);
        while (!Display.isCloseRequested()) {
            // RENDER
            glClear(GL_COLOR_BUFFER_BIT);

            glPushMatrix();
            {
                glBindBuffer(GL_ARRAY_BUFFER, MSTestMain.vboVertexHandle);
                glVertexPointer(2, GL_FLOAT, 0, 0L);
                glBindBuffer(GL_ARRAY_BUFFER, MSTestMain.vboColorHandle);
                glColorPointer(3, GL_FLOAT, 0, 0L);
                glDrawArrays(GL_TRIANGLES, 0, size);
            }
            glPopMatrix();

            glPointSize(5);
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
                        glVertex2f(MSTestMain.zoom * x - MSTestMain.zoom * (MSTestMain.chunkSize - 1) / 2f, MSTestMain.zoom * y - MSTestMain.zoom * (MSTestMain.chunkSize - 1) / 2f);
                    }
                }
            }
            glEnd();

            glBegin(GL_LINES);
            {
                glColor3f(0, 0, 1);
                glVertex2f(-X.WINDOW_WIDTH, 0);
                glVertex2f(X.WINDOW_WIDTH, 0);
                glVertex2f(0, -X.WINDOW_HEIGHT);
                glVertex2f(0, X.WINDOW_HEIGHT);
            }
            glEnd();

            // END RENDER
            // LOGIC

            // XXX: FIX THIS MESS
            final int x = (int) (((Mouse.getX() - diffX) / MSTestMain.zoom / MSTestMain.chunkSize) + (chunkSize + 1)) / (chunkSize - 1);
            final int y = (int) (((X.WINDOW_HEIGHT - Mouse.getY() - diffY) / MSTestMain.zoom / MSTestMain.chunkSize) + (chunkSize + 1)) / (chunkSize - 1);
            System.out.println("x=" + x + " y=" + y);
            if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
                final int radius = (int) Math.max(1, MSTestMain.chunkSize / 8f);
                for (int dx = Math.max(0, x - radius); dx <= Math.min(MSTestMain.voxels.length - 1, x + radius); dx++) {
                    for (int dy = Math.max(0, y - radius); dy <= Math.min(MSTestMain.voxels.length - 1, y + radius); dy++) {
                        final int d = dx - x == 0 && dy - y == 0 ? Integer.MAX_VALUE : (int) (radius * radius / (double) Math.max((dx - x) * (dx - x) + (dy - y) * (dy - y), 1));
                        if (Mouse.isButtonDown(0)) {
                            MSTestMain.voxels[dx][dy] = (byte) Math.min(MSTestMain.voxels[dx][dy] + d, Byte.MAX_VALUE);
                        } else {
                            MSTestMain.voxels[dx][dy] = (byte) Math.max(MSTestMain.voxels[dx][dy] - d, Byte.MIN_VALUE);
                        }
                    }
                }
                size = MSTestMain.fillVBO(MSTestMain.chunkSize);
            }
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
                        for (int i = 0; i < MSTestMain.voxels.length; i++) {
                            X.RANDOM.nextBytes(MSTestMain.voxels[i]);
                        }
                        size = MSTestMain.fillVBO(MSTestMain.chunkSize);
                    }
                }
            }

            // END LOGIC

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
                        vertexList.add(MSTestMain.zoom * ((isX ? x : y) - (chunkSize - 2) / 2f) + v * MSTestMain.zoom / 2);
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
        /*
        final int trianglesAmount = vertexList.size() / 2 / 3;
        final float colors = 16;
        final Random random = new Random(Float.toString(colors).hashCode());
        for (int i = 0; i < trianglesAmount; i++) {
            float r, g, b;
            do {
                r = Math.round(random.nextFloat() * colors) / colors;
                g = Math.round(random.nextFloat() * colors) / colors;
                b = Math.round(random.nextFloat() * colors) / colors;
            } while (r == g && r == b && g == b);
            MSTestMain.colorBuffer.put(r);
            MSTestMain.colorBuffer.put(g);
            MSTestMain.colorBuffer.put(b);
            MSTestMain.colorBuffer.put(r);
            MSTestMain.colorBuffer.put(g);
            MSTestMain.colorBuffer.put(b);
            MSTestMain.colorBuffer.put(r);
            MSTestMain.colorBuffer.put(g);
            MSTestMain.colorBuffer.put(b);
        }
        /*/
        for (int i = 0; i < vertexList.size() / 2 * 3; i++) {
            MSTestMain.colorBuffer.put(1f);
        }
        //*/
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
