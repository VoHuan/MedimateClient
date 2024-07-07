package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.DTO.ResponseDto;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LogoutService {
    @GET("/api/auth/logout")
    Call<ResponseDto> logout();
}
