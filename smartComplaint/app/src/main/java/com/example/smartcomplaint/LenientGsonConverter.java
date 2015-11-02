package com.example.smartcomplaint;

import com.example.smartcomplaint.utility.smartcomplaintConstant;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;

import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;

/**
 * Created by Rakshith on 9/3/2015.
 */
public class LenientGsonConverter extends GsonConverter {
    private Gson mGson;

    public LenientGsonConverter(Gson gson) {
        super(gson);
        mGson = gson;
    }

    public LenientGsonConverter(Gson gson, String charset) {
        super(gson, charset);
        mGson = gson;
    }
    BufferedReader reader;
    String text;
    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
     /*   boolean willCloseStream = false; // try to close the stream, if there is no exception thrown using tolerant  JsonReader
        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(body.in()));
            jsonReader.setLenient(true);
            Object o = mGson.fromJson(jsonReader,type);
            willCloseStream = true;
            return o;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(willCloseStream) {
                closeStream(body);
            }
        }*/


        try
        {


            reader = new BufferedReader(new InputStreamReader(body.in()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }



            text = sb.toString();




         return text;
        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        return super.fromBody(body, type);
    }

    private void closeStream(TypedInput body){
        try {
            InputStream in = body.in();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}