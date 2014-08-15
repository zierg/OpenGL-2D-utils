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
    private final Texture cursor;

    public TestScene2(OpenGLManager gm) {
        this.gm = gm;
        t1 = gm.createTexture("grounds.png", "PNG");
        t2 = gm.createTexture("ship.png", "PNG");
        //gm.drawQuad(500, 500, 0, 0, 0, 0, 0, t2);
        t3 = gm.createTexture(500, 500);
        cursor = gm.createTexture(10, 10);
        gm.drawQuad(4, 10, 0, 0, 0, 0, 0, cursor);
        gm.drawQuad(10, 4, 0, 0, 0, 0, 0, cursor);
        gm.drawQuad(4, 10, 6, 0, 0, 0, 0, cursor);
        gm.drawQuad(2, 8, 1, 1, 255, 255, 255, cursor);
        gm.drawQuad(8, 2, 1, 1, 255, 255, 255, cursor);
        gm.drawQuad(2, 8, 7, 1, 255, 255, 255, cursor);
        gm.setCursor(cursor);
        //gm.drawTexture(t2, 20, 20, t3);
        /*gm.drawTexture(t2, 30, 00, t3);
         gm.drawTexture(t2, 190, 83, t3);*/
        gm.drawTexture(t2, 0, 0, 128, 64, 0, 0, t2.getWidth(), t2.getHeight(), t3);
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
        gm.drawTexture(t1, 0, 0);
        gm.drawQuad(20, 30, 0, 30, 0, 0, 0);
    }

}
