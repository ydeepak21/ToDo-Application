package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.User;

public class loginViewModel extends AndroidViewModel {
    Repository repository;
    private LiveData<User> userLiveData;


    public loginViewModel(@NonNull Application application) {
        super(application);

        AppDatabase appDatabase = AppDatabase.getInstance(application);
        repository = new Repository(appDatabase);
    }
    public LiveData<User> getUserByEmail(String email, String password) {
        userLiveData = repository.getUserByEmail(email,password);
        return userLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

}

