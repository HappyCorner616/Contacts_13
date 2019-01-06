package com.example.archer.contacts_13.provider;

import com.example.archer.contacts_13.dto.AuthDto;
import com.example.archer.contacts_13.dto.AuthResponseDto;
import com.example.archer.contacts_13.dto.Contact;
import com.example.archer.contacts_13.dto.ContactsDto;
import com.example.archer.contacts_13.dto.DeleteResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    @POST("api/registration")
    Call<AuthResponseDto> registration(@Body AuthDto authDto);

    @POST("api/login")
    Call<AuthResponseDto> login(@Body AuthDto authDto);

    @DELETE("api/clear")
    Call<DeleteResponseDto> clearAll(@Header("Authorization") String authorization);

    @GET("api/contact")
    Call<ContactsDto> getAll(@Header("Authorization") String authorization);

    @POST("api/contact")
    Call<Contact> add(@Body Contact contact, @Header("Authorization") String authorization);

    @PUT("api/contact")
    Call<Contact> update(@Body Contact contact, @Header("Authorization") String authorization);

    @DELETE("api/contact/{id}")
    Call<DeleteResponseDto> delete(@Path("id") long id, @Header("Authorization") String authorization);
}
