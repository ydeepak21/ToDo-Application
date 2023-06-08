package com.example.final_todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.final_todo.model.Category;
import com.example.final_todo.viewmodel.CategoryViewModel;

public class CategoryFragment extends Fragment {
    EditText txtCategory;
    CategoryViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        viewModel = new CategoryViewModel(getActivity().getApplication());

        Button btnSave = view.findViewById(R.id.fragment_Category_btn_save);
        txtCategory = view.findViewById(R.id.fragment_category_txt_category);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Integer catId = intent.getIntExtra("categoryId", -1);
            if (catId != -1) {
                viewModel.loadCategoryNameById(catId).observe(getActivity(), category -> {
                    btnSave.setText("Update");
                    txtCategory.setText(category.getCategory());
                    btnSave.setOnClickListener(v -> {
                        category.setCategory(txtCategory.getText().toString());
                        category.setCategoryId(catId);
                        viewModel.updateCategory(category);
                        Toast.makeText(getActivity(), "Category name updated", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).replaceFragmentCategoryList();
                    });
                });
            } else {
                btnSave.setOnClickListener(v -> {
                    Category category = new Category();
                    category.setCategory(txtCategory.getText().toString());
                    viewModel.saveCategory(category);
                    Toast.makeText(getActivity(), "Category Saved", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).replaceFragmentCategoryList();
                });
            }
        }
        return view;
    }
}