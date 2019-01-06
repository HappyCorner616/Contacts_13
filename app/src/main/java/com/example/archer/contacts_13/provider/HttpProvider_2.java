package com.example.archer.contacts_13.provider;

import android.util.Log;

import com.example.archer.contacts_13.dto.AuthDto;
import com.example.archer.contacts_13.dto.AuthResponseDto;
import com.example.archer.contacts_13.dto.Contact;
import com.example.archer.contacts_13.dto.ContactsDto;
import com.example.archer.contacts_13.dto.DeleteResponseDto;
import com.example.archer.contacts_13.dto.ErrorDto;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

import static com.example.archer.contacts_13.App.MY_TAG;

public class HttpProvider_2 {

    private Gson gson;
    private OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //private static final String BASE_URL = "https://contacts-telran.herokuapp.com";
    //private static final String BASE_URL = "http://192.168.43.155:8080/ForAndroid";
    private static final String BASE_URL = "http://192.168.1.11:8080/ForAndroid";
    private static final String AUTHORIZATION = "Authorization";

    private static final HttpProvider_2 ourInstance = new HttpProvider_2();

    public static HttpProvider_2 getInstance() {
        return ourInstance;
    }

    private HttpProvider_2() {
        gson = new Gson();
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public String registration(String email, String password) throws Exception {
        AuthDto authDto = new AuthDto(email, password);
        String json = gson.toJson(authDto);

        URL url = new URL(BASE_URL + "/api/registration");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(15000);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(json);
        bw.flush();
        bw.close();

        int code = connection.getResponseCode();
        String line = null;
        String res = "";

        if(code >= 200 && code < 300){
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            AuthResponseDto responseDto = gson.fromJson(sb.toString(), AuthResponseDto.class);
            return  responseDto.getToken();
        }else{
            InputStreamReader isr = new InputStreamReader(connection.getErrorStream());
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            if(code == 409){
                ErrorDto errorDto = gson.fromJson(sb.toString(), ErrorDto.class);
                throw new Exception(errorDto.getMessage());
            }else{
                Log.e(MY_TAG, "Registration error: " + sb.toString());
                throw new Exception("Connection error!");
            }

        }

    }

    public String login(String email, String password) throws Exception {
        AuthDto authDto = new AuthDto(email, password);
        String json = gson.toJson(authDto);

        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(BASE_URL + "/api/login")
                .post(requestBody)
                .addHeader("Authorized", "token")
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        if(response.isSuccessful()){
            AuthResponseDto responseDto = gson.fromJson(res, AuthResponseDto.class);
            return responseDto.getToken();
        }else{
            if(response.code() == 401){
                throw new Exception("Wrong email or password!");
            }else{
                Log.d(MY_TAG, "login error: " + res);
                throw new Exception("Server error!");
            }
        }

    }

    public String clearContacts(String token) throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "/api/clear")
                .addHeader(AUTHORIZATION, token)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        Log.d(MY_TAG, "clearContacts response:" + res);
        if(response.isSuccessful()) {
            DeleteResponseDto deleteResponse = gson.fromJson(res, DeleteResponseDto.class);
            return deleteResponse.getStatus();
        }else{
            ErrorDto error = gson.fromJson(res, ErrorDto.class);
            throwHttpError(response.code(), error.toString());
        }
        return "Done";
    }

    public ContactsDto getAllContacts(String token) throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "/api/contact")
                .addHeader(AUTHORIZATION ,token)
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        if(!response.isSuccessful()){
            throwHttpError(response.code(), null);
        }
        return gson.fromJson(res, ContactsDto.class);
    }

    public Contact addContact(String token, Contact contact) throws Exception {
        String json = gson.toJson(contact);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(BASE_URL + "/api/contact")
                .addHeader(AUTHORIZATION, token)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        if(response.isSuccessful()) {
            return gson.fromJson(res, Contact.class);
        }else{
            ErrorDto error = gson.fromJson(res, ErrorDto.class);
            throwHttpError(response.code(), error.toString());
            return  contact;
        }

    }

    public Contact updateContact(String token, Contact contact) throws Exception {
        RequestBody requestBody = RequestBody.create(JSON, gson.toJson(contact));
        Request request = new Request.Builder().url(BASE_URL + "/api/contact")
                .addHeader(AUTHORIZATION, token)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        if(response.isSuccessful()) {
            return gson.fromJson(res, Contact.class);
        }else{
            ErrorDto error = gson.fromJson(res, ErrorDto.class);
            throwHttpError(response.code(), error.toString());
            return  contact;
        }

    }

    public String deleteContact(String token, Contact contact) throws Exception {
        Request request = new Request.Builder().url(BASE_URL + "/api/contact/" + contact.getId())
                .addHeader(AUTHORIZATION, token)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        if(response.isSuccessful()) {
            DeleteResponseDto deleteResponse = gson.fromJson(res, DeleteResponseDto.class);
            return deleteResponse.getStatus();
        }else{
            ErrorDto error = gson.fromJson(res, ErrorDto.class);
            throwHttpError(response.code(), error.toString());
            return  error.getMessage();
        }
    }

    private void throwHttpError(int code, String details) throws Exception {
        details = details == null ? "" : " (" + details + ")";
        switch (code) {
            case 401:
                throw new Exception("Unauthorized" + details);
            case 403:
                throw new Exception("Forbidden" + details);
            case 404:
                throw new Exception("Not found" + details);
            default:
                throw new Exception("Something error" + details);
        }
    }


}
