package com.example.videomagnification.inter;

import com.example.videomagnification.model.Params;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ParameterApi {

    @GET("predict/parameter/")
    Call<Params> getParams(@Query("base64_image") String base64_image);


}
