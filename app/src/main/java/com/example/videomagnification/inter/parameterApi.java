package com.example.videomagnification.inter;

import com.example.videomagnification.model.Params;


import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface parameterApi {

    @GET("predict/parameter/{image}")
    Call<List<Params>> getParams(@Path("image") String image);
}
