package co.edu.unicauca.distribuidos.core.excepciones;

public class EntidadNoExisteException extends RuntimeException {
    public EntidadNoExisteException(String message) {
        super(message);
    }
}
