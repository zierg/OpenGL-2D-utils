/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

import java.io.IOException;
import opengl2dutils.test.TestScene2;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 *
 * @author ivko0314
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        OpenGLManager gm = new OpenGLManager();
        gm.setFPS(100).setFullscreen(false).setMouseGrabbed(true).setWindowTitle("Test");
        gm.init();
        gm.setScene(new TestScene2(gm));
        while (!Display.isCloseRequested()) {
            gm.renderScene();
        }
        gm.destroy();
    }

}
