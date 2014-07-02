/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author ivko0314
 */
public class OpenGLManager implements GraphicManager {

    private boolean isListening = false;
    private int listeningInterval = 10;

    private final Map<Integer, Integer> framebuffers = new HashMap<>();

    @Override
    public void drawTexture(Texture texture, float x, float y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drawTexture(Texture texture, float x, float y, Texture target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY, Texture target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Texture createTexture(int width, int height) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);									// Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);				// make it linear filterd
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);	// Create the texture data
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, textureID, 0); // attach it to the framebuffer

        glViewport(0, 0, width, height);
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // Фоновый серый цвет. Для теста, полностью ли текстура заполняется уровнем (если нет, будет виден фон)
        glClear(GL_COLOR_BUFFER_BIT);			// Clear Screen And Depth Buffer on the fbo to red
        glLoadIdentity();
        OpenGLTexture texture = new OpenGLTexture();
        
        return texture;
    }

    @Override
    public Texture createTexture(String filePath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class ListeningThread extends Thread {
        // Интервал в миллисекундах, через который нужно обновлять игру

        // Последнее время обновления игры
        private long lastTime;

        public ListeningThread() {
            lastTime = getTime();
            start();
        }

        @Override
        public void run() {
            while (isListening) {
                long time = getTime();
                if (time - lastTime >= listeningInterval) {
                    scene.listenMouse();
                    scene.listenKeyboard();
                    lastTime = time;
                }
            }
        }

        /**
         * Получение точного времени в миллисекундах
         *
         * @return
         */
        private long getTime() {
            return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        }
    }

    private boolean fullscreen = false;
    private int width = 640;
    private int height = 480;
    private boolean vSync = false;
    private int fps = 60;
    private boolean mouseGrabbed = false;
    private String title = "Program";
    private GraphicScene scene = null;
    private ListeningThread listeningThread = null;

    public OpenGLManager() {
    }

    @Override
    public void init() {
        try {
            setDisplayMode(width, height, fullscreen);
            Display.setTitle(title);
            Display.setVSyncEnabled(vSync);
            Display.create();
        } catch (LWJGLException e) {
            throw new GraphicException(e);
        }

        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, 1, -1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        Mouse.setGrabbed(mouseGrabbed); // Захватываем мышь.
    }

    @Override
    public void initWithCustomConfiguration(GraphicConfigurator configurator) {
        init();
        configurator.configure();
    }

    @Override
    public GraphicManager setScreenWidth(int width) {
        this.width = width;
        return this;
    }

    @Override
    public GraphicManager setScreenHeight(int height) {
        this.height = height;
        return this;
    }

    @Override
    public GraphicManager setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }

    @Override
    public GraphicManager setFPS(int fps) {
        this.fps = fps;
        return this;
    }

    @Override
    public GraphicManager setVSync(boolean vSync) {
        this.vSync = vSync;
        return this;
    }

    public void renderScene() {
        scene.render();
        Display.update();
        Display.sync(fps);
    }

    @Override
    public GraphicManager setMouseGrabbed(boolean grabbed) {
        mouseGrabbed = grabbed;
        return this;
    }

    @Override
    public GraphicManager setWindowTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public void destroy() {
        Display.destroy();
    }

    @Override
    public void setScene(GraphicScene scene) {
        this.scene = scene;
    }

    @Override
    public GraphicManager setListeningInterval(int interval) {
        listeningInterval = interval;
        return this;
    }

    /**
     * Установка режима экрана
     *
     * @param width ширина
     * @param height высота
     * @param fullscreen полный экран
     */
    private void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i = 0; i < modes.length; i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the 
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    @Override
    public void startListeningThread() {
        if (isListening) {
            return;
        }
        isListening = true;
        if (listeningThread == null) { // На всякий случай
            listeningThread = new ListeningThread();
        }
    }

    @Override
    public void stopListeningThread() {
        if (!isListening) {
            return;
        }
        isListening = false;
        listeningThread = null;
    }
}
