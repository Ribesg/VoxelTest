package fr.ribesg.voxeltest;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

/**
 * @author Ribesg
 */
public final class Timer {

    private static long lastFrame;

    private static int  fps;
    private static long lastFps;

    public static void init() {
        Timer.lastFrame = Timer.getTime();
        Timer.lastFps = Timer.getTime();
    }

    public static int getDelta() {
        final long currentTime = Timer.getTime();
        final int delta = (int) (currentTime - Timer.lastFrame);
        Timer.lastFrame = currentTime;
        return delta;
    }

    public static void updateFps() {
        if (Timer.getTime() - Timer.lastFps > 1000) {
            Display.setTitle("FPS: " + Timer.fps);
            Timer.fps = 0;
            Timer.lastFps = Timer.getTime();
        }
        Timer.fps++;
    }

    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
}
