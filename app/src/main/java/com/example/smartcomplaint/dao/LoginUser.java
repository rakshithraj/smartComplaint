package com.example.smartcomplaint.dao;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Rakshith on 1/12/2016.
 */
public class LoginUser  implements Serializable{

    String Email;
    String Password;
    String Name;
    String profile_pic;
    String Age;
    String Contact;
    String City;
    String State;
    String sex;
    String GcmId;
    String Type;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGcmId() {
        return GcmId;
    }

    public void setGcmId(String gcmId) {
        GcmId = gcmId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void Serialize(Context context) {
        try {
            ContextWrapper c = new ContextWrapper(context);
            String Path;

            File file=c.getFilesDir();

            if(file==null)
                file=  context.getExternalCacheDir().getAbsoluteFile();
;


            Path =file.getAbsolutePath() + "/LoginUser.bin";
            context.getExternalCacheDir().getAbsoluteFile();


            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File(Path)));

            oos.writeObject(this);
            oos.flush();
            oos.close();


        } catch (Exception ex) {

            Log.d("tag", "error=" + ex.toString());
        }
    }

    public LoginUser DeSerialize(Context context) {
        ContextWrapper c = new ContextWrapper(context);
        String Path ;

        File file=c.getFilesDir();

        if(file==null)
            file=  context.getExternalCacheDir().getAbsoluteFile();



        Path =file.getAbsolutePath() + "/LoginUser.bin";





         file = new File(Path);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    file));
            Object obj = ois.readObject();
            ois.close();

            return (LoginUser) obj;
        } catch (Exception ex) {

        }
        return null;
    }
}
