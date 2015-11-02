package com.example.smartcomplaint.models;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita on 31/08/15.
 */
public class LoginDetails {




    @SerializedName("Email")
    String Email;

    public String getLoginPassword() {
        return Password;
    }

    public void setLoginPassword(String Password) {
        this.Password = Password;
    }

    @SerializedName("Password")
    String Password;



    public String getLoginEmail() {
        return Email;
    }

    public void setLoginEmail(String Email) {
        this.Email = Email;
    }


}
