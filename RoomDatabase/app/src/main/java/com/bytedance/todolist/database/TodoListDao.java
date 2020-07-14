package com.bytedance.todolist.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface TodoListDao {
    @Query("SELECT * FROM todo")
    List<TodoListEntity> loadAll();

    @Insert
    void addTodo(TodoListEntity entity);

    @Query("DELETE FROM todo")
    void deleteAll();

    @Query("DELETE FROM todo where id=:ID")
    void deleteTodo(String ID);

    @Query("UPDATE todo SET finish=:finish where id=:ID")
    void finishTodo(String finish, String ID);

}
