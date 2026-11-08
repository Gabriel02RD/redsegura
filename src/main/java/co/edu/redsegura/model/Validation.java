package co.edu.redsegura.model;

import co.edu.redsegura.exception.InvalidDataException;

/** Utilidad interna de validación para el modelo. */
final class Validation {

    private Validation() {
    }

    static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName + " no puede ser nulo ni vacío.");
        }
        return value.trim();
    }
}
