package co.edu.redsegura.exception;

/** Se lanza cuando se intenta registrar un ID que ya existe. */
public class DuplicateException extends RedSeguraException {
    public DuplicateException(String message) {
        super(message);
    }
}
