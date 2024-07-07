package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.DTO.GoogleToken;
import com.example.clientsellingmedicine.DTO.User;
import com.example.clientsellingmedicine.DTO.UserLogin;
import com.example.clientsellingmedicine.DTO.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/api/auth/login")
    Call<Token> login(@Body UserLogin userLogin);

    @GET("/api/user")
    Call<User> getUserLogin();

    @POST("/api/token/check")
    Call<Boolean> checkToken(@Body Token token);

    @POST("/api/auth/refresh")
    Call<Token> refreshToken(@Body Token token);

    @POST("/api/auth/login_with_google")
    Call<Token> loginWithGoogle(@Body GoogleToken token);


}
