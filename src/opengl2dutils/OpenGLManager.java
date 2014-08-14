/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.BufferUtils;
import static org.lwjgl.util.glu.GLU.gluBuild2DMipmaps;
import org.newdawn.slick.opengl.TextureImpl;

/**
 *
 * @author ivko0314
 */
public class OpenGLManager {

    private boolean isListening = false;
    private int listeningInterval = 10;

    private boolean fullscreen = false;
    private int width = 800;
    private int height = 600;
    private boolean vSync = false;
    private int fps = 60;
    private boolean mouseGrabbed = false;
    private String title = "Program";
    private GraphicScene scene = null;
    private ListeningThread listeningThread = null;

    private final Map<Integer, Integer> framebuffers = new HashMap<>();

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

    public OpenGLManager() {
    }
    
    public void drawTexture(Texture texture, float x, float y) {
        int textWidth = texture.getWidth();
        int textHeight = texture.getHeight();
        drawTexture(texture, x, y, textWidth, textHeight, 0, 0, textWidth, textHeight);
    }

    public void drawTexture(Texture texture, float x, float y, Texture target) {
        int textWidth = texture.getWidth();
        int textHeight = texture.getHeight();
        System.out.println("without wh. width = " + textWidth + ", height = " + textHeight);
        drawTexture(texture, x, y, textWidth, textHeight, 0, 0, textWidth, textHeight, target);
    }

    public void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY) {
        drawTexture(texture, x, y, texture.getWidth(), texture.getHeight(), fromX, fromY, toX, toY);
    }

    public void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY, Texture target) {
        drawTexture(texture, x, y, texture.getWidth(), texture.getHeight(), fromX, fromY, toX, toY, target);
    }

    public Texture createTexture(int width, int height) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);									// Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);				// make it linear filterd
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);	// Create the texture data

        glViewport(0, 0, width, height);
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // Фоновый серый цвет. Для теста, полностью ли текстура заполняется уровнем (если нет, будет виден фон)
        glClear(GL_COLOR_BUFFER_BIT);			// Clear Screen And Depth Buffer on the fbo to red
        glLoadIdentity();
        glBindTexture(GL_TEXTURE_2D, 0);

        Texture texture = new Texture(textureID, width, height);
        return texture;
    }

    public Texture createTexture(String filePath, String type) {
        try {
            org.newdawn.slick.opengl.Texture tex = TextureLoader.getTexture(type, ResourceLoader.getResourceAsStream(filePath));
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            //glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
            
            //new TextureImpl(type, height, width);
            /*glMatrixMode(GL_MODELVIEW);	
            glLoadIdentity();*/
            glBindTexture(GL_TEXTURE_2D, 0);
            Texture texture = new Texture(tex.getTextureID(), tex.getTextureWidth(), tex.getTextureHeight());
            return texture;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteTexture(Texture texture) {
        glDeleteTextures(texture.getId());
    }

    public void drawTexture(Texture texture, float x, float y, float width, float height) {
        drawTexture(texture, x, y, width, height, 0, 0, width, height);
    }

    public void drawTexture(Texture texture, float x, float y, float width, float height, Texture target) {
        System.out.println("with wh. width = " + width + ", height = " + height);
        drawTexture(texture, x, y, width, height, 0, 0, width, height, target);
    }

    public void drawTexture(Texture texture, float x, float y, float width, float height, float fromX, float fromY, float toX, float toY) {
        glEnable(GL_TEXTURE_2D);
        glColor3f(1, 1, 1);
        glViewport(0, 0, this.width, this.height);
        glScalef(1.0f, 1.0f, 1.0f);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        float xBegin = fromX / width;
        float yBegin = fromY / height;
        float xEnd = toX / width;
        float yEnd = toY / height;
        width = (toX - fromX);
        height = (toY - fromY);
        //System.out.println("width = " + width + ", height = " + height);
        glBegin(GL_QUADS);
        {
            glTexCoord2f(xBegin, yBegin);
            glVertex2f(x, y);
            glTexCoord2f(xEnd, yBegin);
            glVertex2f((x + width), y);
            glTexCoord2f(xEnd, yEnd);
            glVertex2f((x + width), (y + height));
            glTexCoord2f(xBegin, yEnd);
            glVertex2f(x, (y + height));
        }
        glEnd();
        glDisable(GL_QUADS);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void drawTexture(Texture texture, float x, float y, float width, float height, float fromX, float fromY, float toX, float toY, Texture target) {
        int framebufferID;
        int textureID = texture.getId();
        int targetID = target.getId();
        if (!framebuffers.containsKey(targetID)) {      // Если не было фреймбуфера, создаём
            framebufferID = glGenFramebuffersEXT();     // и привязываем к нему текстуру,
            framebuffers.put(targetID, framebufferID);  // иначе используем имеющийся
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
            glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, targetID, 0);
        } else {
            framebufferID = framebuffers.get(targetID);
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
        }

        glEnable(GL_TEXTURE_2D);
        glColor3f(1, 1, 1);

        glBindTexture(GL_TEXTURE_2D, textureID);

        glViewport(0, 0, target.getWidth(), target.getHeight());

        float floatTargetWidth = target.getWidth();
        float floatWidth = this.width;
        float floatTargetHeight = target.getHeight();
        float floatHeight = this.height;
        glScalef(floatWidth / floatTargetWidth, floatHeight / floatTargetHeight, 1.0f);

        final int yOffset = 0;
        float xBegin = fromX / width;
        float yBegin = fromY / height;
        float xEnd = toX / width;
        float yEnd = toY / height;
        width = toX - fromX;
        height = (int) (toY - fromY);

        glBegin(GL_QUADS);
        {
            glTexCoord2f(xBegin, yBegin);
            glVertex2f(x, target.getHeight() - y - yOffset);
            glTexCoord2f(xEnd, yBegin);
            glVertex2f((x + width), target.getHeight() - y - yOffset);
            glTexCoord2f(xEnd, yEnd);
            glVertex2f((x + width), target.getHeight() - (y + height) - yOffset);
            glTexCoord2f(xBegin, yEnd);
            glVertex2f(x, target.getHeight() - (y + height) - yOffset);
        }
        glEnd();
        glScalef(floatTargetWidth / floatWidth, floatTargetHeight / floatHeight, 1.0f);//glScalef(1.0f, 1.0f, 1.0f); // Меняем масштаб обратно
        glDisable(GL_QUADS);
        glBindTexture(GL_TEXTURE_2D, 0);

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glViewport(0, 0, this.width, this.height);
    }

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

    public void initWithCustomConfiguration(GraphicConfigurator configurator) {
        configurator.configure();
    }

    public OpenGLManager setScreenWidth(int width) {
        this.width = width;
        return this;
    }

    public OpenGLManager setScreenHeight(int height) {
        this.height = height;
        return this;
    }

    public OpenGLManager setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }

    public OpenGLManager setFPS(int fps) {
        this.fps = fps;
        return this;
    }

    public OpenGLManager setVSync(boolean vSync) {
        this.vSync = vSync;
        return this;
    }

    public void renderScene() {
        scene.render();
        Display.update();
        Display.sync(fps);
    }

    public OpenGLManager setMouseGrabbed(boolean grabbed) {
        mouseGrabbed = grabbed;
        return this;
    }

    public OpenGLManager setWindowTitle(String title) {
        this.title = title;
        return this;
    }

    public void destroy() {
        Display.destroy();
    }

    public void setScene(GraphicScene scene) {
        this.scene = scene;
    }

    public OpenGLManager setListeningInterval(int interval) {
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

    public void startListeningThread() {
        if (isListening) {
            return;
        }
        isListening = true;
        if (listeningThread == null) { // На всякий случай
            listeningThread = new ListeningThread();
        }
    }

    public void stopListeningThread() {
        if (!isListening) {
            return;
        }
        isListening = false;
        listeningThread = null;
    }
}
