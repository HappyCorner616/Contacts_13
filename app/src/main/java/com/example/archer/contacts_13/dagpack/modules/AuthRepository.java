package com.example.archer.contacts_13.dagpack.modules;

import com.example.archer.contacts_13.dagpack.interfaces.IAuthRepository;
import com.example.archer.contacts_13.dagpack.interfaces.IStore;
import com.example.archer.contacts_13.dto.AuthDto;
import com.example.archer.contacts_13.dto.AuthResponseDto;
import com.example.archer.contacts_13.dto.ErrorDto;
import com.example.archer.contacts_13.provider.Api;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
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
    public Completable registration(String email, String password) {
        AuthDto authDto = new AuthDto(email, password);
        return Completable.fromSingle(api.registration())
    }

    private String registrationResponse(Response<AuthResponseDto> response) throws Exception {
        if(response.isSuccessful()){
            return "Done";
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throw new Exception(errorDto.toString());
        }
    }

    @Override
    public void login(String email, String password) {
        AuthDto authDto = new AuthDto(email, password);
        return Completable.fromSingle(api.login(authDto)
                .doOnSuccess(this::registrationResponse));
    }

    private void loginResponse(Response<AuthResponseDto> response) throws Exception {
        if(response.isSuccessful()){
            store.saveToket(response.body().getToken());
        }else{
            ErrorDto errorDto = gson.fromJson(response.errorBody().string(), ErrorDto.class);
            throw new Exception(errorDto.toString());
        }
    }
}
