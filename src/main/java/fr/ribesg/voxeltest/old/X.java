package fr.ribesg.voxeltest.old;

import java.util.Random;

/**
 * @author Ribesg
 */
public final class X {

    public static final Random RANDOM = new Random();

    public static final int    WINDOW_WIDTH  = 768;
    public static final int    WINDOW_HEIGHT = 768;
    public static final int    WINDOW_BITS   = 32;
    public static final String WINDOW_TITLE  = "Title";
    public static final int    FIXED_FPS     = 60;

    public static final int CHUNK_SIZE = 64;

    public static float[][] VERTICES_TABLE = new float[][]{
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
}
