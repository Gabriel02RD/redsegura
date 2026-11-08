package co.edu.redsegura.exception;

/** Excepción base de la aplicación RedSegura. */
public class RedSeguraException extends RuntimeException {
    public RedSeguraException(String message) {
        super(message);
    }
}
