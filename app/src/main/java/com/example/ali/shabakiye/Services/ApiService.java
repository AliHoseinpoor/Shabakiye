package com.example.ali.shabakiye.Services;

import com.example.ali.shabakiye.Holders.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ali on 7/14/18.
 */

public interface ApiService {

    @GET("customer_info")
    Call<UserInfo> getUser(@Query("username") String username);

}
