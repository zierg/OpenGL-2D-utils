/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils.test;

import opengl2dutils.*;
import org.lwjgl.input.Mouse;

/**
 *
 * @author ivko0314
 */
public class TestScene2 implements GraphicScene {

    private final OpenGLManager gm;
    private final Texture t1;
    private final Texture t2;
    private final Texture t3;
    

    public TestScene2(OpenGLManager gm) {
        this.gm = gm;
        t1 = gm.createTexture("grounds.png", "PNG");
        t2 = gm.createTexture("ship.png", "PNG");
        //gm.drawQuad(500, 500, 0, 0, 0, 0, 0, t2);
        t3 = gm.createTexture(500, 500);
        
        //gm.drawTexture(t2, 20, 20, t3);
        /*gm.drawTexture(t2, 30, 00, t3);
         gm.drawTexture(t2, 190, 83, t3);*/
        gm.drawTexture(t2, 0, 0, 28, 20, 50, 20, t2.getWidth()-50, t2.getHeight()-20, t3);
        gm.drawTexture(t3, 0, 0, t1);
        //GL11.glScalef(2, 2, 2);
        RGBColor color = new RGBColor();
        color.setRed((int) (Math.random() * 255));
        color.setGreen((int) (Math.random() * 255));
        color.setBlue((int) (Math.random() * 255));
        gm.drawQuad(20, 30, 20, 30, color.getRed(), color.getGreen(), color.getBlue(), t1);
    }

    @Override
    public void listenKeyboard() {

    }

    @Override
    public void listenMouse() {

    }

    @Override
    public void makeChanges() {

    }

    @Override
    public void render() {
        gm.drawTexture(t3, 0, 0);
        gm.drawQuad(20, 30, 0, 30, 0, 0, 0);
    }

}
