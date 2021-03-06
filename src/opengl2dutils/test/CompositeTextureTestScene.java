/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl2dutils.test;

import opengl2dutils.*;

/**
 *
 * @author ivko0314
 */
public class CompositeTextureTestScene implements GraphicScene {
    
    private final OpenGLManager gm;
    
    private final CompositeTexture texture;
    private final Texture texture2;
    
    public CompositeTextureTestScene(OpenGLManager gm) {
        this.gm = gm;
        texture = gm.createCompositeTexture();
        Texture t1 = gm.createTexture("grounds.png", "PNG");
        Texture t2 = gm.createTexture("ship.png", "PNG");
        gm.drawTexture(t1, 0, 0, texture);
        gm.drawTexture(t2, 50, 50, 128, 64, 0, 0, t2.getWidth()-80, t2.getHeight(), texture);
        texture2 = gm.createTexture(300, 180);
        gm.drawTexture(texture, 0, 0, texture2);
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
        gm.drawTexture(texture2, 50, 50);
    }
    
}
