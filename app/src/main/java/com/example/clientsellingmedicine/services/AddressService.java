package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.DTO.AddressDto;
import com.example.clientsellingmedicine.DTO.District;
import com.example.clientsellingmedicine.DTO.Province;
import com.example.clientsellingmedicine.DTO.ResponseDto;
import com.example.clientsellingmedicine.DTO.Ward;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressService {
    @GET("/api/address")
    Call<List<AddressDto>> getAddress();

    @GET("/api/province")
    Call<List<Province>> getProvinces();

    @GET("/api/district/{id}")
    Call<List<District>> getDistricts(@Path("id") Integer provinceId);

    @GET("/api/ward/{id}")
    Call<List<Ward>> getWards(@Path("id") Integer districtId);

    @POST("/api/address")
    Call<ResponseDto> addAddress(@Body AddressDto addressDto);

    @PUT("/api/address")
    Call<ResponseDto> updateAddress(@Body AddressDto addressDto);

    @DELETE("/api/address/{id}")
    Call<ResponseDto> deleteAddress(@Path("id") Integer addressId);
}
