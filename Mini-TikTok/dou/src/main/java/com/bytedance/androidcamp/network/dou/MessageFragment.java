package com.bytedance.androidcamp.network.dou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.bytedance.androidcamp.network.dou.recycler.MyAdapter;
import com.bytedance.androidcamp.network.dou.recycler.TestDataSet;
import com.bytedance.androidcamp.network.dou.recycler2.MyAdapter2;
import com.bytedance.androidcamp.network.dou.recycler2.TestDataSet2;

import java.util.Objects;

public class MessageFragment extends Fragment {
    private MyAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recyclerbelow);
        RecyclerView recyclerView2 = Objects.requireNonNull(getView()).findViewById(R.id.recyclerupper);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        myAdapter = new MyAdapter(TestDataSet.getMessageSet());
        MyAdapter2 myAdapter2 = new MyAdapter2(TestDataSet2.getMessageSet2());
        recyclerView.setAdapter(myAdapter);
        recyclerView2.setAdapter(myAdapter2);
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));

    }

}
