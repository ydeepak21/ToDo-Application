package com.example.final_todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.final_todo.viewmodel.CategoryViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if(intent != null){
            Integer catId = intent.getIntExtra("categoryId",-1);
            if(catId != -1){
                replaceFragmentCategory();
            }
        }
    }

    public void replaceFragmentCategoryList(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, CategoryListFragment.class, null)
                .addToBackStack("CategoryList")
                .setReorderingAllowed(true)
                .commit();
    }

    public void replaceFragmentCategory(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, CategoryFragment.class, null)
                .addToBackStack("Category")
                .setReorderingAllowed(true)
                .commit();
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater(); //Inflate le xmla file laii java code maa readable banaauxa
        inflater.inflate(R.menu.mainmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_newcategory:
                replaceFragmentCategory();
                return true;
            case R.id.main_menu_clear_category:
                CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
                categoryViewModel.removeAllCategory();
                replaceFragmentCategoryList();
            return true;
            case R.id.main_menu_TodoList_category:
                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);
                return true;

            case R.id.main_menu_logout:
                SharedPreferences sharedPreferences = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("loginToken");
                editor.apply();

                intent = new Intent(this, FirstActivity.class);
                startActivity(intent);
                return  true;
        }
        return true;
    }
}