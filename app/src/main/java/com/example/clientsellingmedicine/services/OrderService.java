package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.DTO.MomoResponse;
import com.example.clientsellingmedicine.DTO.OrderDTO;
import com.example.clientsellingmedicine.DTO.OrderDetailDTO;
import com.example.clientsellingmedicine.DTO.OrderWithDetails;
import com.example.clientsellingmedicine.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {
    @GET("/api/order")
    Call<List<OrderDTO>> getOrders();

    @GET("/api/order/{code}")
    Call<OrderDTO>getOrderByCode(@Path("code")String code);

    @GET("/api/order-detail/{id}")
    Call<List<OrderDetailDTO>> getOrderItem(@Path("id")int id);

    @POST("/api/order/momo")
    Call<MomoResponse> newOrderWithMoMo(@Body OrderWithDetails order);

    @POST("/api/order/cod")
    Call<Order> newOrderWithCOD(@Body OrderWithDetails order);


}
