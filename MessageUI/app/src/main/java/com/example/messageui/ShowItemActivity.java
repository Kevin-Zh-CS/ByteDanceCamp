package com.example.messageui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item);
        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos",0);
        TextView textView = findViewById(R.id.textView2);
        textView.setText("这是第" + pos + "条");

    }
}
