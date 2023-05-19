package ru.forblitz.statistics.exception;

import ru.forblitz.statistics.dto.ErrorDTO;

public class ObjectException extends Exception {

    private final ErrorDTO error;

    public ObjectException(ErrorDTO error) {
        super(error.getMessage());
        this.error = error;
    }

    public ErrorDTO getError() {
        return error;
    }

}
