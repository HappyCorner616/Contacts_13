package com.example.archer.contacts_13.dagpack.modules;

import com.example.archer.contacts_13.dagpack.interfaces.IAuthRepository;
import com.example.archer.contacts_13.dagpack.interfaces.ILoginRepositoryCallback;
import com.example.archer.contacts_13.dagpack.interfaces.IRegistrationRepositoryCallback;
import com.example.archer.contacts_13.dagpack.interfaces.IStore;
import com.example.archer.contacts_13.dto.AuthDto;
import com.example.archer.contacts_13.dto.AuthResponseDto;
import com.example.archer.contacts_13.dto.ErrorDto;
import com.example.archer.contacts_13.provider.Api;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class AuthRepository implements IAuthRepository {

    private Api api;
    private IStore store;
    private Gson gson;

    public AuthRepository(Api api, IStore store) {
        this.api = api;
        this.store = store;
        gson = new Gson();
    }

    @Override
    public void registration(String email, String password, IRegistrationRepositoryCallback callback) {
        AuthDto authDto = new AuthDto(email, password);
        Call<AuthResponseDto> call = api.registration(authDto);
        try {
            Response<AuthResponseDto> response = call.execute();
            if(response.isSuccessful()){
                callback.successful();
            }else{
                ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
                callback.failure(errorDto.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.failure(e.getMessage());
        }
    }

    @Override
    public void login(String email, String password, ILoginRepositoryCallback callback) {
        AuthDto authDto = new AuthDto(email, password);
        Call<AuthResponseDto> call = api.login(authDto);
        try {
            Response<AuthResponseDto> response = call.execute();
            if(response.isSuccessful()){
                store.saveToket(response.body().getToken());
                callback.successful();
            }else{
                ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
                callback.failure(errorDto.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.failure(e.getMessage());
        }
    }
}
