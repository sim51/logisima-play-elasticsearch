package play.modules.elasticsearch.exception;

public class ESearchException extends Exception {

    /**
     * serialUID
     */
    private static final long serialVersionUID = 8900000567753471797L;

    /**
     * Construct a <code>ESearchException</code> with the specified detail message.
     * 
     * @param msg the detail message
     */
    public ESearchException(String message) {
        super(message);
    }

    /**
     * Construct a <code>ESearchException</code> with the specified detail message and nested exception.
     * 
     * @param msg the detail message
     * @param cause the nested exception
     */
    public ESearchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct a <code>ESearchException</code> with the snested exception.
     * 
     * @param cause the nested exception
     */
    public ESearchException(Throwable cause) {
        super(cause);
    }

}
