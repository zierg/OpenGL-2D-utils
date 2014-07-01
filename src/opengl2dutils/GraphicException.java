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
public class GraphicException extends RuntimeException {
    /**
     * Creates a new instance of
     * <code>DaoException</code> without detail message.
     */
    public GraphicException() {
    }

    /**
     * Constructs an instance of
     * <code>DaoException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GraphicException(String msg) {
        super(msg);
    }
    
    public GraphicException(Throwable cause) {
        super(cause);
    }
    
    public GraphicException(String message, Throwable cause) {
        super(message, cause);
    }
}
