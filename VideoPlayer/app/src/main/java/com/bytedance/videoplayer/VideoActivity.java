package com.bytedance.videoplayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.video_container);
        progressBar = findViewById(R.id.progress_bar);
        videoView.setMediaController(new MediaController(this));
        Intent intent = getIntent();
        String str = intent.getStringExtra("url");
        if (str.equals("local")) {
            videoView.setVideoURI(Uri.parse("https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4"));
        }
        else {
            videoView.setVideoURI(Uri.parse(str));
        }
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        ActionBar supportActionBar = getSupportActionBar();
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(layoutParams);

            supportActionBar.hide();
            progressBar.setVisibility(View.VISIBLE);

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = 600;
            videoView.setLayoutParams(layoutParams);

            supportActionBar.show();
            progressBar.setVisibility(View.GONE);
        }
    }
}