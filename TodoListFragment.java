package com.example.final_todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.final_todo.R;
import com.example.final_todo.adaptor.TodoAdaptor;
import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.CategoryViewModel;
import com.example.final_todo.viewmodel.TodoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TodoListFragment extends Fragment implements  TodoAdaptor.OnTodoClickListener{

    private TodoViewModel todoViewModel;
    private RecyclerView todoRecyclerView;
    private TodoAdaptor todoAdaptor;
    private Repository repository;
    private CategoryViewModel categoryViewModel;
    private MainActivity2 mainActivity2;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_todo_list, container, false);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        todoRecyclerView = view.findViewById(R.id.activity_todo_list_rv_todo);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        todoAdaptor = new TodoAdaptor(this::onItemClick);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        todoRecyclerView.setAdapter(todoAdaptor);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext());
        repository = new Repository(appDatabase);

        // ItemTouchHelper for swipe and drag events
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Not needed for drag events
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Todo todo = todoAdaptor.todoList.get(position); // Access todo directly

                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        // Handle swipe right (delete)
                        repository.deleteTodo(todo);
                        Toast.makeText(requireContext(), "Todo deleted", Toast.LENGTH_SHORT).show();
                        todoAdaptor.removeTodoAt(position); // Remove the item from the adapter immediately
                        break;
                    case ItemTouchHelper.LEFT:
                        // Handle swipe left (complete)
                        todo.setCompleted(true);
                        repository.completeTask(todo.getTodoId());
                        Toast.makeText(requireContext(), "Todo Status Updated.", Toast.LENGTH_SHORT).show();
                        todoAdaptor.notifyItemChanged(position); // Notify the adapter that the item at the position has changed
                        break;
                }
            }
        });
        Intent intent = getActivity().getIntent();
        int fk_category_id = intent.getIntExtra("categoryId", -1);
        todoViewModel.loadByCategoryId(fk_category_id).observe(getActivity(), (Observer<List<Todo>>) todos -> {
            categoryViewModel.loadCategoryNameById(fk_category_id).observe(getActivity(), (Observer<Category>) catname -> {
                if (catname != null) {
                    todoAdaptor.setTodoList(todos);
                    todoRecyclerView = view.findViewById(R.id.activity_todo_list_rv_todo);
                    todoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    todoRecyclerView.setAdapter(todoAdaptor);
                } else {
                    todoViewModel.getTodoList().observe(getActivity(),
                            todos2 -> {
                                todoAdaptor.setTodoList(todos2);
                                todoRecyclerView = view.findViewById(R.id.activity_todo_list_rv_todo);
                                todoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                todoRecyclerView.setAdapter(todoAdaptor);
                            });
                }
            });
        });

        getActivity().getIntent().putExtra("categoryId", -1);

        FloatingActionButton fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity2) getActivity()).addTaskPage();
            }
        });


        itemTouchHelper.attachToRecyclerView(todoRecyclerView);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Todo todo = todoAdaptor.todoList.get(position);
        Intent intent = new Intent(getActivity(), MainActivity2.class);
        intent.putExtra("todoId", todo.getTodoId());
        startActivity(intent);
    }
}
