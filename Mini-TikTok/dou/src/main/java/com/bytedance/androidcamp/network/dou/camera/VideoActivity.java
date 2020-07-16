package com.bytedance.androidcamp.network.dou.camera;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.bytedance.androidcamp.network.dou.R;

import java.io.IOException;

public class VideoActivity extends Activity {
    private SurfaceView surfaceView;
    private SeekBar seekBar;
    private Button next_Button;
    private Button play_Button;
    private String videoPath;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private Boolean isPlay=false;
    private boolean started=false;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_review);
        surfaceView=findViewById(R.id.surfaceview);
        seekBar=findViewById(R.id.seekbar);
        next_Button=findViewById(R.id.next);
        play_Button=findViewById(R.id.play);
        videoPath=this.getIntent().getStringExtra("Path");
        mediaPlayer=new MediaPlayer();
        initSurfaceviewListener();
        play_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!started){
                    seekBar.setMax(mediaPlayer.getDuration());
                    handler.postDelayed(SeekBarRenew, 100);
                    started=true;
                }
                if(isPlay){
                    isPlay=!isPlay;
                    play_Button.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    isPlay=!isPlay;
                    play_Button.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        next_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CoverActivity=new Intent(VideoActivity.this, com.bytedance.androidcamp.network.dou.camera.CoverActivity.class);
                CoverActivity.putExtra("VideoPath", videoPath);
                VideoActivity.this.startActivity(CoverActivity);
            }
        });
    }

    private void initSurfaceviewListener(){
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(new Callback()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(holder);//给mMediaPlayer添加预览的SurfaceHolder
                try {
                    mediaPlayer.setDataSource(VideoActivity.this, Uri.parse(videoPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mediaPlayer.prepareAsync();//异步准备
                mediaPlayer.stop();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder=holder;
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                surfaceView=null;
                surfaceHolder=null;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlay=false;
                play_Button.setBackgroundResource(R.drawable.play);
            }
        });
    }
    private Handler handler=new Handler();
    private Runnable SeekBarRenew =new Runnable() {
        public void run() {
                handler.postDelayed(this, 100);
                if(mediaPlayer!=null&&isPlay)
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
        }
    };
    @Override
    public void onRestart(){
        super.onRestart();
        finish();
    }
}
