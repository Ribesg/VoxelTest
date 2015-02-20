package fr.ribesg.voxeltest;

import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * @author Ribesg
 */
public final class X {

    public static final Random RANDOM = new Random();

    public static final int    WINDOW_WIDTH  = 775;
    public static final int    WINDOW_HEIGHT = 775;
    public static final int    WINDOW_BITS   = 32;
    public static final String WINDOW_TITLE  = "Title";
    public static final int    FIXED_FPS     = 60;

    public static final float BLOCK_SIZE = 1f;
    public static final int   CHUNK_SIZE = 64;

    public static int     QUADS_OR_LINES = GL11.GL_QUADS;
    public static boolean USE_VBO        = false;
}
