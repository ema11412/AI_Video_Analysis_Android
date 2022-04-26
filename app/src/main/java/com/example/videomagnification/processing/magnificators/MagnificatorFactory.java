package com.example.videomagnification.processing.magnificators;

import android.app.Activity;

import com.example.videomagnification.R;
import com.example.videomagnification.application.App;
import com.example.videomagnification.inter.ParameterApi;
import com.example.videomagnification.model.Params;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MagnificatorFactory {

    private Activity context;

    String[] testing = new String[7];

    String test = "https://i.imgur.com/d3gn4Xm.jpeg";

    String d0   = "https://i.imgur.com/7Sayp4U.jpg";
    String d1   = "https://i.imgur.com/ESI5x5V.jpg";
    String d2   = "https://i.imgur.com/AADnZjf.jpg";
    String d3   = "https://i.imgur.com/eLbrsGU.jpg";
    String d4   = "https://i.imgur.com/w4FZb2P.jpg";
    String d5   = "https://i.imgur.com/JQpxuEQ.jpg";
    String d6   = "https://i.imgur.com/p3Bfmxk.jpg";
    String d7   = "https://i.imgur.com/Iw5lNBE.jpg";
    String d8   = "https://i.imgur.com/qPvyFZK.jpg";


    public MagnificatorFactory(Activity context) {
        this.context = context;
    }




    int[] executeSend(String image){

        final int[] para = new int[2];


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://192.168.0.114:5000/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        ParameterApi parameterApi = retrofit.create(ParameterApi.class);

        Call<Params> call = parameterApi.getParams(image);

        call.enqueue(new Callback<Params>() {
            @Override
            public void onResponse(Call<Params> call, Response<Params> response) {
                Params parametros = response.body();

                para[0] = parametros.getAlp0();
                para[1] = parametros.getLamb0();

            }

            @Override
            public void onFailure(Call<Params> call, Throwable t) {

            }
        });
        return para;
    }


    int[] pars = executeSend(d3);

    int alpha = pars[0];
    int lambda = pars[1];
    int cromA = pars[1]/100;

    //int lvl = (int)(Math.random()*5+1);
    int lvl = 4;



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
            return new MagnificatorGdownIdeal(
                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
                    ((App) context.getApplication()).getAppData().getVideoDir(),
                    alpha,
                    lvl,
                    ((App) context.getApplication()).getAppData().getFl(),
                    ((App) context.getApplication()).getAppData().getFh(),
                    30,
                    cromA,
                    ((App) context.getApplication()).getAppData().getRoiX(),
                    ((App) context.getApplication()).getAppData().getRoiY());

        } else if (algorithmId == R.id.radio_laplacian_butterworth) {
//            return new MagnificatorLpyrButter(
//                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
//                    ((App) context.getApplication()).getAppData().getVideoDir(),
//                    ((App) context.getApplication()).getAppData().getAlpha(),
//                    ((App) context.getApplication()).getAppData().getLambda(),
//                    ((App) context.getApplication()).getAppData().getFl(),
//                    ((App) context.getApplication()).getAppData().getFh(),
//                    ((App) context.getApplication()).getAppData().getSampling(),
//                    ((App) context.getApplication()).getAppData().getChromAtt(),
//                    ((App) context.getApplication()).getAppData().getRoiX(),
//                    ((App) context.getApplication()).getAppData().getRoiY()
//            );
            return new MagnificatorLpyrButter(
                    ((App) context.getApplication()).getAppData().getAviVideoPath(),
                    ((App) context.getApplication()).getAppData().getVideoDir(),
                    alpha,
                    lambda,
                    60,
                    100,
                    30,
                    cromA,
                    325,
                    140
            );
        }
        // TODO: Unknown algorithm

        return null;
    }
}
