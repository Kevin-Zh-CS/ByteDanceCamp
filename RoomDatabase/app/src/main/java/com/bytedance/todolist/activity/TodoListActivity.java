package com.bytedance.todolist.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.bytedance.todolist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private TodoListAdapter mAdapter;
    private FloatingActionButton mFab;
    TodoListDao dao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TodoListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);


        mAdapter.setOnItemClickListener(new TodoListAdapter.OnItemClickListener() {
            @Override
            public void onCheck(View view, int position, boolean isChecked, final long ID) {
                if (isChecked) {
                    new Thread() {
                        @Override
                        public void run() {
                            dao.finishTodo(1+"",ID+"");
                            loadFromDatabase();
                        }
                    }.start();

                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            dao.finishTodo(0+"",ID+"");
                            loadFromDatabase();
                        }
                    }.start();
                }
            }

            @Override
            public void onButtonClick(View view, int position,final long ID){
                new Thread() {
                    @Override
                    public void run() {
                        dao.deleteTodo(ID+"");
                        loadFromDatabase();
                    }
                }.start();
            }
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoListActivity.this, TakeNote.class);
                startActivityForResult(intent, 233);
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        dao.deleteAll();
                        for (int i = 0; i < 20; ++i) {
                            dao.addTodo(new TodoListEntity("This is " + i + " item", new Date(System.currentTimeMillis())));
                        }
                        Snackbar.make(mFab, R.string.hint_insert_complete, Snackbar.LENGTH_SHORT).show();
                        loadFromDatabase();
                    }
                }.start();
                return true;
            }

        });
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        new Thread() {
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                final List<TodoListEntity> entityList = dao.loadAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(entityList);
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 233) {
            if (resultCode == RESULT_OK && data != null) {
                final String result = data.getStringExtra(TakeNote.KEY);
                new Thread() {
                    @Override
                    public void run() {
                        dao.addTodo(new TodoListEntity(result, new Date(System.currentTimeMillis())));
                        loadFromDatabase();
                    }
                }.start();
            }
        }

    }
}
