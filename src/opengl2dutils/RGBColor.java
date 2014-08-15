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
public class RGBColor {
    private int red;
    private int green;
    private int blue;
    
    public RGBColor() {}
    
    public RGBColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * @return the red
     */
    public int getRed() {
        return red;
    }

    /**
     * @param red the red to set
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * @return the green
     */
    public int getGreen() {
        return green;
    }

    /**
     * @param green the green to set
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * @return the blue
     */
    public int getBlue() {
        return blue;
    }

    /**
     * @param blue the blue to set
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }
}
