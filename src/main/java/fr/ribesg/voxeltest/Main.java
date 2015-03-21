package fr.ribesg.voxeltest;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Arrays;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * @author Ribesg
 */
public final class Main {

    public static final Random RANDOM = new Random();

    private final CameraController camera;

    private float[][] points;

    public Main() throws LWJGLException {
        Config.SCREEN_WIDTH = 1440;
        Config.SCREEN_HEIGHT = 900;
        Config.MAX_FPS = 240;
        Config.VIEW_DISTANCE = 5000;

        Config.KEY_SPRINT = Keyboard.KEY_LSHIFT;
        Config.KEY_FORWARD = Keyboard.KEY_Z;
        Config.KEY_BACK = Keyboard.KEY_S;
        Config.KEY_LEFT = Keyboard.KEY_Q;
        Config.KEY_RIGHT = Keyboard.KEY_D;
        Config.KEY_UP = Keyboard.KEY_SPACE;
        Config.KEY_DOWN = Keyboard.KEY_LCONTROL;

        this.camera = new CameraController();

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
        gluPerspective((float) 60, Config.SCREEN_WIDTH / Config.SCREEN_HEIGHT, 0.001f, Config.VIEW_DISTANCE);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);

        // Create some points
        this.points = new float[10000][3];
        Arrays.stream(this.points).forEach(p -> {
            p[0] = (Main.RANDOM.nextFloat() - 0.5f) * 250f;
            p[1] = (Main.RANDOM.nextFloat() - 0.5f) * 250f;
            p[2] = Main.RANDOM.nextInt(400) - 400;
        });

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
            this.camera.lookThrough();

            // Render data
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glBegin(GL_POINTS);
            {
                glColor3f(1, 1, 1);
                Arrays.stream(this.points).forEach(p -> glVertex3f(p[0], p[1], p[2]));
            }
            glEnd();
            glBegin(GL_LINES);
            {
                glColor3f(1, 0, 0);
                glVertex3f(0, 0, 0);
                glVertex3f(1000, 0, 0);
                glColor3f(0, 1, 0);
                glVertex3f(0, 0, 0);
                glVertex3f(0, 1000, 0);
                glColor3f(0, 0, 1);
                glVertex3f(0, 0, 0);
                glVertex3f(0, 0, 1000);
            }
            glEnd();

            // Update screen
            Timer.updateFps();
            Display.update();
            Display.sync(Config.MAX_FPS);
        }
    }

    private void destroy() {
        Display.destroy();
    }

    // ##################################################################### //

    public static void main(final String[] args) {
        try {
            new Main();
        } catch (final LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
