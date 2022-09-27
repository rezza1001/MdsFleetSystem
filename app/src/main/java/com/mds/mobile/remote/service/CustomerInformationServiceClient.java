package com.mds.mobile.remote.service;

import com.mds.mobile.remote.entity.contactus.request.GetContactUsRequestEntity;
import com.mds.mobile.remote.entity.contactus.response.GetContactUsResponseEntity;
import com.mds.mobile.remote.entity.customerinstallment.request.DetailRequestEntity;
import com.mds.mobile.remote.entity.customerinstallment.request.InstallmentRequestEntity;
import com.mds.mobile.remote.entity.customerinstallment.response.DetailResponseEntity;
import com.mds.mobile.remote.entity.customerinstallment.response.InstallmentResponseEntity;
import com.mds.mobile.remote.entity.faq.request.GetFaqRequestEntity;
import com.mds.mobile.remote.entity.faq.response.GetFaqResponseEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CustomerInformationServiceClient {

    @POST("Customer_Information/getInstallment")
    Call<InstallmentResponseEntity> installment(@Body InstallmentRequestEntity entity);

    @POST("Customer_Information/getInstallmentDetail")
    Call<DetailResponseEntity> installmentDetail(@Body DetailRequestEntity entity);

    @POST("Customer_Information/getContactus")
    Call<GetContactUsResponseEntity> contactUs(@Body GetContactUsRequestEntity entity);

    @POST("Faq/getFaq")
    Call<GetFaqResponseEntity> faq(@Body GetFaqRequestEntity entity);
}
