package com.example.project.APIConnect;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @POST("/user/login")
    @FormUrlEncoded
    Call<LoginRequest> login( @Field("emailPhone") String emailPhone, @Field("password") String password);

    @POST("/user/register")
    @FormUrlEncoded
    Call<RegisterRequest> signup(@Field("fullName") String fullName, @Field("email") String email,
                                 @Field("phone") String phone, @Field("password") String password);

    @POST("/user/login/by-facebook")
    @FormUrlEncoded
    Call<LoginRequest> loginFacebook(@Field("accessToken") String accessToken);

    @GET("/tour/list")
    Call<ListToursResponse> getResponseListTour(@Header("Authorization") String token,@Query("rowPerPage") int n,@Query("pageNum" )int m
                                                );
    @POST("/tour/create")
    @FormUrlEncoded
    Call<CreateTourRequest> createTour(@Header("Authorization") String token,@Field("name") String name, @Field("startDate") long startDate,
                                       @Field("endDate") long endDate, @Field("adults") int adults,
                                       @Field("childs") int childs, @Field("minCost") float minCost,
                                       @Field("maxCost") float maxCost, @Field("isPrivate") boolean isPrivate,@Field("sourceLat") float sourceLat,
                                       @Field("sourceLong") float sourceLong,@Field("desLat") float desLat,@Field("desLong") float desLong);
}
