package com.example.smartcomplaint.models;

/**
* Class to handle the response
 *
 */
public class ImgUploadResponse {

    private String url;
    private String folder;
    private String errorMsg;

    public String getUrl() {
        return url;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
