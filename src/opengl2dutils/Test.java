/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

import java.io.IOException;
import opengl2dutils.test.*;
import org.lwjgl.opengl.Display;

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
        gm.setFPS(100).setFullscreen(false).setMouseGrabbed(false).setWindowTitle("Test");
        gm.init();
        
        Texture cursor = gm.createTexture(10, 10);
        gm.drawQuad(4, 10, 0, 0, 0, 0, 0, cursor);
        gm.drawQuad(10, 4, 0, 0, 0, 0, 0, cursor);
        gm.drawQuad(4, 10, 6, 0, 0, 0, 0, cursor);
        gm.drawQuad(2, 8, 1, 1, 255, 255, 255, cursor);
        gm.drawQuad(8, 2, 1, 1, 255, 255, 255, cursor);
        gm.drawQuad(2, 8, 7, 1, 255, 255, 255, cursor);
        gm.setCursor(cursor);
        //gm.setScene(new TestScene2(gm));
        //gm.setScene(new CompositeTextureTestScene(gm));
        gm.setScene(new CompositeTextureTestScene2(gm));
        while (!Display.isCloseRequested()) {
            gm.renderScene();
        }
        gm.destroy();
    }

}
