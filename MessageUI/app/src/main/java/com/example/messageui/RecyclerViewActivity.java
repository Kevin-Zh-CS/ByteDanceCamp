package com.example.messageui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messageui.recycler.MyAdapter;
import com.example.messageui.recycler.TestData;
import com.example.messageui.recycler.TestDataSet;
import com.example.messageui.recycler2.MyAdapter2;
import com.example.messageui.recycler2.TestDataSet2;

public class RecyclerViewActivity extends AppCompatActivity implements MyAdapter.IOnItemClickListener {

    private static final String TAG = "TAG233";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private MyAdapter myAdapter;
    private MyAdapter2 myAdapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        recyclerView = findViewById(R.id.recyclerbelow);
        recyclerView2 = findViewById(R.id.recyclerupper);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        myAdapter = new MyAdapter(TestDataSet.getMessageSet());
        myAdapter2 = new MyAdapter2(TestDataSet2.getMessageSet2());
        myAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(myAdapter);
        recyclerView2.setAdapter(myAdapter2);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.i(TAG, "RecyclerViewActivity onStart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.i(TAG, "RecyclerViewActivity onResume");
//    }

    @Override
    public void onItemCLick(int position, TestData data) {
        Intent intent = new Intent(this, ShowItemActivity.class);
        intent.putExtra("pos", position);
        startActivity(intent);
    }

    @Override
    public void onItemLongCLick(int position, TestData data) {
        Toast.makeText(RecyclerViewActivity.this, "删除了第" + position + "条", Toast.LENGTH_SHORT).show();
        myAdapter.removeData(position);
    }
}
