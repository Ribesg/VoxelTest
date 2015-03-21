package fr.ribesg.voxeltest;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * @author Ribesg
 */
public final class Main {

    public static final Random RANDOM = new Random();

    private final CameraController camera;

    private int axesHandle, axesColorHandle, axesSize;

    public Main() throws LWJGLException {
        this.camera = new CameraController(-500f, -500f, -500f, -45f, 45f);

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

        // Draw some axes
        final float[][] axesVertices = new float[][] {
            new float[] {0, 0, 0}, new float[] {100000, 0, 0},
            new float[] {0, 0, 0}, new float[] {0, 100000, 0},
            new float[] {0, 0, 0}, new float[] {0, 0, 100000}
        };
        this.axesHandle = VBOHandler.createNew3D(axesVertices);
        this.axesSize = axesVertices.length * 3;

        final float[][] axesColors = new float[][] {
            new float[] {1, 0, 0}, new float[] {1, 0, 0},
            new float[] {0, 1, 0}, new float[] {0, 1, 0},
            new float[] {0, 0, 1}, new float[] {0, 0, 1}
        };
        this.axesColorHandle = VBOHandler.createNew3D(axesColors);

        // Hide Mouse pointer
        Mouse.setGrabbed(true);

        // Initialize Timer
        Timer.init();
    }

    private void loop() {
        float delta;
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            // Update data
            delta = Timer.getDelta();
            this.camera.update(delta);
            if (Mouse.isGrabbed() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
                Mouse.setGrabbed(false);
            } else if (!Mouse.isGrabbed() && Mouse.isButtonDown(0)) {
                Mouse.setGrabbed(true);
            }

            // Render data
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();
            {
                VBOHandler.render(this.axesHandle, this.axesColorHandle, GL_LINES, this.axesSize);
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
        // Configuration
        Config.SCREEN_WIDTH = 1440;
        Config.SCREEN_HEIGHT = 900;
        Config.SCREEN_RATIO = Config.SCREEN_WIDTH / (float) Config.SCREEN_HEIGHT;
        Config.MAX_FPS = 120;
        Config.VIEW_DISTANCE = 5000;

        Config.HORIZONTAL_FOV = 85;
        Config.VERTICAL_FOV = 95;

        Config.KEY_SPRINT = Keyboard.KEY_LSHIFT;
        Config.KEY_FORWARD = Keyboard.KEY_Z;
        Config.KEY_BACK = Keyboard.KEY_S;
        Config.KEY_LEFT = Keyboard.KEY_Q;
        Config.KEY_RIGHT = Keyboard.KEY_D;
        Config.KEY_UP = Keyboard.KEY_SPACE;
        Config.KEY_DOWN = Keyboard.KEY_LCONTROL;

        try {
            new Main();
        } catch (final LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
