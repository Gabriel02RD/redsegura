package co.edu.redsegura.exception;

/** Se lanza cuando un dato es nulo, vacío o inválido (por ejemplo, un peso menor o igual a 0). */
public class InvalidDataException extends RedSeguraException {
    public InvalidDataException(String message) {
        super(message);
    }
}
