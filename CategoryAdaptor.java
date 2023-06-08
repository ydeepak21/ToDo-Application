package com.example.final_todo.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_todo.R;
import com.example.final_todo.model.Category;

import java.util.List;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.CategoryView> {

    public List<Category> categoryList;

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    private OnTaskClickListner onTaskClickListner;

    public List<Category> getCategoryList(){
        return categoryList;
    }


    public CategoryAdaptor(OnTaskClickListner onTaskClickListner){
        this.onTaskClickListner = onTaskClickListner;
    }

    public void removeCategoryAt(int position){ //notify where item was removed
        categoryList.remove(position);
        notifyItemRemoved(position);
    }



    @NonNull
    @Override
    public CategoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitemlayout, parent, false);
        CategoryView categoryView = new CategoryView(view, onTaskClickListner);
        return categoryView;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryView holder, int position) {
        holder.tvCategoryName.setText(categoryList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public class CategoryView extends  RecyclerView.ViewHolder implements  View.OnClickListener {

        TextView tvCategoryName;
        OnTaskClickListner onTaskClickListner;
        public CategoryView(@NonNull View itemView, OnTaskClickListner onTaskClickListner) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.category_item_layout_txt_category);
            this.onTaskClickListner = onTaskClickListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTaskClickListner.onItemClick(getAdapterPosition());
        }
    }

    public interface  OnTaskClickListner{
        void onItemClick(int position);
    }
}
