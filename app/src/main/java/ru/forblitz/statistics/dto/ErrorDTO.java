package ru.forblitz.statistics.dto;

import com.google.gson.annotations.SerializedName;

public class ErrorDTO {

    public ErrorDTO(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private String code;

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
