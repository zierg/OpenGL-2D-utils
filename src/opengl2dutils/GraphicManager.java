/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl2dutils;

/**
 *
 * @author ivko0314
 */
public interface GraphicManager {
    void init();
    void initWithCustomConfiguration(GraphicConfigurator configurator);
    void renderScene();
    void destroy();
    void setScene(GraphicScene scene);
    void startListeningThread();
    void stopListeningThread();
    GraphicManager setListeningInterval(int interval);
    GraphicManager setScreenWidth(int width);
    GraphicManager setScreenHeight(int height);
    GraphicManager setFullscreen(boolean fullscreen);
    GraphicManager setFPS(int FPS);
    GraphicManager setVSync(boolean vSync);
    GraphicManager setMouseGrabbed(boolean grabbed);
    GraphicManager setWindowTitle(String title);
    Texture createTexture(int width, int height);
    Texture createTexture(String filePath, String type);
    void deleteTexture(Texture texture);
    
    void drawTexture(Texture texture, float x, float y);
    void drawTexture(Texture texture, float x, float y, Texture target);
    void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY);
    void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY, Texture target);
}
