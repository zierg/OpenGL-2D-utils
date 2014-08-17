/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils.test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import java.io.FileReader;
import java.io.Reader;
import opengl2dutils.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 *
 * @author Ivan
 */
public class CompositeTextureTestScene2 implements GraphicScene {

    private static final int FILE_GROUND_SIZE = 64;
    private static final int GROUND_SIZE = 8;
    private final OpenGLManager gm;
    private final Level level;
    private final CompositeTexture levelTexture;
    private Texture groundsTexture;
    private Texture[] grounds;
    Ground[] groundArray = null;

    public CompositeTextureTestScene2(OpenGLManager gm) {
        this.gm = gm;
        level = LevelCreator.readLevel("level.ini");
        levelTexture = gm.createCompositeTexture();
        loadGrounds();
        loadLevel();

    }

    private void loadGrounds() {
        groundsTexture = gm.createTexture("grounds.png", "PNG");

        {
            XStream xstream = new XStream(new PureJavaReflectionProvider(), new Dom4JDriver());
            // new PureJavaReflectionProvider() - будет использоваться конструктор по умолчанию, чтобы отсутствующие в xml поля не были null
            xstream.processAnnotations(Ground.class);
            Ground[] gr = new Ground[0];
            xstream.alias("Grounds", gr.getClass());
            try {
                Reader reader = new FileReader("Grounds.xml");
                groundArray = (Ground[]) xstream.fromXML(reader);
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        final int GROUND_AMOUNT = 16;

        grounds = new Texture[GROUND_AMOUNT];
        for (Ground ground : groundArray) {
            int textX = ground.col * FILE_GROUND_SIZE;
            int textY = ground.row * FILE_GROUND_SIZE;
            grounds[ground.id] = gm.createTexture(GROUND_SIZE, GROUND_SIZE);
            gm.drawTexture(groundsTexture, 0, 0, GROUND_SIZE, GROUND_SIZE, textX, textY, textX + FILE_GROUND_SIZE, textY + FILE_GROUND_SIZE, grounds[ground.id]);
        }
    }

    private void loadLevel() {
        int i = 0;
        int j = 0;
        for (short[] col : level.grounds) {
            for (short row : col) {
                Ground ground = groundArray[row];
                int textX = ground.col * FILE_GROUND_SIZE;
                int textY = ground.row * FILE_GROUND_SIZE;
                //Texture ground = grounds[row];
                gm.drawTexture(groundsTexture, j * GROUND_SIZE, i * GROUND_SIZE, GROUND_SIZE, GROUND_SIZE, textX, textY, textX + FILE_GROUND_SIZE, textY + FILE_GROUND_SIZE, levelTexture);
                j++;
            }
            i++;
            j = 0;
        }
    }

    @Override
    public void listenKeyboard() {
    }

    @Override
    public void listenMouse() {
        if (Mouse.isButtonDown(0)) {
            System.out.println("x:" + Mouse.getX() + ", y:" + Mouse.getY());
        }
    }
    int x = 0;

    @Override
    public void makeChanges() {
        //x++;
    }

    @Override
    public void render() {
        //gm.drawTexture(levelTexture, x, 0);
        float y = 0;
        final int a = 256 / FILE_GROUND_SIZE;
        int width = 800;
        int height = 600;
        final float varX = 1f / a; // ширина участка земли относительно ширины текстуры
        final float varY = 1f / a; // высота участка земли относительно высоты текстуры

        // Ширина и высота участка земли в пикселях
        float ww = GROUND_SIZE;
        float hh = GROUND_SIZE;

        glEnable(GL_TEXTURE_2D);
        glColor3f(1, 1, 1);
        glViewport(0, 0, width, height);
        glBindTexture(GL_TEXTURE_2D, groundsTexture.getId());
        int i = 0;
        int j = 0;
        for (short[] col : level.grounds) {
            for (short row : col) {
                float coordX = x + j * GROUND_SIZE;
                float coordY = y + i * GROUND_SIZE;
                if (((coordX + GROUND_SIZE < 0)
                        || (coordX > width)
                        || (coordY + GROUND_SIZE < 0)
                        || (coordY > height))) {
                } else {
                    Ground ground = groundArray[row];

                    // Координаты строки и столбца текстуры, в которых находится нужный участок земли 
                    float rowCoord = varX * (ground.col);
                    float colCoord = varY * (ground.row);

                    glBegin(GL_QUADS);
                    {
                        glTexCoord2f(rowCoord, colCoord);
                        glVertex2f(coordX, coordY);
                        glTexCoord2f(rowCoord + varX, colCoord);
                        glVertex2f((coordX + ww), coordY);
                        glTexCoord2f(rowCoord + varX, colCoord + varY);
                        glVertex2f((coordX + ww), coordY + hh);
                        glTexCoord2f(rowCoord, colCoord + varY);
                        glVertex2f(coordX, coordY + hh);
                    }
                    glEnd();
                }
                j++;
            }
            i++;
            j = 0;
        }
        glDisable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
