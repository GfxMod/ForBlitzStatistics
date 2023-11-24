package ru.forblitz.statistics.dto;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@Keep
public class APIResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("error")
    public ResponseError error;

    @SerializedName("meta")
    public AccountSearchResponse.Meta meta;

    public static class Meta {

        @SerializedName("count")
        public Integer count;

    }

    public static class ResponseError {

        @SerializedName("field")
        public String field;

        @SerializedName("message")
        public String message;

        @SerializedName("code")
        public Integer code;

        @SerializedName("value")
        public String value;

        @NonNull
        @Override
        public String toString() {
            return new GsonBuilder().setPrettyPrinting().create().toJson(this);
        }

    }

    @NonNull
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
