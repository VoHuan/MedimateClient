package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.DTO.ResponseDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LogoutService {
    @POST("/api/auth/logout")
    Call<ResponseDto> logout();
}
