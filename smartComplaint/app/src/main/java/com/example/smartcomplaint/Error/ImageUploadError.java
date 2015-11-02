package com.example.smartcomplaint.Error;

/**
 * Created by Nikita on 25/08/15.
 */
public class ImageUploadError extends RuntimeException {


    private String err;
    private String message;

    public ImageUploadError() {
        super();

    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
