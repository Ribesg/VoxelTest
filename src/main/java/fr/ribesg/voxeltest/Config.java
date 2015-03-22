package fr.ribesg.voxeltest;

import org.lwjgl.input.Keyboard;

/**
 * @author Ribesg
 */
public final class Config {

    public static int   SCREEN_WIDTH;
    public static int   SCREEN_HEIGHT;
    public static float SCREEN_RATIO;
    public static int   MAX_FPS;

    public static float HORIZONTAL_FOV;
    public static float VERTICAL_FOV;
    public static float VIEW_DISTANCE;

    public static int KEY_SPRINT, KEY_FORWARD, KEY_BACK, KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN;

    public static int  OCTREE_DEPTH;
    public static long OCTREE_RADIUS;
    public static int  CHUNK_SIZE;

    public static void init() {
        // Default configuration
        Config.SCREEN_WIDTH = 1440;
        Config.SCREEN_HEIGHT = 900;
        Config.SCREEN_RATIO = Config.SCREEN_WIDTH / (float) Config.SCREEN_HEIGHT;
        Config.MAX_FPS = 120;

        Config.HORIZONTAL_FOV = 85;
        Config.VERTICAL_FOV = 115;
        Config.VIEW_DISTANCE = 5000;

        Config.KEY_SPRINT = Keyboard.KEY_LSHIFT;
        Config.KEY_FORWARD = Keyboard.KEY_Z;
        Config.KEY_BACK = Keyboard.KEY_S;
        Config.KEY_LEFT = Keyboard.KEY_Q;
        Config.KEY_RIGHT = Keyboard.KEY_D;
        Config.KEY_UP = Keyboard.KEY_SPACE;
        Config.KEY_DOWN = Keyboard.KEY_LCONTROL;

        Config.OCTREE_DEPTH = 60;
        Config.OCTREE_RADIUS = Long.MAX_VALUE;
        Config.CHUNK_SIZE = (int) (Config.OCTREE_RADIUS / (2L << (Config.OCTREE_DEPTH - 2)) + 1);

        System.out.println("Configuration initialized");
        System.out.println("\tUsing resolution " + Config.SCREEN_WIDTH + "x" + Config.SCREEN_HEIGHT + " (ratio: " + Config.SCREEN_RATIO + ')');
        System.out.println("\tMax FPS set at " + Config.MAX_FPS);
        System.out.println("\tHorizontal FOV set to " + Config.HORIZONTAL_FOV + ", vertical FOV set to " + Config.VERTICAL_FOV);
        System.out.println("\tView distance is set to " + Config.VIEW_DISTANCE);
        System.out.println("\tChunk size: " + Config.CHUNK_SIZE);
    }
}
