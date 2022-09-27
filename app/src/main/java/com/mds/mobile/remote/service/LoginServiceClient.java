package com.mds.mobile.remote.service;

import com.mds.mobile.remote.entity.login.request.LoginRequestEntity;
import com.mds.mobile.remote.entity.login.response.LoginResponseEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginServiceClient {

    @POST("Authenticate/login")
    Call<LoginResponseEntity> login(@Body LoginRequestEntity loginRequestEntity);
}
