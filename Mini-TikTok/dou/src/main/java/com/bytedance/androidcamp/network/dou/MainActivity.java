package com.bytedance.androidcamp.network.dou;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bytedance.androidcamp.network.dou.camera.CameraActivity;

import java.lang.reflect.Field;
import java.text.Format;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    Fragment videoFragment=null;
    Fragment messageFragment=null;
    TabLayout mainTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTab = findViewById(R.id.main_tab);
        setIndicator(mainTab,25, 25);
        ImageView view=new ImageView(this);
        view.setImageResource(R.drawable.cam);
        mainTab.getTabAt(1).setCustomView(view);
        Objects.requireNonNull(mainTab.getTabAt(0)).select();
        @SuppressLint("CommitTransaction") final FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        if(null == videoFragment){
            videoFragment = new VideoFragment();
            tr.add(R.id.home_container, videoFragment);
        }
        if(messageFragment!=null){
            tr.hide(messageFragment);
        }
        tr.show(videoFragment).commit();
        //        Fragment videoFragment = new VideoFragment();
//        Fragment messageFragment = new MessageFragment();
        mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                @SuppressLint("CommitTransaction")
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (tab.getPosition()){
                    case 0:
                        if(null == videoFragment){
                            videoFragment = new VideoFragment();
                            transaction.add(R.id.home_container, videoFragment);
                        }
                        if(messageFragment!=null){
                            transaction.hide(messageFragment);
                        }
                        transaction.show(videoFragment).commit();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragments[0]).commit();
                        break;
                    case 1:
                        Intent cameraIntent=new Intent(MainActivity.this, CameraActivity.class);
                        MainActivity.this.startActivity(cameraIntent);
                        break;
                    case 2:
                        if(null == messageFragment){
                            messageFragment = new MessageFragment();
                            transaction.add(R.id.home_container, messageFragment);
//                            getSupportActionBar().hide();//隐藏标题栏
                        }
                        if(videoFragment!=null){
                            transaction.hide(videoFragment);
                        }
                        transaction.show(messageFragment).commit();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragments[1]).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    Intent cameraIntent=new Intent(MainActivity.this, CameraActivity.class);
                    MainActivity.this.startActivity(cameraIntent);
                }
            }
        });

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        mainTab.setScrollPosition(0, 0, true);
    }

//    /**
//     * fragment替换（不保留之前的状态）
//     *
//     * @param to
//     * @param i
//     */
//    public void switchContent(Fragment to, int i) {
//        bundle.putString("text", textStrings[i]);
//        to.setArguments(bundle);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, to).commit();
//    }
public  static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
    Class<?> tabLayout = tabs.getClass();
    Field tabStrip = null;
    try {
        tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
    } catch (NoSuchFieldException e) {
        e.printStackTrace();
    }

    tabStrip.setAccessible(true);
    LinearLayout llTab = null;
    try {
        llTab = (LinearLayout) tabStrip.get(tabs);
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    }

    int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
    int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
    for (int i = 0; i < llTab.getChildCount(); i++) {
        View child = llTab.getChildAt(i);
        child.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.leftMargin = left;
        params.rightMargin = right;
        child.setLayoutParams(params);
        child.invalidate();
    }
}
    public CharSequence getSequence(){
        Drawable image = ContextCompat.getDrawable(MainActivity.this, R.drawable.cam);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
