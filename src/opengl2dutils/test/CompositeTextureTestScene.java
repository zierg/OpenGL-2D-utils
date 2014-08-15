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
    
    public CompositeTextureTestScene(OpenGLManager gm) {
        this.gm = gm;
        texture = gm.createCompositeTexture();
        Texture t1 = gm.createTexture("grounds.png", "PNG");
        Texture t2 = gm.createTexture("ship.png", "PNG");
        Texture t3 = gm.createTexture(500, 500);
        gm.drawTexture(t2, 50, 50, 128, 64, 0, 0, t2.getWidth()-80, t2.getHeight(), t3);
        gm.drawTexture(t3, 0, 0, t1);
        texture.addTexture(t1, 0, 0);
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
        gm.drawCompositeTexture(texture, 50, 50);
    }
    
}
