package com.example.final_todo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.final_todo.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Query("select * from category where category = :categoryName")
    LiveData<Category> getCategoryByName(String categoryName);


    @Delete
    void deleteCategory(Category category);

    @Query("select * from category order by category")
    LiveData<List<Category>> loadAllCategory();

    @Query("select * from category where categoryId =:categoryId")
    LiveData<List<Category>> loadCategoryById(int categoryId);
    @Query("select categoryId from category where category =:categoryName")
    LiveData<Category> loadCategoryIdByName(String categoryName);

    @Query("select * from category where categoryId =:categoryId")
    LiveData<Category> loadCategoryNameById(int categoryId);


    @Query("delete from category")
    void deleteAll();


}
