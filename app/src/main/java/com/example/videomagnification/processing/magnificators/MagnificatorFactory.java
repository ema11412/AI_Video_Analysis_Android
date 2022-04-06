package com.example.videomagnification.processing.magnificators;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.videomagnification.R;
import com.example.videomagnification.application.App;
import com.example.videomagnification.inter.parameterApi;
import com.example.videomagnification.model.Params;

import java.io.IOException;
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

    // new Peticion().execute();
    public static class NetworkClient {

        public static final String BASE_URL="http://192.168.0.114:5000/";

        public static Retrofit retrofit;

   /*
    This public static method will return Retrofit client
    anywhere in the appplication
    */

        public static Retrofit getRetrofitClient(){

            //If condition to ensure we don't create multiple retrofit instances in a single application
            if (retrofit==null) {

                //Defining the Retrofit using Builder
                retrofit=new Retrofit.Builder()
                        .baseUrl(BASE_URL)   //This is the only mandatory call on Builder object.
                        .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                        .build();
            }

            return retrofit;
        }

    }


    private void getParameters(String image) {

        //Obtain an instance of Retrofit by calling the static method.
        Retrofit  retrofit= NetworkClient.getRetrofitClient();

       /*
       The main purpose of Retrofit is to create HTTP calls from the Java interface based
       on the annotation associated with each method. This is achieved by just passing the interface
       class as parameter to the create method
       */
        parameterApi weatherAPIs = retrofit.create(parameterApi.class);

       /*
       Invoke the method corresponding to the HTTP request which will return a Call object. This Call
       object will used to send the actual network request with the specified parameters
       */


        Call call = weatherAPIs.getParams(image);

       /*
        This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously
        It has two callback listeners which will invoked on the main thread
        */
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
              /*This is the success callback. Though the response type is JSON, with Retrofit we get
              the response in the form of WResponse POJO class
              */
                if (response.body()!=null) {
                    Params params = (Params) response.body();

                    int alp0 = params.getAlp0();
                    int lamb0 = params.getLamb0();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
              /*
              Error callback
              */

            }
        });

    }


}

