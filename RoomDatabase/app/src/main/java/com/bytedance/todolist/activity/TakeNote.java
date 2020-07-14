package com.bytedance.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bytedance.todolist.database.TodoListDao;


import com.bytedance.todolist.R;



public class TakeNote extends AppCompatActivity {
    private EditText editText;
    private Button button;
    public static final String KEY = "result_key";
    TodoListDao dao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_note);
        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(KEY, editText.getText().toString());
                if (editText.getText() == null)
                    setResult(RESULT_CANCELED, intent);
                else
                    setResult(RESULT_OK, intent);

                finish();
            }
        });

    }
}
