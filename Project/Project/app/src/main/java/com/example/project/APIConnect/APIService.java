package com.example.project.APIConnect;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<ListToursResponse> getResponseListTour(@Header("Authorization") String token,@Query("rowPerPage") int n,@Query("pageNum" )int m);

    @GET("/tour/history-user")
    Call<ListToursHistoryResponse> getResponseListTourHistory(@Header("Authorization") String token,@Query("pageIndex") int index,@Query("pageSize" )int size);

    @POST("/tour/update-tour")
    @FormUrlEncoded
    Call<UpdateTourResponse> updateTour(@Header("Authorization") String token,@Field("id") int id,@Field("name") String name,
                                        @Field("startDate") long startDate, @Field("endDate") long endDate,
                                        @Field("adults") int adults, @Field("childs") int childs,
                                        @Field("minCost") int minCost, @Field("maxCost") int maxCost,@Field("status") int status);

    @POST("/tour/create")
    @FormUrlEncoded
    Call<CreateTourRequest> createTour(@Header("Authorization") String token,@Field("name") String name, @Field("startDate") long startDate,
                                       @Field("endDate") long endDate,@Field("sourceLat") float sourceLat,
                                       @Field("sourceLong") float sourceLong,@Field("desLat") float desLat,@Field("desLong") float desLong,
                                       @Field("isPrivate") boolean isPrivate ,@Field("adults") int adults,
                                       @Field("childs") int childs, @Field("minCost") int minCost,
                                       @Field("maxCost") int maxCost,@Field("avatar") String avatar
    );

    @GET("/tour/history-user-by-status")
    Call<StatusResponse> getStatusTotal(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("/tour/set-stop-points")
    Call<ResponseBody> addStopPoint(@Header("Authorization") String token
                    ,@Body  AddStopPointRequest body);

    @Headers("Content-Type: application/json")
    @POST("/tour/suggested-destination-list")
    Call<SuggestDescResponse> getSuggestDesc(@Header("Authorization") String token, @Body  SuggestDescRequest  coordList);

    @GET("/tour/info")
    Call<InfoTourResponse> getInfoTour(@Header("Authorization") String token,@Query("tourId") int tourId);

    @GET("/tour/remove-stop-point")
    Call<ResponseBody> removeStopPoint(@Header("Authorization") String token,@Query("stopPointId") String stopPointId);

    @GET("/tour/get/service-detail")
    Call<DetailServiceResponse> detailService(@Header("Authorization") String token,@Query("serviceId") int serviceId);

}
