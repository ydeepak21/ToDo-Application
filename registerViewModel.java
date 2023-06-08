package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.User;

public class registerViewModel extends AndroidViewModel {
    Repository repository;
    private LiveData<User> userLiveData;


    public registerViewModel(@NonNull Application application) {
        super(application);

        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);

    }

    public void insertUser(User user) {
        repository.insertUser(user);
    }
//    public void loadUserById(int userId) {
//        userLiveData = repository.loadUserById(userId);
//    }

//    public LiveData<User> getUserLiveData() {
//        return userLiveData;
//    }

}
