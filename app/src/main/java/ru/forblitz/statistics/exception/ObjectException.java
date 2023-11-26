package ru.forblitz.statistics.exception;


public class ObjectException extends Exception {

    public String title;

    public String message;

    public ObjectException(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

}
