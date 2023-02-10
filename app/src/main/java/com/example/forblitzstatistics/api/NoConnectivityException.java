package com.example.forblitzstatistics.api;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    /**
     * @return Returns the detail message string of this throwable.
     */
    @Override
    public String getMessage() {
        return "No Internet Connection";
    }

}