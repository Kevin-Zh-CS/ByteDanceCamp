package com.bytedance.androidcamp.network.dou.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bytedance.androidcamp.network.dou.R;
import com.bytedance.androidcamp.network.dou.api.IMiniDouyinService;
import com.bytedance.androidcamp.network.dou.model.PostVideoResponse;
import com.bytedance.androidcamp.network.dou.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoverActivity extends Activity {
    private Button chooseButton;
    private Button postButton;
    private ImageView imageView;
    private Uri coverImage=null;
    private String videoPath;
    private String mId="18366345568";
    private String mName="长者";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover_choose);
        chooseButton=findViewById(R.id.choose);
        postButton=findViewById(R.id.post);
        imageView=findViewById(R.id.image);
        videoPath=getIntent().getStringExtra("VideoPath");
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        final Bitmap bitmap = media.getFrameAtTime();
        imageView = (ImageView)this.findViewById(R.id.cover);
        imageView.setImageBitmap(bitmap);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imagef;
                if(coverImage!=null) {
                    imagef = new File(ResourceUtils.getRealPath(CoverActivity.this, coverImage));
                }
                else{
                    imagef =compressImage(bitmap);
                }
                RequestBody imageFile = RequestBody.create(MediaType.parse("multipart/form-data;charset=utf-8"), imagef);
                MultipartBody.Part coverImagePart= MultipartBody.Part.createFormData("cover_image", imagef.getName(), imageFile);
                File videof=new File(videoPath);
                RequestBody videoFile = RequestBody.create(MediaType.parse("multipart/form-data;charset=utf-8"), videof);
                MultipartBody.Part videoPart =MultipartBody.Part.createFormData("video", videof.getName(), videoFile);
                miniDouyinService.postVideo(mId, mName, coverImagePart, videoPart).enqueue(
                        new Callback<PostVideoResponse>() {
                            @Override
                            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                                if (response.body() != null) {
                                    Toast.makeText(CoverActivity.this, response.body().toString(), Toast.LENGTH_SHORT)
                                            .show();
                                }
                                CoverActivity.this.finish();
                            }
                            @Override
                            public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                                Toast.makeText(CoverActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("CoverActivity", "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        if (null != data) {
                coverImage = data.getData();
                Log.d("CoverActivity", "selectedImage = " + coverImage);
                chooseButton.setText("重新选择");
                imageView.setImageURI(coverImage);
        }
    }
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //图片名
        String filename = format.format(date);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/"+filename + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        // recycleBitmap(bitmap);
        return file;
    }
}
