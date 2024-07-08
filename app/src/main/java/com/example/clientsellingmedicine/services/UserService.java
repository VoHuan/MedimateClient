package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.DTO.ResponseDto;
import com.example.clientsellingmedicine.DTO.UserDTO;
import com.example.clientsellingmedicine.DTO.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.POST;

public interface UserService {
    @GET("/api/user")
    Call<UserDTO> getUser();
    @PATCH("/api/user")
    Call<UserDTO> updateUser(@Body UserDTO user);
    @POST("/api/auth/register_with_otp")
    Call<ResponseDto> registerUser(@Body UserLogin userLogin);
}
