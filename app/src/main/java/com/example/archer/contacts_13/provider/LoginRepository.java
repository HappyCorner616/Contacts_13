package com.example.archer.contacts_13.provider;

import android.support.annotation.NonNull;

import com.example.archer.contacts_13.AuthRepsitoryCallback;
import com.example.archer.contacts_13.dto.AuthDto;
import com.example.archer.contacts_13.dto.AuthResponseDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository implements IAuthRepository {

    private Api api;
    private Store store;

    public LoginRepository(Api api, Store store) {
        this.api = api;
        this.store = store;
    }

    @Override
    public void registration(@NonNull String email, @NonNull String password, @NonNull final AuthRepsitoryCallback callback) {
        AuthDto body = new AuthDto(email, password);
        api.registration(body).enqueue(new Callback<AuthResponseDto>() {
            @Override
            public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response) {
                if(response.isSuccessful()){
                    store.saveToken(response.body().getToken());
                    callback.registrationSuccess();
                }else if(response.code() == 409){
                    callback.registrationFailure("User already exist");
                }else{
                    callback.registrationFailure("Server error!");
                }
            }

            @Override
            public void onFailure(Call<AuthResponseDto> call, Throwable t) {
                callback.registrationFailure("Connection error!");
            }
        });
    }
}
