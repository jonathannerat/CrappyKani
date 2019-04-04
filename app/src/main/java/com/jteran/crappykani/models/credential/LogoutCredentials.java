package com.jteran.crappykani.models.credential;

public class LogoutCredentials {
    private String _method = "delete";

    private String authenticity_token;

    public LogoutCredentials(String authenticity_token) {
        this.authenticity_token = authenticity_token;
    }
}
