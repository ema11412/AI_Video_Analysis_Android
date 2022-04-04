package com.example.videomagnification.processing.magnificators;

import android.app.Activity;

import com.example.videomagnification.R;
import com.example.videomagnification.application.App;
import com.example.videomagnification.inter.parameterApi;
import com.example.videomagnification.model.Params;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MagnificatorFactory {

    private Activity context;

    public MagnificatorFactory(Activity context) {
        this.context = context;
    }

    public Magnificator createMagnificator() {
        int algorithmId = ((App) context.getApplication()).getAppData().getSelectedAlgorithmOption();
        if (algorithmId == R.id.radio_gaussian_ideal) {
            // 150, 6, 136.8, 163.8, 25.22, 1, 292, 139, heart rate
            // Baby 2: 150, 6, 2.33, 2.66, 30, 1, 294, 170
            // Face 2: 150, 6, 1, 1.66, 30, 1, 294, 170
            // Baby: 30, 16, 0.4, 3, 30, 0.1 -----> 24 bpm to 180 bpm
//            return new MagnificatorGdownIdeal(
//                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
//                    ((App) context.getApplication()).getAppData().getVideoDir(),
//                    ((App) context.getApplication()).getAppData().getAlpha(),
//                    ((App) context.getApplication()).getAppData().getLevel(),
//                    ((App) context.getApplication()).getAppData().getFl(),
//                    ((App) context.getApplication()).getAppData().getFh(),
//                    ((App) context.getApplication()).getAppData().getSampling(),
//                    ((App) context.getApplication()).getAppData().getChromAtt(),
//                    ((App) context.getApplication()).getAppData().getRoiX(),
//                    ((App) context.getApplication()).getAppData().getRoiY());

            return new MagnificatorGdownIdeal(
                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
                    ((App) context.getApplication()).getAppData().getVideoDir(),
                    ((App) context.getApplication()).getAppData().getAlpha(),
                    ((App) context.getApplication()).getAppData().getLevel(),
                    ((App) context.getApplication()).getAppData().getFl(),
                    ((App) context.getApplication()).getAppData().getFh(),
                    ((App) context.getApplication()).getAppData().getSampling(),
                    ((App) context.getApplication()).getAppData().getChromAtt(),
                    ((App) context.getApplication()).getAppData().getRoiX(),
                    ((App) context.getApplication()).getAppData().getRoiY());
//            // TEST 1
//            return new MagnificatorGdownIdeal(
//                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
//                    ((App) context.getApplication()).getAppData().getVideoDir(),
//                    50,
//                    6,
//                    1,
//                    10.0/6.0,
//                    30,
//                    1,
//                    128,
//                    15);
//             //TEST 2
//            return new MagnificatorGdownIdeal(
//                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
//                    ((App) context.getApplication()).getAppData().getVideoDir(),
//                    150,
//                    6,
//                    136.8/60,
//                    163.8/60,
//                    25.22,
//                    1,
//                    292,
//                    139);
        } else if (algorithmId == R.id.radio_laplacian_butterworth) {
            return new MagnificatorLpyrButter(
                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
                    ((App) context.getApplication()).getAppData().getVideoDir(),
                    ((App) context.getApplication()).getAppData().getAlpha(),
                    ((App) context.getApplication()).getAppData().getLambda(),
                    ((App) context.getApplication()).getAppData().getFl(),
                    ((App) context.getApplication()).getAppData().getFh(),
                    ((App) context.getApplication()).getAppData().getSampling(),
                    ((App) context.getApplication()).getAppData().getChromAtt(),
                    ((App) context.getApplication()).getAppData().getRoiX(),
                    ((App) context.getApplication()).getAppData().getRoiY()
            );
//            return new MagnificatorLpyrButter(
//                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
//                    ((App) context.getApplication()).getAppData().getVideoDir(),
//                    30,
//                    16,
//                    1.0 / 3.0,
//                    8.0 / 6.0,
//                    30,
//                    0.1,
//                    325,
//                    140
//            );
        }
        // TODO: Unknown algorithm

        return null;
    }
    private void getParams(){

        int alp0;
        float lamb0;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        parameterApi parameterapi = retrofit.create(parameterApi.class);

        Call<List<Params>> call = parameterapi.getParams("3");

        call.enqueue(new Callback<List<Params>>() {
            @Override
            public void onResponse(Call<List<Params>> call, Response<List<Params>> response) {

                List<Params> paramsList = response.body();
                for (Params param: paramsList){
                    int alp0 = param.getAlp0();
                    float lamb0 = param.getLamb0();
                }
            }

            @Override
            public void onFailure(Call<List<Params>> call, Throwable t) {

            }

        });

    }

}
