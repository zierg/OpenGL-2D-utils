/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl2dutils;

/**
 *
 * @author Ivan
 */
public class Mouse {
    
    private static OpenGLManager gm;
    private static int width;
    private static int height;

    private Mouse() {
    }
    
    static void init(OpenGLManager openGlManager) {
        gm = openGlManager;
        width = gm.getWidth();
        height = gm.getHeight();
    }

    public static boolean isClipMouseCoordinatesToWindow() {
        return org.lwjgl.input.Mouse.isClipMouseCoordinatesToWindow();
    }

    public static void setClipMouseCoordinatesToWindow(boolean clip) {
        org.lwjgl.input.Mouse.setClipMouseCoordinatesToWindow(clip);
    }

    public static void setCursorPosition(int new_x, int new_y) {
        org.lwjgl.input.Mouse.setCursorPosition(new_x, height - new_y);
    }

    public static void destroy() {
        org.lwjgl.input.Mouse.destroy();
    }

    public static void poll() {
        org.lwjgl.input.Mouse.poll();
    }

    public static boolean isButtonDown(int button) {
        return org.lwjgl.input.Mouse.isButtonDown(button);
    }

    public static String getButtonName(int button) {
        return org.lwjgl.input.Mouse.getButtonName(button);
    }

    public static int getButtonIndex(String buttonName) {
        return org.lwjgl.input.Mouse.getButtonIndex(buttonName);
    }

    public static boolean next() {
        return org.lwjgl.input.Mouse.next();
    }

    public static int getEventButton() {
        return org.lwjgl.input.Mouse.getEventButton();
    }

    public static boolean getEventButtonState() {
        return org.lwjgl.input.Mouse.getEventButtonState();
    }

    public static int getEventDX() {
        return org.lwjgl.input.Mouse.getEventDX();
    }

    public static int getEventDY() {
        return -org.lwjgl.input.Mouse.getEventDY();
    }

    public static int getEventX() {
        return org.lwjgl.input.Mouse.getEventX();
    }

    public static int getEventY() {
        return org.lwjgl.input.Mouse.getEventY();
    }

    public static int getEventDWheel() {
        return org.lwjgl.input.Mouse.getEventDWheel();
    }

    public static long getEventNanoseconds() {
        return org.lwjgl.input.Mouse.getEventNanoseconds();
    }

    public static int getX() {
        return org.lwjgl.input.Mouse.getX();
    }

    public static int getY() {
        System.out.println("h:" + height);
        return height - org.lwjgl.input.Mouse.getY() - 1;
    }

    public static int getDX() {
        return org.lwjgl.input.Mouse.getDX();
    }

    public static int getDY() {
        return -org.lwjgl.input.Mouse.getDY();
    }

    public static int getDWheel() {
        return org.lwjgl.input.Mouse.getDWheel();
    }

    public static int getButtonCount() {
        return org.lwjgl.input.Mouse.getButtonCount();
    }

    public static boolean hasWheel() {
        return org.lwjgl.input.Mouse.hasWheel();
    }

    static boolean isGrabbed() {
        return org.lwjgl.input.Mouse.isGrabbed();
    }

    static void setGrabbed(boolean grab) {
        org.lwjgl.input.Mouse.setGrabbed(grab);
    }

    public static void updateCursor() {
        org.lwjgl.input.Mouse.updateCursor();
    }

    public static boolean isInsideWindow() {
        return org.lwjgl.input.Mouse.isInsideWindow();
    }
}
