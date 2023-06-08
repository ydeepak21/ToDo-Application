package com.example.final_todo.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_todo.R;
import com.example.final_todo.model.Todo;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TodoAdaptor extends RecyclerView.Adapter<TodoAdaptor.TodoView> {
    public List<Todo> todoList;
    private OnTodoClickListener onTodoClickListener;

    public TodoAdaptor(OnTodoClickListener onTodoClickListener) {
        this.onTodoClickListener = onTodoClickListener;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed

    }

    @NonNull
    @Override
    public TodoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todoitemlayout, parent, false);
        TodoView todoView = new TodoView(view);
        return todoView;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoView holder, int position) {
        Todo todo = todoList.get(position);
        holder.tvTitle.setText(todo.getTitle());
        holder.tvDescription.setText(todo.getDescription());
        holder.tvComplete.setText(todo.isCompleted() ? "Completed" : "Incomplete");

        int priority = todo.getPriority();
        if (priority == 0)
            holder.tvPriority.setText(R.string.todo_high);
        else if (priority == 1)
            holder.tvPriority.setText(R.string.todo_medium);
        else
            holder.tvPriority.setText(R.string.todo_low);

        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        holder.tvDate.setText(dateFormat.format(todo.getTodoDate()));
    }

    public void removeTodoAt(int position) {
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return todoList != null ? todoList.size() : 0;
    }

    public class TodoView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle, tvDescription, tvComplete, tvDate, tvPriority;

        public TodoView(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.todo_item_tv_title);
            tvDescription = itemView.findViewById(R.id.todo_item_tv_description);
            tvComplete = itemView.findViewById(R.id.todo_item_tv_complete);
            tvDate = itemView.findViewById(R.id.todo_item_tv_date);
            tvPriority = itemView.findViewById(R.id.todo_item_tv_prority);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onTodoClickListener.onItemClick(position);
            }
        }
    }

    public interface OnTodoClickListener {
        void onItemClick(int position);
    }
}
