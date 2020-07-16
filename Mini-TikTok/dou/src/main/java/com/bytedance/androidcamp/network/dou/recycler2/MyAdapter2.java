package com.bytedance.androidcamp.network.dou.recycler2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bytedance.androidcamp.network.dou.R;

import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder>{
    private List<TestData2> dataSet2;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            textView = itemView.findViewById(R.id.textView3);
        }

        public void onBind2(TestData2 data) {
            imageView.setImageResource(data.uri_2);
            textView.setText(data.content_2);

        }

    }

    public MyAdapter2(List<TestData2> myDataset2) {
        dataSet2 = myDataset2;
    }

    @NonNull
    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapter2.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_hint,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.MyViewHolder holder, int position) {
        holder.onBind2(dataSet2.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet2.size();
    }


}
