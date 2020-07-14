package com.bytedance.todolist.activity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private CheckBox mCheckBox;
    private TextView mContent;
    private TextView mTimestamp;
    private ImageButton mImageButton;
    private Long ID;
    TodoListAdapter.OnItemClickListener mListener;

    public ViewHolder(@NonNull View itemView, final TodoListAdapter.OnItemClickListener onClickListener) {
        super(itemView);
        mCheckBox = itemView.findViewById(R.id.cb_done);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        mImageButton = itemView.findViewById(R.id.ib_delete);
        mListener = onClickListener;

        mImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickListener.onButtonClick(view, position, ID);
                    }
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void bind(TodoListEntity entity) {
        mContent.setText(entity.getContent());
        mTimestamp.setText(formatDate(entity.getTime()));
        ID = entity.getId();

        if(entity.getFinish()) {
            mContent.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); ;
            mCheckBox.setOnCheckedChangeListener(null);
            mCheckBox.setChecked(true);
        }
        else
            mContent.getPaint().setFlags(0);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onCheck(buttonView, position, isChecked, ID);
                    }
                }
            }
        });
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
