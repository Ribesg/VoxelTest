package fr.ribesg.voxeltest;

import fr.ribesg.voxeltest.collection.qtree.QTree;
import fr.ribesg.voxeltest.collection.qtree.QTreeLeaf;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Color;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Ribesg
 */
public class QTreeTestMain {

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

        // Data init
        final int depth = 8, radius = 400;
        final QTree<Color> qTree = new QTree<>(depth, radius);
        for (int x = -radius; x < radius; x += radius / (depth + 1)) {
            for (int y = -radius; y < radius; y += radius / (depth + 1)) {
                qTree.set(x, y, new Color(
                    Math.round(Math.abs(x) / (float) radius * 255),
                    Math.round((400 - Math.abs((x + y) / 2)) / (float) radius * 255),
                    Math.round(Math.abs(y) / (float) radius * 255)
                ));
            }
        }

        // Main loop
        glTranslatef(X.WINDOW_WIDTH / 2, X.WINDOW_HEIGHT / 2, 0);
        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT);

            for (int x = -radius; x < radius; x += radius / (depth + 1)) {
                for (int y = -radius; y < radius; y += radius / (depth + 1)) {
                    final QTreeLeaf<Color> leaf = qTree.get(x, y);
                    final float cx = (float) leaf.getCenterX();
                    final float cy = (float) leaf.getCenterY();
                    final Color color = leaf.getData();
                    final float size = 2f;
                    glBegin(GL_QUADS);
                    {
                        glColor3f(
                            color.getRed() / 255f,
                            color.getGreen() / 255f,
                            color.getBlue() / 255f
                                 );
                        glVertex2f(cx - size, cy - size);
                        glVertex2f(cx - size, cy + size);
                        glVertex2f(cx + size, cy + size);
                        glVertex2f(cx + size, cy - size);
                    }
                    glEnd();
                }
            }

            Display.update();
            Display.sync(60);
            Timer.updateFps();
        }

        Display.destroy();
        System.exit(0);
    }
}
