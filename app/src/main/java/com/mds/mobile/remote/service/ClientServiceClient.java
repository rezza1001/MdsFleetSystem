package com.mds.mobile.remote.service;

import com.mds.mobile.remote.entity.changepassword.request.ChangePasswordRequestEntity;
import com.mds.mobile.remote.entity.changepassword.response.ChangePasswordResponseEntity;
import com.mds.mobile.remote.entity.notif.request.GetNotifRequestEntity;
import com.mds.mobile.remote.entity.notif.response.GetNotifResponseEntity;
import com.mds.mobile.remote.entity.password.request.GetForgotPasswordRequestEntity;
import com.mds.mobile.remote.entity.password.response.GetForgotPasswordResponseEntity;
import com.mds.mobile.remote.entity.profile.request.GetProfileRequestEntity;
import com.mds.mobile.remote.entity.profile.response.GetProfileResponseEntity;
import com.mds.mobile.remote.entity.promo.request.GetPromoRequestEntity;
import com.mds.mobile.remote.entity.promo.response.GetPromoResponseEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClientServiceClient {

    @POST("Promo/getPromo")
    Call<GetPromoResponseEntity> getPromo(@Body GetPromoRequestEntity entity);

    @POST("Notification/getNotif")
    Call<GetNotifResponseEntity> getNotification(@Body GetNotifRequestEntity entity);

    @POST("Authenticate/forgotPassword")
    Call<GetForgotPasswordResponseEntity> forgotPassword(@Body GetForgotPasswordRequestEntity entity);

    @POST("Authenticate/changePassword")
    Call<ChangePasswordResponseEntity> changePassword(@Body ChangePasswordRequestEntity entity);

    @POST("Profile/getProfile")
    Call<GetProfileResponseEntity> getProfile(@Body GetProfileRequestEntity entity);

}
