/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

import java.io.IOException;
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
        GraphicManager gm = new OpenGLManager();
        gm.setFPS(100).setFullscreen(false).setMouseGrabbed(false).setWindowTitle("asd");
        gm.init();
        Texture t1 = gm.createTexture("grounds.png", "PNG");
        Texture t2 = gm.createTexture("ship.png", "PNG");
        Texture t3 = gm.createTexture(500, 500);
        //gm.drawTexture(t2, 20, 20, t3);
        /*gm.drawTexture(t2, 30, 00, t3);
        gm.drawTexture(t2, 190, 83, t3);*/
        gm.drawTexture(t2, 0, 0, 128,64,0,0,500,200,t3);
        gm.drawTexture(t3,0, 0,t1);
        //GL11.glScalef(2, 2, 2);
        while (!Display.isCloseRequested()) {
            //glEnable(GL_TEXTURE_2D);

            glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
            glClear(GL_COLOR_BUFFER_BIT);
            gm.drawTexture(t1, 0, 0);
            //glDisable(GL_TEXTURE_2D);
            Display.update();
            Display.sync(100);
        }
    }

}
