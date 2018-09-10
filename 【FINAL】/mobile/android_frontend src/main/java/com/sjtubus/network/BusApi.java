package com.sjtubus.network;

import com.sjtubus.model.response.AppointResponse;
import com.sjtubus.model.response.CollectionResponse;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.LineInfoResponse;
import com.sjtubus.model.response.LocationResponse;
import com.sjtubus.model.response.LoginResponse;
import com.sjtubus.model.response.MessageResponse;
import com.sjtubus.model.response.ProfileResponse;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.model.response.ScheduleResponse;
import com.sjtubus.model.response.ShiftInfoResponse;
import com.sjtubus.model.response.StationResponse;
import com.sjtubus.model.response.StationSingleResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Allen on 2018/7/4.
 */

public interface BusApi {
    @GET("bus/buses")
    Observable<List<Object>> getBuses();

    @POST("account/login")
    Observable<LoginResponse> login(@Body RequestBody info);

    @POST("driver/login")
    Observable<LoginResponse> driverlogin(@Body RequestBody info);

    @POST("account/admin")
    Observable<LoginResponse> adminlogin(@Body RequestBody info);

    @POST("account/logout")
    Observable<HttpResponse> logout();

    @POST("account/register")
    Observable<HttpResponse> register(@Body RequestBody info);

    @POST("account/update_infos")
    Observable<HttpResponse> updatePersonInfos(@Body RequestBody info);

    @GET("account/profile")
    Observable<ProfileResponse> getProfile();

    @GET("message/all")
    Observable<MessageResponse> getMessages();

    @GET("shift/search_schedule")
    Observable<ScheduleResponse> getSchedule(@Query("type") String type, @Query("line_name") String line_name);

    @GET("shift/search_schedule_loopline")
    Observable<StationSingleResponse> getScheduleOfLoopLine(@Query("station") String station);

    @GET("shift/infos")
    Observable<ShiftInfoResponse> getShiftInfos(@Query("shiftid") String shiftid);

    @GET("line/stations")
    Observable<StationResponse> getLineStation(@Query("line_name") String line_name);

    @GET("line/infos")
    Observable<LineInfoResponse> getLineInfos(@Query("type") String type);

    @GET("appointment/infos")
    Observable<AppointResponse> getAppointment(@Query("line_name") String line_name, @Query("type") String type, @Query("appoint_date") String appoint_date);

    @GET("appointment/record")
    Observable<RecordInfoResponse> getRecordInfos(@Query("username")String username,@Query("user_role")String user_role);

    @POST("appointment/appoint")
    Observable<HttpResponse> appoint(@Body RequestBody appointInfo);

    @POST("appointment/cancel")
    Observable<HttpResponse> deleteAppoint(@Body RequestBody info);

    @POST("appointment/verify")
    Observable<HttpResponse> verifyUser(@Body RequestBody info);

    @POST("locate")
    Observable<HttpResponse> postLocation(@Body RequestBody location);

    @GET("location")
    Observable<LocationResponse> getLocation();

    @POST("collection/addCollect")
    Observable<HttpResponse> addCollection(@Body RequestBody collectInfo);

    @GET("collection/infos")
    Observable<CollectionResponse> getCollection(@Query("username") String username);

    @POST("collection/deleteCollect")
    Observable<HttpResponse> deleteCollection(@Body RequestBody collectInfo);

    @POST("ridebusinfo/import")
    Observable<HttpResponse> importRideInfo(@Body RequestBody rideInfo);
}
