package com.bytedance.androidcamp.network.dou.recycler;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bytedance.androidcamp.network.dou.R;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private List<TestData> dataSet;
    private IOnItemClickListener itemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView tvSender;
        private TextView tvMessage;
        private TextView tvTime;
        private View contentView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView;
            tvSender = itemView.findViewById(R.id.sender);
            tvMessage = itemView.findViewById(R.id.message);
            tvTime = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.imageView2);
        }

        public void onBind(TestData data){
            imageView.setImageResource(data.uri);
            tvTime.setText(data.time);
            tvMessage.setText(data.message);
            tvSender.setText(data.sender);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            if (listener != null) {
                contentView.setOnClickListener(listener);
            }
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            if (listener != null) {
                contentView.setOnLongClickListener(listener);
            }
        }

    }

    public MyAdapter(List<TestData> myDataset) {
        dataSet = myDataset;
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        itemClickListener = listener;
    }



    public void removeData(int position) {
        if (null != dataSet && dataSet.size() > position) {
            dataSet.remove(position);
            notifyItemRemoved(position);
            if (position != dataSet.size()) {
                //刷新改变位置item下方的所有Item的位置,避免索引错乱
                notifyItemRangeChanged(position, dataSet.size() - position);
            }
        }
    }


    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_message,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.onBind(dataSet.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemCLick(position, dataSet.get(position));
                }
            }
        });
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongCLick(position, dataSet.get(position));
                }
                return false;
            }

        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface IOnItemClickListener {

        void onItemCLick(int position, TestData data);

        void onItemLongCLick(int position, TestData data);
    }
}
