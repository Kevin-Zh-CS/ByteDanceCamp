package com.bytedance.androidcamp.network.dou.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bytedance.androidcamp.network.dou.R;
import com.bytedance.androidcamp.network.dou.util.FormatUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraActivity extends Activity {

    private String tag ="CameraActivity";
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button vedioButton;  //摄像按钮
    private TextView timeTextView;
    private List<Camera.Size> vsizes;
    private int maxVWidth=0;
    private int maxVHeight=0;

    protected boolean isPreview = false; //摄像区域是否准备良好
    private boolean isRecording = true; // true表示没有录像，点击开始；false表示正在录像，点击暂停
    private boolean bool;

    private int hour = 0;
    private int minute = 0;     //计时专用
    private int second = 0;

    private File mRecVedioPath;
    private File mRecAudioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        initCamera();
        initViews();
    }
    //初始化摄像头
    private void initCamera() {
        mRecVedioPath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ "/DCIM/Camera/");
        if (!mRecVedioPath.exists()) {
            mRecVedioPath.mkdirs();
        }
        surfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera = Camera.open();
                    vsizes=camera.getParameters().getSupportedVideoSizes();
                    for(int i=0;i<vsizes.size();i++)
                        if(vsizes.get(i).width>maxVWidth){
                            maxVWidth=vsizes.get(i).width;
                            maxVHeight=vsizes.get(i).height;
                        }
                    //设置Camera的角度/方向
                    camera.setDisplayOrientation(90);
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPreviewFrameRate(30); // 每秒30帧
                    parameters.setPictureFormat(ImageFormat.JPEG);// 设置照片的输出格式
                    parameters.set("jpeg-quality", 100);// 照片质量
                    List<String> focusModes = parameters.getSupportedFocusModes();
                    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    camera.setParameters(parameters);
                    camera.setPreviewDisplay(holder);
                    isPreview = true;
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                surfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                surfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    if (isPreview) {
                        camera.stopPreview();
                        isPreview = false;
                    }
                    camera.release();
                    camera = null; // 记得释放Camera
                }
                surfaceView = null;
                surfaceHolder = null;
                mediaRecorder = null;
            }
        });
        //开发时建议设置
        //This method was deprecated in API level 11. this is ignored, this value is set automatically when needed.
        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    //初始化视图组件
    private void initViews() {
        timeTextView = (TextView) findViewById(R.id.camera_time);
        timeTextView.setVisibility(View.GONE);
        vedioButton = (Button) findViewById(R.id.camera_vedio);
        ButtonOnClickListener onClickListener = new ButtonOnClickListener();
        vedioButton.setOnClickListener(onClickListener);
    }

    class ButtonOnClickListener implements OnClickListener{
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.camera_vedio:
                    //点击开始录像
                    if(isRecording){
                        second = 0;
                        minute = 0;
                        hour = 0;
                        bool = true;
                        if(null==mediaRecorder){
                            mediaRecorder = new MediaRecorder();
                        }else {
                            mediaRecorder.reset();
                        }
                        camera.unlock();
                        mediaRecorder.setCamera(camera);
                        mediaRecorder.setVideoEncodingBitRate(20*1024*1024);
                        mediaRecorder.setOrientationHint(90);
                        //表面设置显示记录媒体（视频）的预览
                        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                        //开始捕捉和编码数据到setOutputFile（指定的文件）
                        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        //设置用于录制的音源
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                        //设置在录制过程中产生的输出文件的格式
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        //设置视频编码器，用于录制
                        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                        //设置audio的编码格式
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        //设置要捕获的视频的宽度和高度
                        mediaRecorder.setVideoSize(maxVWidth, maxVHeight);
                        // 设置要捕获的视频帧速率
                        mediaRecorder.setVideoFrameRate(30);
                        try {
                            mRecAudioFile = File.createTempFile("Vedio", ".mp4",
                                    mRecVedioPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
                        try {
                            mediaRecorder.prepare();
                            timeTextView.setVisibility(View.VISIBLE);
                            handler.postDelayed(task, 1000);
                            mediaRecorder.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isRecording = !isRecording;
                        Log.e(tag, "=====开始录制视频=====");
                    }else {
                        //点击停止录像
                        @SuppressLint("SimpleDateFormat") String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                                .format(new Date()) + ".mp4";
                        try {
                            bool = false;
                            mediaRecorder.setPreviewDisplay(null);
                            mediaRecorder.stop();
                            timeTextView.setText(FormatUtil.format(hour) + ":" + FormatUtil.format(minute) + ":"
                                    + FormatUtil.format(second));
                            mediaRecorder.release();
                            camera.lock();
                            camera.setPreviewDisplay(surfaceHolder);
                            mediaRecorder = null;
                            FormatUtil.videoRename(mRecAudioFile,fileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isRecording = !isRecording;
                        Log.e(tag, "=====录制完成，已保存=====");
                        Intent videoIntent=new Intent(CameraActivity.this,VideoActivity.class);
                        videoIntent.putExtra("Path", Environment.getExternalStorageDirectory()
                                .getAbsolutePath()+ "/DCIM/Camera/"+fileName);
                        videoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        CameraActivity.this.startActivity(videoIntent);
            }
                    break;
                default:
                    break;
            }
        }
    }
    /*
     * 定时器设置，实现计时
     */
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        @SuppressLint("SetTextI18n")
        public void run() {
            if (bool) {
                handler.postDelayed(this, 1000);
                second++;
                if (second >= 60) {
                    minute++;
                    second = second % 60;
                }
                if (minute >= 60) {
                    hour++;
                    minute = minute % 60;
                }
                timeTextView.setText(FormatUtil.format(hour) + ":" + FormatUtil.format(minute) + ":"
                        + FormatUtil.format(second));
            }
        }
    };
    @Override
    public void onRestart(){
        super.onRestart();
        finish();
    }
}