/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivko0314
 */
public class CompositeTexture {

    private static final class TextureInfo {

        TextureInfo(Texture texture, float x, float y) {
            this(texture, x, y, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight());
        }

        TextureInfo(Texture texture, float x, float y, float width, float height) {
            this(texture, x, y, width, height, 0, 0, width, height);
        }

        TextureInfo(Texture texture, float x, float y, float width, float height, float fromX, float fromY, float toX, float toY) {
            this.texture = texture;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        final Texture texture;
        final float x;
        final float y;
        final float width;
        final float height;
        final float fromX;
        final float fromY;
        final float toX;
        final float toY;
    }

    private final OpenGLManager gm;
    private float scale = 1.5f;

    private final List<TextureInfo> textures = new ArrayList<>();

    CompositeTexture(OpenGLManager gm) {
        this.gm = gm;
    }
    
    public void clear() {
        textures.clear();
    }
    
    public void clearAndDeleteTextures() {
        for (TextureInfo t : textures) {
            gm.deleteTexture(t.texture);
        }
        textures.clear();
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }
    
    public float getScale() {
        return scale;
    }
    
    void addTexture(Texture texture, float x, float y) {
        textures.add(new TextureInfo(texture, x, y));
    }
    
    void addTexture(Texture texture, float x, float y, float width, float height) {
        textures.add(new TextureInfo(texture, x, y, width, height));
    }
    
    void addTexture(Texture texture, float x, float y, float width, float height, float fromX, float fromY, float toX, float toY) {
        textures.add(new TextureInfo(texture, x, y, width, height, fromX, fromY, toX, toY));
    }

    void draw(float x, float y) {
        int width = gm.getWidth();
        int height = gm.getHeight();
        for (TextureInfo t : textures) {
            float coordX = (x + (int)((t.x) * scale));
            float coordY = (y + (int)((t.y) * scale));
            float tWidth =  (t.width  * scale) ;
            float tHeight =  (t.height * scale);
            if (!(
                    (coordX + tWidth < 0) ||
                    (coordX > width) ||
                    (coordY + tHeight < 0) ||
                    (coordY > height)
                    )) {
                gm.drawTexture(t.texture, coordX, coordY, tWidth, tHeight, t.fromX, t.fromY, t.toX, t.toY);
            }
        }
    }

    void draw(float x, float y, Texture target) {
        int width = target.getWidth();
        int height = target.getHeight();
        for (TextureInfo t : textures) {
            float coordX = x + t.x;
            float coordY = y + t.y;
            if (!(
                    (coordX + t.width < 0) ||
                    (coordX > width) ||
                    (coordY + t.height < 0) ||
                    (coordY > height)
                    )) {
                gm.drawTexture(t.texture, coordX, coordY, t.width, t.height, t.fromX, t.fromY, t.toX, t.toY, target);
            }
        }
    }
}
