package com.example.customcamera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

/**
 * 相机
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private Button start, stop;
    //旋转相机
    private ImageView rotate;
    //相机录制视频存储的位置
    private String path;
    //将相机捕捉的画面的显示在SurfaceView
    private SurfaceView mSurfaceView;
    //相机
    private Camera camera;
    //录制
    private MediaRecorder mediaRecorder;
    //0代表前置摄像头，1代表后置摄像头
    private int cameraPosition = 1;

    private SurfaceHolder surfaceHolder;
    private int filename = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        initSurfaceViewData();
        setLisiner();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        rotate = (ImageView) findViewById(R.id.carmera);
        mSurfaceView = (SurfaceView) findViewById(R.id.sufaceView);
    }

    /**
     * 初始化SurfaceView
     */
    private void initSurfaceViewData() {
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //设置屏幕常亮
        mSurfaceView.setKeepScreenOn(true);

        SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

            //在控件创建的时候，进行相应的初始化工作
            public void surfaceCreated(SurfaceHolder holders) {
                //打开相机，同时进行各种控件的初始化mediaRecord等
                mediaRecorder = new MediaRecorder();
                surfaceHolder = holders;
                changeView();
            }

            //当控件发生变化的时候调用，进行surfaceView和camera进行绑定，可以进行画面的显示
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

        };
        //为SurfaceView设置回调函数
        mSurfaceView.getHolder().addCallback(callback);
    }

    /**
     * 设置监听
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setLisiner() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        rotate.setOnClickListener(this);
        //监听surfaceView点击，设置相机聚焦
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (camera != null) {
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                Log.e("Camer", "聚焦成功");
                            } else {
                                Log.e("Camer", "聚焦成功");
                            }
                        }
                    });
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                //判断SD卡是否存在
                if (creatFiles()) startRecord();
                break;
            case R.id.stop:
                if (creatFiles()) stopRecord();
                break;
            case R.id.carmera:
                changeView();
                break;
        }
    }

    /**
     * 创建文件的存储路径
     *
     * @return
     */
    private boolean creatFiles() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            //获取SD卡根目录的绝对路径+自建包名
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kevin";
            File file = new File(path);
            //判断改路径是否存在，不存在创建
            if (!file.exists()) {
                file.mkdir();
            }
            return true;
        } else {
            Toast.makeText(this, "SD卡不存在", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void startRecord() {
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        if (cameraPosition == 0) {
//            前置摄像头
            mediaRecorder.setOrientationHint(270);
        } else {
            //后置摄像头
            mediaRecorder.setOrientationHint(90);
        }
        //设置音频的来源  麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置视频的来源Camera
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //设置视频的输出格式  3gp
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //设置视频中的声音和视频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);


        WindowManager manager = getWindowManager();
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        mediaRecorder.setVideoSize(width,height);

        //设置录制视频的帧数,必须在setVideoSource()和setOutputFormat之后调用
        mediaRecorder.setVideoFrameRate(60);
        //设置保存的路径
        String string = setVidioName(path);
        mediaRecorder.setOutputFile(string);
        //开始录制
        try {
            //准备录制
            mediaRecorder.prepare();
            //开始
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void stopRecord() {
        //当结束录制之后，就将当前的资源都释放
        mediaRecorder.release();
        mediaRecorder = null;
        //然后再重新初始化所有的必须的控件和对象
        mediaRecorder = new MediaRecorder();
        //录制结束，相机前后显示的方向保持不变
        if (cameraPosition == 1) {
            cameraPosition = 0;
        } else {
            cameraPosition = 1;
        }
        changeView();
    }


    private String setVidioName(String path) {
        File file = new File(path + "/vidio" + filename + ".mp4");
        //判断改路径是否存在，存在创建其他的文件名
        if (!file.exists()) {

        } else {
            filename += 1;
            setVidioName(path + "/vidio" + filename + ".mp4");
        }
        return path + "/vidio" + filename + ".mp4";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出销毁，释放资源
        mediaRecorder.release();
        camera.release();
        mediaRecorder = null;
    }

    /**
     * 切换前后摄像头
     */
    public void changeView() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    if (camera != null) {
                        camera.stopPreview();//停掉原来摄像头的预览
                        camera.release();//释放资源
                        camera = null;//取消原来摄像头
                    }
                    camera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                        //设置相机角度，旋转90，因为相机默认是横屏
                        camera.setDisplayOrientation(90);
                        //自动聚焦
                        camera.cancelAutoFocus();
                        camera.startPreview();//开始预览
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    if (camera != null) {
                        camera.stopPreview();//停掉原来摄像头的预览
                        camera.release();//释放资源
                        camera = null;//取消原来摄像头
                    }
                    camera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                        //设置相机显示方向（后置90）
                        camera.setDisplayOrientation(90);
                        //自动聚焦
                        camera.cancelAutoFocus();
                        camera.startPreview();//开始预览
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }

}

