package com.example.clientsellingmedicine.services;

import com.example.clientsellingmedicine.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    @GET("/api/product")
    Call<List<Product>> getProducts();


    @GET("/api/product/filter")
    Call<List<Product>> getProductsFilter(
            @Query("keySearch") String keySearch,
            @Query("categoryId") int categoryId,
            @Query("minPrice") int minPrice,
            @Query("maxPrice") int maxPrice
    );

    @GET("/api/product/newest")
    Call<List<Product>> getNewProducts();

    @GET("/api/product/best_sellers")
    Call<List<Product>> getBestSellerProducts();


    @GET("/api/product/top-discounted")
    Call<List<Product>> getBestPromotionProducts();

    @GET("/api/product/discounted")
    Call<List<Product>> getHavePromotionProducts();
}
