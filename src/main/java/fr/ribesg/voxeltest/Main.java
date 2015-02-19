package fr.ribesg.voxeltest;

import fr.ribesg.voxeltest.block.Chunk;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 */
public class Main {

    /**
     * Program entry point.
     *
     * @param args program arguments
     */
    public static void main(final String[] args) {
        new Main(args);
    }

    private final Chunk chunk;

    private int vboColorHandle;
    private int vboVertexHandle;

    private Main(final String[] args) {
        this.chunk = new Chunk();
        this.setUp();
        this.loop();
        this.destroy();
    }

    private void setUp() {
        // Display SetUp
        try {
            Display.setDisplayMode(new DisplayMode(X.WINDOW_WIDTH, X.WINDOW_HEIGHT));
            Display.setTitle(X.WINDOW_TITLE);
            Display.create();
        } catch (final LWJGLException e) {
            System.err.println("Display wasn't initialized correctly.");
            System.exit(1);
        }

        // OpenGL SetUp
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        GLU.gluPerspective(
            45.0f,
            (float) X.WINDOW_WIDTH / (float) X.WINDOW_HEIGHT,
            0.1f,
            1000.0f);

        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        if (X.USE_VBO) {
            glEnableClientState(GL_VERTEX_ARRAY);
            glEnableClientState(GL_COLOR_ARRAY);

            this.vboColorHandle = glGenBuffers();
            this.vboVertexHandle = glGenBuffers();
        }
        // Timer SetUp
        Timer.init();

        if (X.USE_VBO) {
            // Data SetUp
            final FloatBuffer vertexPositionData = BufferUtils.createFloatBuffer((int) (Math.pow(X.CHUNK_SIZE, 3) * 24 * 3));
            final FloatBuffer vertexColorData = BufferUtils.createFloatBuffer(vertexPositionData.capacity());
            this.chunk.render(vertexPositionData, vertexColorData);
            vertexPositionData.flip();
            vertexColorData.flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.vboVertexHandle);
            glBufferData(GL_ARRAY_BUFFER, vertexPositionData, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboColorHandle);
            glBufferData(GL_ARRAY_BUFFER, vertexColorData, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
    }

    private void loop() {
        float d = 0f;
        while (!Display.isCloseRequested()) {
            d = (d + .5f) % 360f;
            this.render(d);
            this.logic();
            Display.update();
            Timer.updateFps();
            Display.sync(X.FIXED_FPS);
        }
    }

    private void render(final float d) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glPushMatrix();
        glTranslatef(-X.CHUNK_SIZE / 2, -X.CHUNK_SIZE / 2, -X.CHUNK_SIZE * 3);
        glRotatef(90f + d, 1.5f, 1.5f, 1.5f);
        glColor3f(0.5f, 0.5f, 1.0f);

        glLineWidth(2f);
        glBegin(GL_LINES);
        {
            glColor3f(1, 0, 0);
            glVertex3f(0, 0, 0);
            glVertex3f(Float.MAX_VALUE, 0, 0);
            glColor3f(0, 1, 0);
            glVertex3f(0, 0, 0);
            glVertex3f(0, Float.MAX_VALUE, 0);
            glColor3f(0, 0, 1);
            glVertex3f(0, 0, 0);
            glVertex3f(0, 0, Float.MAX_VALUE);
        }
        glEnd();

        if (X.USE_VBO) {
            glPushMatrix();
            {
                glBindBuffer(GL_ARRAY_BUFFER, this.vboVertexHandle);
                glVertexPointer(3, GL_FLOAT, 0, 0L);

                glBindBuffer(GL_ARRAY_BUFFER, this.vboColorHandle);
                glColorPointer(3, GL_FLOAT, 0, 0L);

                glDrawArrays(X.QUADS_OR_LINES, 0, (int) (Math.pow(X.CHUNK_SIZE, 3) * 24 * 3));
            }
            glPopMatrix();
        } else {
            this.chunk.render();
        }
    }

    private void logic() {
        X.QUADS_OR_LINES = !Keyboard.isKeyDown(Keyboard.KEY_SPACE) ? GL_LINE_LOOP : GL_QUADS;
    }

    private void destroy() {
        Display.destroy();
        System.exit(0);
    }
}
