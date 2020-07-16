package com.bytedance.androidcamp.network.dou;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;

public class VideoActivity extends AppCompatActivity {
    boolean startPlay;
    private VideoView videoView;
    private LottieAnimationView heart;
    private ImageView videoPause;

    public static void launch(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String url = getIntent().getStringExtra("url");
        videoView = findViewById(R.id.video_container);
        heart = findViewById(R.id.heart);
        videoPause = findViewById(R.id.imageView);
        videoPause.bringToFront();
        //final ProgressBar progressBar = findViewById(R.id.progress_bar);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                startPlay = true;
            }
        });

        heart.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(animation.getAnimatedFraction() == 1f){
                    if(heart.getVisibility() == View.VISIBLE){
                        heart.setVisibility(View.GONE);
                    }
                }
            }
        });

        videoView.setOnTouchListener(new Myclick(new Myclick.MyClickCallBack() {
            @Override
            public void oneClick() {
                if(startPlay) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        videoPause.setVisibility(View.VISIBLE);
                    } else {
                        videoView.start();
                        videoPause.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void doubleClick() {
                if(startPlay) {
                    heart.playAnimation();
                    heart.setVisibility(View.VISIBLE);
                }
            }
        }));

        //progressBar.setVisibility(View.VISIBLE);
    }


}
