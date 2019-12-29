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
                                 @Field("phone") String phone, @Field("password") String password,
                                 @Field("address") String address, @Field("gender") int gender, @Field("dob") String dob);

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

    @POST("/tour/add/feedback-service")
    @FormUrlEncoded
    Call<ResponseBody> sendFeedback(@Header("Authorization") String token, @Field("serviceId") int serviceId,
                                    @Field("feedback") String feedback, @Field("point") int point);

    @Headers("Content-Type: application/json")
    @GET("/tour/get/feedback-service")
    Call<ListCommentServiceResponse> getListCommentService(@Header("Authorization") String token,@Query("serviceId") int serviceId,
                                              @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @Headers("Content-Type: application/json")
    @GET("/tour/get/feedback-point-stats")
    Call<PointServiceResponse> getPointFeedbackService(@Header("Authorization") String token,@Query("serviceId") int serviceId);

    @POST("/tour/add/member")
    @FormUrlEncoded
    Call<ResponseBody> inviteMember(@Header("Authorization") String token, @Field("tourId") String tourId,
                                    @Field("invitedUserId") String invitedUserId, @Field("isInvited") boolean isInvited);

    @Headers("Content-Type: application/json")
    @GET("/user/search")
    Call<SearchUserResponse> getListUsersSearch(@Query("searchKey") String searchKey, @Query("pageIndex") int pageIndex,
                                                @Query("pageSize") int pageSize);

    @POST("/tour/comment")
    @FormUrlEncoded
    Call<String> sendComment( @Header("Authorization") String token,@Field("tourId") String tourId, @Field("userId") String userId,@Field("comment") String comment);
    @GET("/tour/info")

    Call<InfoTourResponse> getResponseInfoTour(@Header("Authorization") String token,@Query("tourId") int n);
    @GET("/tour/comment-list")

    Call<listCommentResponse> getComment(@Header("Authorization") String token,@Query("tourId") int n,@Query("pageIndex") int in,@Query("pageSize") int im);
    @GET("/tour/get/review-list")

    Call<reviewResponse> getReview(@Header("Authorization") String token,@Query("tourId") int n,@Query("pageIndex") int in,@Query("pageSize") int im);
    @GET("/tour/get/review-point-stats")

    Call<pointResponse> getPointReview(@Header("Authorization") String token,@Query("tourId") int n);

    @POST("/user/request-otp-recovery")
    @FormUrlEncoded
    Call<OTPResponse> requestOTP(@Field("type") String type, @Field("value") String value);

    @POST("/user/verify-otp-recovery")
    @FormUrlEncoded
    Call<ResponseBody> verifyOTP(@Field("userId") int userId, @Field("newPassword") String newPassword, @Field("verifyCode") String verifyCode);

    @GET("/user/info")
    Call<UserInfoResponse>  getUsetInfo(@Header("Authorization") String token);

    @POST("/user/edit-info")
    @FormUrlEncoded
    Call<ResponseBody> updateUserInfo(@Header("Authorization") String token,@Field("fullName") String fullName, @Field("gender") int gender, @Field("dob") String dob);

    @POST("/user/update-password")
    @FormUrlEncoded
    Call<ResponseBody> updatePassword(@Header("Authorization") String token,@Field("userId") int userId,@Field("currentPassword") String currentPassword,
                                      @Field("newPassword") String newPassword);

    @POST("/tour/current-users-coordinate")
    @FormUrlEncoded
    Call<ArrayList<PosMemberResponse>> getPosMember(@Header("Authorization") String token,@Field("userId") int userId,@Field("tourId") int tourId,
                                     @Field("lat") double lat, @Field("long") double longtitude);

    @POST("/tour/add/notification-on-road")
    @FormUrlEncoded
    Call<ResponseBody> sendSpeedNoti(@Header("Authorization") String token, @Field("lat") double lat, @Field("long") double longtitude
                                     ,@Field("tourId") int tourId,@Field("userId") int userId,@Field("notificationType") int notificationType,
                                                   @Field("speed") int speed, @Field("note") String note);

    @POST("/tour/notification")
    @FormUrlEncoded
    Call<ResponseBody> sendTextNoti(@Header("Authorization") String token,@Field("tourId") int tourId, @Field("userId") int userId,
                                                    @Field("noti") String noti);
}
