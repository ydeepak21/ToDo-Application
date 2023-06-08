package com.example.final_todo;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.final_todo.adaptor.TodoAdaptor;
import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.database.Repository;
import com.example.final_todo.model.Todo;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private Repository repository;
    private RecyclerView todoRecyclerView;
    private TodoAdaptor todoAdaptor;
    private int TodoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Initialize repository instance using the appDatabase
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        repository = new Repository(appDatabase);
        Intent intent = getIntent();
        if(intent != null){
            TodoId = intent.getIntExtra("todoId", -1);
            if(TodoId != -1){
                addTaskPage();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_menu, menu);
        return true;
    }
    public void addTaskPage(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_todo_fragment, TodoFragment.class, null)
                .addToBackStack("Add Task")
                .setReorderingAllowed(true)
                .commit();
    }
    public void todoListPage(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_todo_fragment, TodoListFragment.class, null)
                .addToBackStack("Todo Details")
                .setReorderingAllowed(true)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_todo:
                // Handle the menu item click
                addTaskPage();
                return true;
            case R.id.delete_todo:
                // Handle the menu item click
                repository.deleteAllTodo();
                showToast("All todos are deleted");
                updateTodoListDelayed();
                return true;
            case R.id.delete_complete_todo:
                // Handle the menu item click
                repository.deleteAllCompletedTodo();
                showToast("All completed todos are deleted");
                updateTodoListDelayed();
                return true;
            case R.id.Category_Todo:
                Intent intent1 = new Intent(this ,MainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.user_logout:
                Intent intent2 = new Intent(this ,FirstActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateTodoListDelayed() {
        new Handler().postDelayed(() -> {
            todoRecyclerView = findViewById(R.id.activity_todo_list_rv_todo);
            todoAdaptor = new TodoAdaptor((TodoAdaptor.OnTodoClickListener) this);
            repository.loadAllTodo().observe(this, new Observer<List<Todo>>() {
                @Override
                public void onChanged(List<Todo> todos) {
                    todoAdaptor.setTodoList(todos);
                }
            });
            todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            todoRecyclerView.setAdapter(todoAdaptor);

        }, 1000);
    }

}