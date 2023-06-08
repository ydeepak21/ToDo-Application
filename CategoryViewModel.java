package com.example.final_todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    Repository repository;
    private LiveData<List<Category>> categoryList;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);

        repository = new Repository(appDatabase);
        categoryList = repository.loadAllCategory();
    }
    public void removeAllCategory(){
        repository.deleteAllCategory();
    }


    public void  saveCategory(Category category){
        repository.insertCategory(category);
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }
    public LiveData<Category> getCategoryIdByName(String categoryName){
        return repository.loadCategoryIdByName(categoryName);
    }
    public LiveData<Category> loadCategoryNameById(int categoryId){
        return repository.loadCategoryNameById(categoryId);
    }

    public void updateCategory(Category existingCategory) {
        repository.updateCategory(existingCategory);

    }
    public LiveData<Category> getCategoryByName(String categoryName) {
        return repository.getCategoryByName(categoryName);
    }

}
