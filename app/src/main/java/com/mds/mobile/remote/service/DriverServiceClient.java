package com.mds.mobile.remote.service;

import com.mds.mobile.remote.entity.driverinsurance.request.AddFleetInsuranceClaimRequestEntity;
import com.mds.mobile.remote.entity.driverinsurance.response.AddFleetInsuranceClaimResponseEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.request.GetFleetRequestEntity;
import com.mds.mobile.remote.entity.drivervehiclelist.response.GetFleetResponseEntity;
import com.mds.mobile.remote.entity.fleetservice.request.AddFleetServiceRequestEntity;
import com.mds.mobile.remote.entity.fleetservice.response.AddFleetServiceResponseEntity;
import com.mds.mobile.remote.entity.history.request.GetFleetOdometerHistoryRequestEntity;
import com.mds.mobile.remote.entity.history.response.GetFleetOdometerHistoryResponseEntity;
import com.mds.mobile.remote.entity.odometer.request.AddFleetOdometerRequestEntity;
import com.mds.mobile.remote.entity.odometer.response.AddFleetOdometerResponseEntity;
import com.mds.mobile.remote.entity.stnk.request.AddFleetTaxRequestEntity;
import com.mds.mobile.remote.entity.stnk.response.AddFleetTaxResponseEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DriverServiceClient {

    @POST("Fleet/getFleet")
    Call<GetFleetResponseEntity> getFleet(@Body GetFleetRequestEntity entity);

    @POST("Fleet/addFleetOdometer")
    Call<AddFleetOdometerResponseEntity> addFleetOdometer(@Body AddFleetOdometerRequestEntity entity);

    @POST("Fleet/getFleetOdometerHistory")
    Call<GetFleetOdometerHistoryResponseEntity> getFleetOdometerHistory(@Body GetFleetOdometerHistoryRequestEntity entity);

    @POST("Fleet/addFleetServiceRequest")
    Call<AddFleetServiceResponseEntity> addFleetServiceRequest(@Body AddFleetServiceRequestEntity entity);

    @POST("Fleet/addFleetInsuranceClaim")
    Call<AddFleetInsuranceClaimResponseEntity> addFleetInsuranceClaim(@Body AddFleetInsuranceClaimRequestEntity entity);

    @POST("Fleet/addFleetTax")
    Call<AddFleetTaxResponseEntity> addFleetTax(@Body AddFleetTaxRequestEntity entity);

}
