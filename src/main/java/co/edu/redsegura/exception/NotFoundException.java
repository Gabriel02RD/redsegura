package co.edu.redsegura.exception;

/** Se lanza cuando un vértice o una conexión no existe. */
public class NotFoundException extends RedSeguraException {
    public NotFoundException(String message) {
        super(message);
    }
}
