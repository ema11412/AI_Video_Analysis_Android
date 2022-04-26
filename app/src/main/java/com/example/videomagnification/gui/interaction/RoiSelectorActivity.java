package com.example.videomagnification.gui.interaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videomagnification.R;
import com.example.videomagnification.application.App;
import com.example.videomagnification.inter.ParameterApi;
import com.example.videomagnification.model.Params;
import com.example.videomagnification.model.StatusImg;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoiSelectorActivity extends AppCompatActivity {

    private SeekBar seekBarX;
    private SeekBar seekBarY;
    private TextView textViewX;
    private TextView textViewY;
    ImageView imageView;

    private Bitmap thumbnail;

    private Bitmap thumbnail2;

    private Bitmap preview;
    private Canvas canvas;
    private Paint paint;

    private Button buttonNext;

    private Uri inputVideoUri;
    private Uri thumbnailUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         TODO: verify if preview image is compressed
         TODO: verify if it is always in range

             (0, 0)
             o———————————————————————————————————————————————o————————————————————>
             |            .                                  .
             |            .                                  .
             |            .                                  .
             |            .                                  .
             |            .  (roiX, roiY)                    .
             o . . . . .  x——————————————————————————————————o
             |            |                                  |
             |            |                                  |
             |            |                                  |
             |            |                                  |
             |            |                                  |
             o . . . . .  o——————————————————————————————————x (roiX + 100, roiY + 100)
             |
             |
             |
             |
             v

         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_of_interest);

        String inputFileName = ((App) getApplication()).getAppData().getAviVideoPath();
        String thumbnailFileName = ((App) getApplication()).getAppData().getCompressedVideoPath();

        inputVideoUri = Uri.parse(inputFileName);
        thumbnailUri = Uri.parse(thumbnailFileName);

        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(getApplicationContext(), thumbnailUri);
        thumbnail = mMMR.getFrameAtTime();

        thumbnail2 = mMMR.getFrameAtTime();

        uploadImage(thumbnail2);


        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);

        int imageWidth = thumbnail.getWidth();
        int imageHeight = thumbnail.getHeight();

        ((App) getApplication()).getAppData().setImageWidth(imageWidth);
        ((App) getApplication()).getAppData().setImageHeight(imageHeight);
        imageView = findViewById(R.id.preview_roi);
        imageView.setImageBitmap(thumbnail);

        seekBarX = findViewById(R.id.seek_roi_x);
        seekBarY = findViewById(R.id.seek_roi_y);
        seekBarX.setMax(imageWidth - 100);
        seekBarY.setMax(imageHeight - 100);

        updatePreview();

        textViewX = findViewById(R.id.text_view_roi_x);
        textViewY = findViewById(R.id.text_view_roi_y);

        textViewX.setText(String.valueOf(seekBarX.getProgress()));
        textViewY.setText(String.valueOf(seekBarY.getProgress()));

        buttonNext = findViewById(R.id.btn_next_roi);

        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewX.setText(String.valueOf(seekBarX.getProgress()));
                updatePreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        seekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewY.setText(String.valueOf(seekBarY.getProgress()));
                updatePreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        buttonNext.setOnClickListener(v -> {
            ((App) getApplication()).getAppData().setRoiX(seekBarX.getProgress());
            ((App) getApplication()).getAppData().setRoiY(seekBarY.getProgress());
            startActivity(new Intent(getApplicationContext(), VitalSignSelectorActivity.class));
        });
    }

    private void updatePreview() {
        preview = thumbnail.copy(thumbnail.getConfig(), true);
        canvas = new Canvas(preview);
        float x1 = seekBarX.getProgress();
        float x2 = seekBarY.getProgress();
        canvas.drawRect(x1, x2, x1 + 100, x2 + 100, paint);
        imageView.setImageBitmap(preview);
    }

    // ############################################
    private void uploadImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.114:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ParameterApi api = retrofit.create(ParameterApi.class);


        Call<StatusImg> call = api.upImage(encodedImage);

        call.enqueue(new Callback<StatusImg>() {

            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<StatusImg> call, Response<StatusImg> response) {

                StatusImg stado = response.body();

                int estado = stado.getStatusIMG();

                String elestado = String.valueOf(estado);

                Log.d("Hola", elestado);

            }

            @Override
            public void onFailure(Call<StatusImg> call, Throwable t) {

            }
        });

    }
    // ################################################
}