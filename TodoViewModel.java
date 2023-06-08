package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private LiveData<List<Todo>> todoList;
    Repository repository;
    public TodoViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);
    }

    public void saveTodo(Todo todo) {
        repository.insertTodo(todo);
    }

    public LiveData<List<Todo>> getTodoList() {
        todoList = repository.loadAllTodo();
        return todoList;
    }

    public LiveData<Todo>loadTodoById(int todoId){
       return repository.loadTodoById(todoId);
    }
    public LiveData<List<Todo>>loadByCategoryId(int categoryId){
        return repository.loadByCategoryId(categoryId);
    }
    public void updateTodo(Todo todo) {
        repository.updateTodo(todo);
    }
}
