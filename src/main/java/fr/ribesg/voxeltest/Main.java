package fr.ribesg.voxeltest;

import fr.ribesg.voxeltest.gfx.CameraController;
import fr.ribesg.voxeltest.gfx.VBOHandler;
import fr.ribesg.voxeltest.world.World;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * @author Ribesg
 */
public final class Main {

    public static final Random RANDOM = new Random();

    private final CameraController camera;

    private final World world;

    private int axesHandle, axesColorHandle, axesSize;
    private int axesPointsHandle, axesPointsColorHandle, axesPointsSize;

    public Main() throws LWJGLException {
        this.camera = new CameraController(-16f, -16f, -16f, -45f, 45f);

        this.world = new World();

        this.init();
        this.loop();
        this.destroy();
    }

    private void init() throws LWJGLException {
        // Initialize Display
        Display.setDisplayMode(new DisplayMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));
        Display.create();

        // Initialize GL View
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(Config.HORIZONTAL_FOV, Config.SCREEN_WIDTH / (float) Config.SCREEN_HEIGHT, 0.001f, Config.VIEW_DISTANCE);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);
        glPointSize(5f);
        glLineWidth(2f);

        // Draw some axes
        final int length = 1000;
        final float[][] axesVertices = new float[][] {
            new float[] {0, 0, 0}, new float[] {length, 0, 0},
            new float[] {0, 0, 0}, new float[] {0, length, 0},
            new float[] {0, 0, 0}, new float[] {0, 0, length}
        };
        this.axesHandle = VBOHandler.createNew3D(axesVertices);
        this.axesSize = axesVertices.length * 3;

        final float[][] axesColors = new float[][] {
            new float[] {1, 0, 0}, new float[] {1, 0, 0},
            new float[] {0, 1, 0}, new float[] {0, 1, 0},
            new float[] {0, 0, 1}, new float[] {0, 0, 1}
        };
        this.axesColorHandle = VBOHandler.createNew3D(axesColors);

        final List<float[]> axesPoints = new ArrayList<>(length);
        final List<float[]> axesColorsPoints = new ArrayList<>(length);
        for (int i = 1; i <= length; i++) {
            axesPoints.add(new float[] {i, 0, 0});
            axesPoints.add(new float[] {0, i, 0});
            axesPoints.add(new float[] {0, 0, i});
            if (i % 16 == 0) {
                axesColorsPoints.add(new float[] {1, .25f, .75f});
                axesColorsPoints.add(new float[] {1, .25f, .75f});
                axesColorsPoints.add(new float[] {1, .25f, .75f});
            } else {
                axesColorsPoints.add(new float[] {1, 0, 0});
                axesColorsPoints.add(new float[] {0, 1, 0});
                axesColorsPoints.add(new float[] {0, 0, 1});
            }
        }
        this.axesPointsHandle = VBOHandler.createNew3D(axesPoints);
        this.axesPointsColorHandle = VBOHandler.createNew3D(axesColorsPoints);
        this.axesPointsSize = axesPoints.size();

        // Hide Mouse pointer
        Mouse.setGrabbed(true);

        // Initialize Timer
        Timer.init();
    }

    private void loop() {
        float delta;
        while (!Display.isCloseRequested()) {
            // Update data
            delta = Timer.getDelta();
            this.camera.update(delta);
            if (Mouse.isGrabbed() && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                Mouse.setGrabbed(false);
            } else if (!Mouse.isGrabbed() && Mouse.isButtonDown(0)) {
                Mouse.setGrabbed(true);
            }

            // Render data
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();
            {
                VBOHandler.render(this.axesHandle, this.axesColorHandle, GL_LINES, this.axesSize);
                VBOHandler.render(this.axesPointsHandle, this.axesPointsColorHandle, GL_POINTS, this.axesPointsSize);
                glLineWidth(3f);
                this.world.test();
                glLineWidth(2f);
            }
            glPopMatrix();

            // Update screen
            this.camera.lookThrough();
            Display.update();
            Timer.updateFps();
            Display.sync(Config.MAX_FPS);
        }
    }

    private void destroy() {
        Display.destroy();
    }

    // ##################################################################### //

    public static void main(final String[] args) {
        // Initialize Configuration
        Config.init();

        try {
            new Main();
        } catch (final LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
