package ru.forblitz.statistics.jsonobjects;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private int code;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
