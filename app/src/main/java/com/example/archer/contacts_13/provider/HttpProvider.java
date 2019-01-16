package com.example.archer.contacts_13.provider;

import android.util.Log;

import com.example.archer.contacts_13.dto.AuthDto;
import com.example.archer.contacts_13.dto.AuthResponseDto;
import com.example.archer.contacts_13.dto.Contact;
import com.example.archer.contacts_13.dto.ContactsDto;
import com.example.archer.contacts_13.dto.DeleteResponseDto;
import com.example.archer.contacts_13.dto.ErrorDto;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.archer.contacts_13.App.MY_TAG;

public class HttpProvider {

    private static final String BASE_URL_GRISHA = "http://contacts-telran.herokuapp.com/";
    private static final String BASE_URL_ME = "http://192.168.1.11:8080/ForAndroid/";

    private Api api;
    private Gson gson;

    private static final HttpProvider ourInstance = new HttpProvider();

    public static HttpProvider getInstance() {
        return ourInstance;
    }

    private HttpProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_ME)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);
        gson = new Gson();
    }

    public String registration(String email, String password) throws Exception {
        AuthDto authDto = new AuthDto(email, password);
        Call<AuthResponseDto> call = api.registration(authDto);
        //Log.d(MY_TAG, "before call.execute(): ");
        Response<AuthResponseDto> response = call.execute();
        //Log.d(MY_TAG, "after call.execute(): ");
        if(response.isSuccessful()){
            return response.body().getToken();
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throwHttpError(response.code(), errorDto.toString());
            return errorDto.getMessage();
        }
    }

    public String login(String email, String password) throws Exception {
        AuthDto authDto = new AuthDto(email, password);
        Call<AuthResponseDto> call = api.login(authDto);
        Response<AuthResponseDto> response = call.execute();
        if(response.isSuccessful()){
            return response.body().getToken();
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throwHttpError(response.code(), errorDto.toString());
            return errorDto.getMessage();
        }
    }

    public String clearContacts(String token) throws Exception {
        Call<DeleteResponseDto> call = api.clearAll(token);
        Response<DeleteResponseDto> response = call.execute();
        if(response.isSuccessful()){
            return  response.body().getStatus();
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throwHttpError(response.code(), errorDto.toString());
            return errorDto.getMessage();
        }
    }

    public ContactsDto getContacts(String token) throws Exception {
        Call<ContactsDto> call = api.getAll(token);
        Response<ContactsDto> response = call.execute();
        if(response.isSuccessful()){
            return response.body();
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throwHttpError(response.code(), errorDto.toString());
            return null;
        }
    }

    public Contact addContact(Contact contact, String token) throws Exception {
        Call<Contact> call = api.add(contact, token);
        Response<Contact> response = call.execute();
        if(response.isSuccessful()){
            return response.body();
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throwHttpError(response.code(), errorDto.toString());
            return null;
        }
    }

    public Contact updateContact(Contact contact, String token) throws Exception {
        Call<Contact> call = api.update(contact, token);
        Response<Contact> response = call.execute();
        if(response.isSuccessful()){
            return response.body();
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throwHttpError(response.code(), errorDto.getMessage());
            return null;
        }
    }

    public String deleteContact(Contact contact, String token) throws Exception {
        Call<DeleteResponseDto> call = api.delete(contact.getId(), token);
        Log.d(MY_TAG, "deleteContact url: " + call.request().url().toString());
        Response<DeleteResponseDto> response = call.execute();
        if(response.isSuccessful()){
            return response.body().getStatus();
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throwHttpError(response.code(), errorDto.getMessage());
            return null;
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
