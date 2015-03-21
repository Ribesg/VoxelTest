package fr.ribesg.voxeltest;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Ribesg
 */
public final class CameraController {

    public float posX, posY, posZ, yaw, pitch, speed, fastSpeed, mouseSensitivity;

    public CameraController(final float posX, final float posY, final float posZ, final float yaw, final float pitch, final float speed, final float mouseSensitivity) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.speed = speed;
        this.fastSpeed = speed * 5;
        this.mouseSensitivity = mouseSensitivity;
    }

    public CameraController() {
        this(0, 0, 0, 0, 0, .05f, .05f);
    }

    public void update(final float delta) {
        // Update horizontal view angle
        this.yaw += Mouse.getDX() * this.mouseSensitivity;

        // Update vertical view angle
        this.pitch -= Mouse.getDY() * this.mouseSensitivity;

        final float distance;
        if (Keyboard.isKeyDown(Config.KEY_SPRINT)) {
            distance = this.fastSpeed * delta;
        } else {
            distance = this.speed * delta;
        }
        if (Keyboard.isKeyDown(Config.KEY_FORWARD)) {
            // Move forward
            this.posX -= distance * Math.sin(Math.toRadians(this.yaw));
            this.posY += distance * Math.tan(Math.toRadians(this.pitch));
            this.posZ += distance * Math.cos(Math.toRadians(this.yaw));
        }
        if (Keyboard.isKeyDown(Config.KEY_BACK)) {
            // Move backward
            this.posX += distance * Math.sin(Math.toRadians(this.yaw));
            this.posY -= distance * Math.tan(Math.toRadians(this.pitch));
            this.posZ -= distance * Math.cos(Math.toRadians(this.yaw));
        }
        if (Keyboard.isKeyDown(Config.KEY_LEFT)) {
            // Strafe left
            this.posX -= distance * Math.sin(Math.toRadians(this.yaw - 90));
            this.posZ += distance * Math.cos(Math.toRadians(this.yaw - 90));
        }
        if (Keyboard.isKeyDown(Config.KEY_RIGHT)) {
            // Strafe right
            this.posX -= distance * Math.sin(Math.toRadians(this.yaw + 90));
            this.posZ += distance * Math.cos(Math.toRadians(this.yaw + 90));
        }
        if (Keyboard.isKeyDown(Config.KEY_UP)) {
            this.posY -= distance;
        }
        if (Keyboard.isKeyDown(Config.KEY_DOWN)) {
            this.posY += distance;
        }
    }

    public void lookThrough() {
        glLoadIdentity();
        glRotatef(this.pitch, 1.0f, 0.0f, 0.0f);
        glRotatef(this.yaw, 0.0f, 1.0f, 0.0f);
        glTranslatef(this.posX, this.posY, this.posZ);
    }
}
