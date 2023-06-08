package com.example.final_todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_todo.adaptor.CategoryAdaptor;
import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Category;
import com.example.final_todo.viewmodel.CategoryViewModel;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
public class CategoryListFragment extends Fragment implements CategoryAdaptor.OnTaskClickListner {

    CategoryViewModel categoryViewModel;
    RecyclerView categoryRecyclerView;
    private Repository repository;
    private CategoryAdaptor categoryAdaptor;
    private Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        categoryViewModel  = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        categoryAdaptor = new CategoryAdaptor(this::onItemClick);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        categoryRecyclerView.setAdapter(categoryAdaptor);
        // Initialize repository instance using the appDatabase
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
        repository = new Repository(appDatabase);
        categoryViewModel.getCategoryList().observe(getActivity(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdaptor.setCategoryList(categories);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
                categoryRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
                categoryRecyclerView.setAdapter(categoryAdaptor);
            }
        },1000);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                Category category = categoryAdaptor.categoryList.get(position); // Access category directly

                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        repository.deleteCategory(category);
                        Toast.makeText(requireContext(), "Category deleted", Toast.LENGTH_SHORT).show();
                        categoryAdaptor.removeCategoryAt(position); // Remove the item from the adapter immediately
                        break;
                    case ItemTouchHelper.LEFT:
                        Integer catId = category.getCategoryId();
                        startActivity(new Intent(getActivity(), MainActivity.class).putExtra("categoryId", catId));
                        break;

                }
            }
        });
        itemTouchHelper.attachToRecyclerView(categoryRecyclerView);
        return view;
    }
    private void showCategoryTodos(String categoryName){
        categoryViewModel.getCategoryIdByName(categoryName).observe(requireActivity(), (Observer<Category>) category ->{
            if(category != null){
                startActivity(new Intent(getActivity(), MainActivity2.class).putExtra("categoryId", category.getCategoryId()));
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        String text = String.valueOf(categoryAdaptor.getCategoryList().get(position));
        showCategoryTodos(text);
    }
}