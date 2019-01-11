package com.example.sss.goodlife.Api;

import com.example.sss.goodlife.Models.BankAccountIdStatus;
import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.ParticipantsList;
import com.example.sss.goodlife.Models.ParticipantsTypeStatus;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
import com.example.sss.goodlife.Models.Status;
import com.example.sss.goodlife.Models.VendorDataFromDb;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

        //Api for getting locations
        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @GET("program_list/program_application/location")
        Call<Status> getLocations();

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/program")
        Call<FormStatus> FormSubmission(
                @Field("program_aim") String program_aim ,
                @Field("location")String location,
                @Field("program_times")String date,
                @Field("partcipants")String from_time);


        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @GET("program_list/program_application/programs")
        Call<ProgramIdsStatus> getProgramids();

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @GET("program_list/program_application/participants")
        Call<ParticipantsTypeStatus> getParticipantsIds();

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @GET("program_list/program_application/accountname")
        Call<BankAccountIdStatus> getBankAccountNames();


        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/finance")
        Call<FormStatus> financeSubmission(
                @Field("program_id") String program_id,
                @Field("location_id") String location_id,
                @Field("finance_list")String financeList);

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/transport")
        Call<FormStatus> transportSubmission(
                @Field("program_id") String program_id,
                @Field("location_id") String location_id,
                @Field("transport_list")String financeList);



        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/daywise_report")
        Call<FormStatus> daywiseReportSubmission(
                @Field("program_id") String program_id,
                @Field("location_id") String location_id,
                @Field("date")String date,
                @Field("men")String men,
                @Field("women")String women,
                @Field("boys")String boys,
                @Field("girls")String girls,
                @Field("place_of_work")String place_of_work,
                @Field("work_information")String work_information,
                @Field("prayer_points")String prayer_points,
                @Field("achievements")String achievements,
                @Field("challenges")String challenges);

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @GET("program_list/program_application/finance_report")
        Call<VendorDataFromDb> vendorData(
                @Query("program_id") String program_id,
                @Query("location_id") String location_id);

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/login")
        Call<FormStatus> goodLifeLogin(
                @Field("email") String email,
                @Field("password")String password);


            @Headers("X-API-KEY:" + "Goodlife@&121%")
            @GET("program_list/program_application/logout")
            Call<FormStatus> goodLifeLogOut();

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/total_finance_report")
        Call<FormStatus> updateFinanceReport(
                @Field("program_id") String program_id,
                @Field("finance_id") String finance_id,
                @Field("vendor_id") String vendor_id,
                @Field("location_id") String location_id,
                @Field("total_amount")String total_amount,
                @Field("total_expenditure")String total_expenditure,
                @Field("balance_amount")String balance_amount,
                @Field("quation_image")String quation_image);






        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/program_review")
        Call<FormStatus> submitReview(
                @Field("program_id") String program_id,
                @Field("location_id") String location_id,
                @Field("email") String email,
                @Field("phone")String phone,
                @Field("review")String review,
                @Field("review_image")String image);

        @Headers("X-API-KEY:" + "Goodlife@&121%")
        @FormUrlEncoded
        @POST("program_list/program_application/program_images")
        Call<FormStatus> multipleImages(
                @Field("program_id") String program_id,
                @Field("images_list") String program_img,
                @Field("location_id") String location_id,
                @Field("date")String date,
                @Field("img_category")String img_category);




      //  http://localhost/goodlife/program_list/program_application/finance_report?program_id=14


//        http://www.yesinteriors.online/goodlf/program_list/program_application/login
//
//        email
//
//                password
//
//        harish@gmail.com
//
//123456


//       // http://www.yesinteriors.online/goodlf/
//        http://www.yesinteriors.online/goodlf/program_list/program_application/finance

}



