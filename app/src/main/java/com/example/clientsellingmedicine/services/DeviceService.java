package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.DTO.Device;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeviceService {
    @POST("/api/device")
    Call<Device> saveDevice(@Body Device device);
}
