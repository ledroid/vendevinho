package zap.com.example.app.appzap.controller;

import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by leandro on 16/05/17.
 */
public class ServiceRest {

    private static final String TAG = ServiceRest.class.getSimpleName();

    public ServiceRest(){}


    public String serviceCall(String urlRequest) {
        String response = null;
        try {
                URL url = new URL(urlRequest);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                //
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                response = convertForString(inputStream);
            } catch (MalformedURLException me) {
                Log.e(TAG, "MalformedURLException: " + me.getMessage());

            } catch (ProtocolException pe){
                Log.e(TAG, "ProtocolException: " + pe.getMessage());
            } catch (IOException ie){
                Log.e(TAG, "IOException: " + ie.getMessage());
            } catch (Exception e){
                Log.e(TAG,"Exception"+ e.getMessage());
            }
           return response;
    }

    private String convertForString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null){
                builder.append(line).append('\n');
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
