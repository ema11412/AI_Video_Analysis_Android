package com.example.videomagnification.inter;

import com.example.videomagnification.model.Params;
import com.example.videomagnification.model.StatusImg;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ParameterApi {

    @GET("predict/parameter/")
    Call<Params> getParams(@Query("base64_image") String base64_image);

    @POST("predict/input")
    Call<StatusImg> upImage(@Body String base64_image);
}
