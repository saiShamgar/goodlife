package com.example.sss.goodlife.Api;

import com.example.sss.goodlife.Models.FormStatus;
import com.example.sss.goodlife.Models.ParticipantsList;
import com.example.sss.goodlife.Models.ParticipantsTypeStatus;
import com.example.sss.goodlife.Models.ProgramIdsStatus;
import com.example.sss.goodlife.Models.Status;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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




}



