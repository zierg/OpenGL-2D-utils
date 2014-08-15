/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

import java.io.IOException;
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

/**
 *
 * @author ivko0314
 */
public class OpenGLManager {

    private Texture cursor;

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
                    scene.makeChanges();
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
        if (texture == null) {
            return;
        }
        int textWidth = texture.getWidth();
        int textHeight = texture.getHeight();
        drawTexture(texture, x, y, textWidth, textHeight, 0, 0, textWidth, textHeight);
    }

    public void drawTexture(Texture texture, float x, float y, Texture target) {
        if (texture == null || target == null) {
            return;
        }
        int textWidth = texture.getWidth();
        int textHeight = texture.getHeight();
        drawTexture(texture, x, y, textWidth, textHeight, 0, 0, textWidth, textHeight, target);
    }

    public void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY) {
        if (texture == null) {
            return;
        }
        drawTexture(texture, x, y, texture.getWidth(), texture.getHeight(), fromX, fromY, toX, toY);
    }

    public void drawTexture(Texture texture, float x, float y, float fromX, float fromY, float toX, float toY, Texture target) {
        if (texture == null || target == null) {
            return;
        }
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
            glBindTexture(GL_TEXTURE_2D, 0);
            Texture texture = new Texture(tex.getTextureID(), tex.getTextureWidth(), tex.getTextureHeight());
            return texture;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public CompositeTexture createCompositeTexture() {
        return new CompositeTexture(this);
    }
    
    public void drawTexture(CompositeTexture texture, float x, float y) {
        texture.draw(x, y);
    }
    
    public void drawTexture(Texture texture, float x, float y, CompositeTexture target) {
        target.addTexture(texture, x, y);
    }
    
    public void drawTexture(Texture texture, float x, float y, float width, float height, CompositeTexture target) {
        target.addTexture(texture, x, y, width, height);
    }
    
    public void drawTexture(Texture texture, float x, float y, float width, float height, float fromX, float fromY, float toX, float toY, CompositeTexture target) {
        target.addTexture(texture, x, y, width, height, fromX, fromY, toX, toY);
    }
    
    public void drawTexture(CompositeTexture texture, float x, float y, Texture target) {
        texture.draw(x, y, target);
    }

    public void deleteTexture(Texture texture) {
        if (texture == null) {
            return;
        }
        glDeleteTextures(texture.getId());
    }

    public void drawTexture(Texture texture, float x, float y, float width, float height) {
        if (texture == null) {
            return;
        }
        drawTexture(texture, x, y, width, height, 0, 0, width, height);
    }

    public void drawTexture(Texture texture, float x, float y, float width, float height, Texture target) {
        if (texture == null || target == null) {
            return;
        }
        System.out.println("with wh. width = " + width + ", height = " + height);
        drawTexture(texture, x, y, width, height, 0, 0, width, height, target);
    }

    public void drawTexture(Texture texture, float x, float y, float width, float height, float fromX, float fromY, float toX, float toY) {
        if (texture == null) {
            return;
        }
        glEnable(GL_TEXTURE_2D);
        glColor3f(1, 1, 1);
        glViewport(0, 0, this.width, this.height);
        glScalef(1.0f, 1.0f, 1.0f);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        float textureWidth = texture.getWidth();
        float textureHeight = texture.getHeight();
        float xBegin = fromX / textureWidth;
        float yBegin = fromY / textureHeight;
        float xEnd = toX / textureWidth;
        float yEnd = toY / textureHeight;
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
        if (texture == null || target == null) {
            return;
        }
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
        float textureWidth = texture.getWidth();
        float textureHeight = texture.getHeight();
        float xBegin = fromX / textureWidth;
        float yBegin = fromY / textureHeight;
        float xEnd = toX / textureWidth;
        float yEnd = toY / textureHeight;

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

    public void drawQuad(int quadWidth, int quadHeight, float x, float y, int red, int green, int blue) {
        final float COLOR_MAX = 255;
        glBindTexture(GL_TEXTURE_2D, 0);
        glEnable(GL_TEXTURE_2D);
        glViewport(0, 0, this.width, this.height);
        glScalef(1.0f, 1.0f, 1.0f);
        glColor3f((float) red / COLOR_MAX, (float) green / COLOR_MAX, (float) blue / COLOR_MAX);
        glBegin(GL_QUADS);
        {
            glVertex2f(x, /*height - */ y);
            glVertex2f(x + quadWidth, /*height - */ y);
            glVertex2f(x + quadWidth, /*height - */ (y + quadHeight));
            glVertex2f(x, /*height - */ (y + quadHeight));
        }
        glEnd();
        glDisable(GL_QUADS);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void drawQuad(int quadWidth, int quadHeight, float x, float y, RGBColor color) {
        drawQuad(quadWidth, quadHeight, x, y, color.getRed(), color.getGreen(), color.getBlue());
    }

    public void drawQuad(int quadWidth, int quadHeight, float x, float y, int red, int green, int blue, Texture target) {
        if (target == null) {
            return;
        }
        final float COLOR_MAX = 255;
        int framebufferID;
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

        glBindTexture(GL_TEXTURE_2D, 0);

        glViewport(0, 0, target.getWidth(), target.getHeight());

        float floatTargetWidth = target.getWidth();
        float floatWidth = this.width;
        float floatTargetHeight = target.getHeight();
        float floatHeight = this.height;
        glScalef(floatWidth / floatTargetWidth, floatHeight / floatTargetHeight, 1.0f);

        final int yOffset = 0;
        float scaleX = floatTargetWidth / floatWidth; // ???????
        float scaleY = floatTargetHeight / floatHeight; // ???????

        glEnable(GL_TEXTURE_2D);
        glViewport(0, 0, this.width, this.height);

        glColor3f((float) red / COLOR_MAX, (float) green / COLOR_MAX, (float) blue / COLOR_MAX);
        glBegin(GL_QUADS);
        {
            glVertex2f(x * scaleX, target.getHeight() - (y - yOffset) * scaleY); // ???????
            glVertex2f((x + quadWidth) * scaleX, target.getHeight() - (y - yOffset) * scaleY); // ???????
            glVertex2f((x + quadWidth) * scaleX, target.getHeight() - ((y + quadHeight) - yOffset) * scaleY); // ???????
            glVertex2f(x * scaleX, target.getHeight() - ((y + quadHeight) - yOffset) * scaleY); // ???????
        }
        glEnd();

        glScalef(floatTargetWidth / floatWidth, floatTargetHeight / floatHeight, 1.0f);//glScalef(1.0f, 1.0f, 1.0f); // Меняем масштаб обратно
        glDisable(GL_QUADS);
        glBindTexture(GL_TEXTURE_2D, 0);

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glViewport(0, 0, this.width, this.height);
    }

    public void drawQuad(int quadWidth, int quadHeight, float x, float y, RGBColor color, Texture target) {
        drawQuad(quadWidth, quadHeight, x, y, color.getRed(), color.getGreen(), color.getBlue(), target);
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
        glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        glClear(GL_COLOR_BUFFER_BIT);
        scene.render();
        if (mouseGrabbed) {
            drawTexture(cursor, Mouse.getX(), height - Mouse.getY());
        }
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

    public void setCursor(Texture cursor) {
        this.cursor = cursor;
    }
    
    int getWidth() {
        return width;
    }
    
    int getHeight() {
        return height;
    }
}
