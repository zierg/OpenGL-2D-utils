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

/**
 *
 * @author Ivan
 */
public class CompositeTextureTestScene2 implements GraphicScene {

    private static final int FILE_GROUND_SIZE = 64;
    private static final int GROUND_SIZE = 32;
    private final OpenGLManager gm;
    private final Level level;
    private final CompositeTexture levelTexture;
    private Texture[] grounds;

    public CompositeTextureTestScene2(OpenGLManager gm) {
        this.gm = gm;
        level = LevelCreator.readLevel("level.ini");
        levelTexture = gm.createCompositeTexture();
        loadGrounds();
        loadLevel();

    }

    private void loadGrounds() {
        Texture groundsTexture = gm.createTexture("grounds.png", "PNG");
        Ground[] groundArray = null;
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
                Texture ground = grounds[row];
                gm.drawTexture(ground, j * GROUND_SIZE, i * GROUND_SIZE, GROUND_SIZE, GROUND_SIZE, levelTexture);
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
        x++;
    }

    @Override
    public void render() {
        gm.drawTexture(levelTexture, x, 0);
    }
}
