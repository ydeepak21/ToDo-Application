package com.example.final_todo.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;
import com.example.final_todo.model.User;

import java.util.List;

public class Repository {
    private CategoryDao categoryRepostiroy;

    private TodoDao todoRepository;

    private UserDao userRepository;

    public Repository(AppDatabase appDatabase) {
        this.categoryRepostiroy = appDatabase.categoryDao();
        this.todoRepository = appDatabase.todoDao();
        this.userRepository = appDatabase.userDao();
    }

    public void insertCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.insertCategory(category);
        });
    }
    public void deleteTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.delete(todo);
        });
    }

    public void updateCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.updateCategory(category);
        });
    }
    public void updateTodo(Todo todo){
        AppDatabase.databaseWriteExecutor.execute(()->{
            todoRepository.updateTodo(todo);
        });
    }

    public void deleteCategory(Category category){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.deleteCategory(category);
        });
    }

    public LiveData<List<Category>> loadAllCategory(){
        return categoryRepostiroy.loadAllCategory();
    }


    public void insertTodo(Todo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.insert(todo);
        });
    }

    public void insertUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userRepository.insertUser(user);
        });
    }

    public LiveData<Category> loadCategoryNameById(int categoryId){
            return categoryRepostiroy.loadCategoryNameById(categoryId);
    }
//    public void updateUser(User user) {
//        AppDatabase.databaseWriteExecutor.execute(() -> {
//            userRepository.updateUser(user);
//        });
//    }
//    public LiveData<List<User>> loadAllUsers() {
//        return userRepository.getAllUsers();
//    }
    public LiveData<User> getUserByEmail(String email, String password) {
        return userRepository.getUserByEmail(email, password);
    }

    public LiveData<List<Todo>> loadAllTodo(){
        return todoRepository.loadAllTodo();
    }

    public LiveData<Todo>loadTodoById(int todoId){
        return todoRepository.loadTodoById(todoId);
    }
    //Using Method from interface: void deleteAllCompleted();
    public void deleteAllTodo() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteAll();
        });
    }

    public void deleteAllCompletedTodo() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.deleteAllCompleted();
        });
    }
    public void completeTask(int todoId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            todoRepository.completeTask(todoId);
        });
    }
    public LiveData<Category> loadCategoryIdByName(String categoryName){
        return categoryRepostiroy.loadCategoryIdByName(categoryName);
    }

    public void deleteAllCategory(){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoryRepostiroy.deleteAll();
        });
    }

    public LiveData<List<Todo>> loadByCategoryId(int categoryId) {
        return todoRepository.loadByCategoryId(categoryId);

    }
    public LiveData<List<Category>> loadCategoryById(Integer categoryId) {
        return categoryRepostiroy.loadCategoryById(categoryId);
    }
    public LiveData<Category> getCategoryByName(String categoryName) {
        return categoryRepostiroy.getCategoryByName(categoryName);
    }


}
