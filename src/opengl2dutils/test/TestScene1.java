/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils.test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import opengl2dutils.*;

/**
 *
 * @author ivko0314
 */
public class TestScene1 implements GraphicScene {

    private final OpenGLManager gm;

    private static final int BAR_HEIGHT = 20; // Высота заголовка
    private int windowXPosBeforePress;
    private int windowYPosBeforePress;
    //private static Window draggingWindow = null; // Перетаскиваемое окно

    private /*static /*final*/ int GROUND_SIZE = 32; // Размер участка земли
    private static final int FILE_GROUND_SIZE = 128; // Размер участка земли в файле grounds.png

    // Звуки
    /*private static Audio oggStream; // Канал ogg-файла
     private static Audio wavEffect; // Канал wav-файла
     private static Audio groundSound; // Канал wav-файла*/

    /*private static UnicodeFont popupFont;  // Шрифт для всплывающего окна
     private static UnicodeFont otherFont;  // Другой шрифт*/
    private static final int FPS = 100;  // Количество кадров в секунду
    // Скорость перемещения по уровню при помощи мыши по вертикали/горизонтали
    private static final int VERTICAL_MOUSE_SPEED = 20;
    private static final int HORIZONTAL_MOUSE_SPEED = 20;
    private static final int MOUSE_OFFSET = 100; // Максимальное смещение "камеры" при достижении края уровня
    private boolean isLeftButtonPressed = false;
    private boolean isRightButtonPressed = false;
    private int mouseXBeforePress;
    private int mouseYBeforePress;
    // Размеры окна
    /*private final static int width = Display.getDesktopDisplayMode().getWidth();
     private final static int height = Display.getDesktopDisplayMode().getHeight();*/
    private final  int width;
    private final  int height;
    private static final boolean fullscreen = false;

    private Texture groundsTexture; // Текстура с участками земли
    private Ground[] grounds; // Массив участков земли
    private Level level = null; // Уровень.
    // Размеры уровня (в пикселях)
    private int levelWidth;
    private int levelHeight;
    private float levelXPosition;
    private float levelYPosition;

    // Персонаж
    private int characterColumn;
    private int characterRow;
    private float characterOffsetX;
    private float characterOffsetY;
    private int characterGoalColumn;
    private int characterGoalRow;
    private static final float CHARACTER_SPEED = 0.05f;

    public TestScene1(Level level, OpenGLManager gm, int width, int height) {
        this.gm = gm;
        this.width = width;
        this.height = height;
        loadGroundTexture();
        this.level = level;
        resetLevelSize();
        levelXPosition = width / 2 - levelWidth / 2;
        levelYPosition = height / 2 - levelHeight / 2;
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
        
    }

    private void resetLevelSize() {
        levelWidth = level.grounds[0].length * GROUND_SIZE;
        levelHeight = level.grounds.length * GROUND_SIZE;
    }

    /**
     * Загрузка текстуры участков земли, загрузка массива участков земли
     *
     * @throws IOException
     */
    private void loadGroundTexture() {
        groundsTexture = gm.createTexture("resources\\grounds.png", "PNG");
        XStream xstream = new XStream(new PureJavaReflectionProvider(), new Dom4JDriver());
        // new PureJavaReflectionProvider() - будет использоваться конструктор по умолчанию, чтобы отсутствующие в xml поля не были null
        xstream.processAnnotations(Ground.class);
        Ground[] gr = new Ground[0];
        xstream.alias("Grounds", gr.getClass());
        try (Reader reader = new FileReader("Grounds.xml")) {
            grounds = (Ground[]) xstream.fromXML(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
}
