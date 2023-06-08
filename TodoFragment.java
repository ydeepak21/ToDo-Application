package com.example.final_todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.CategoryViewModel;
import com.example.final_todo.viewmodel.TodoViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoFragment extends Fragment {

    Spinner categoryDropDownList;
    EditText txtTodoDate, txtTitle, txtDescription;
    RadioGroup rgPriority;
    CheckBox chkComlete;
    Button btnSave;
    CategoryViewModel categoryViewModel;
    RadioButton high, low, medium;
    TodoViewModel todoViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        todoViewModel =  new ViewModelProvider(getActivity()).get(TodoViewModel.class);
        categoryDropDownList = view.findViewById(R.id.fragment_todo_cbo_category);
        txtTodoDate = view.findViewById(R.id.fragment_todo_txt_date);
        txtTitle = view.findViewById(R.id.fragment_todo_txt_title);
        txtDescription = view.findViewById(R.id.fragment_todo_txtDescription);
        rgPriority = view.findViewById(R.id.fragment_todo_rg_priority);
        high = view.findViewById(R.id.fragment_todo_rb_high);
        medium = view.findViewById(R.id.fragment_todo_rb_mid);
        low = view.findViewById(R.id.fragment_todo_rb_low);
        chkComlete = view.findViewById(R.id.fragment_todo_chk_complete);
        btnSave = view.findViewById(R.id.fragment_todo_btn_Save);

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            setCategorySpinner(categories);
        });

        txtTodoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDateDialog();
            }
        });


        Intent intent = getActivity().getIntent();
        if(intent != null){
            int todoId = intent.getIntExtra("todoId", -1);
            if(todoId != -1){
                intent.removeExtra("todoId");
                todoViewModel.loadTodoById(todoId).observe(getActivity(), todo ->{
                    if (todo.getCategoryId() == null) {
                        btnSave.setEnabled(false);
                        Toast.makeText(getActivity(), "No category found for this task", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity2  mainActivity2 = (MainActivity2) getActivity();
                                mainActivity2.todoListPage();
                            }
                        }, 2000);
                        return;
                    }
                    btnSave.setText("Update");
                    txtTitle.setText(todo.getTitle());
                    txtDescription.setText(todo.getDescription());
                    Date date = todo.getTodoDate();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String dateString = format.format(date);
                    txtTodoDate.setText(dateString);
                    // Fetch the category list from the CategoryViewModel
                    categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
                        setCategorySpinner(categories);
                        // Find the position of the category with matching ID
                        Integer categoryId = todo.getCategoryId();
                        int categoryPosition = -1;
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getCategoryId() == categoryId) {
                                categoryPosition = i;
                                break;
                            }
                        }
                        // Set the selected category based on position
                        if (categoryPosition != -1) {
                            categoryDropDownList.setSelection(categoryPosition);
                        }
                    });
                    if(todo.isCompleted()) {
                        chkComlete.setChecked(true);
                    }
                    int priority = todo.getPriority();
                    switch (priority){
                        case 0:
                            high.setChecked(true);
                            break;
                        case 1:
                            medium.setChecked(true);
                            break;
                        case 2:
                            low.setChecked(true);
                            break;
                    }
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!validateFields()) {
                                return; // Exit the click listener if validation fails
                            }

                            todo.setTitle(txtTitle.getText().toString());
                            todo.setDescription(txtDescription.getText().toString());
                            todo.setPriority(priority);
                            todo.setCompleted(chkComlete.isChecked());
                            // Parse the updated date
                            String updatedDateStr = txtTodoDate.getText().toString();
                            Date updatedDate;
                            try {
                                updatedDate = format.parse(updatedDateStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Invalid date format", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            todo.setTodoDate(updatedDate);

                            Category selectedCategory = (Category) categoryDropDownList.getSelectedItem();
                            if (selectedCategory != null) {
                                todo.setCategoryId(selectedCategory.getCategoryId());
                            }
                            todoViewModel.updateTodo(todo);
                            Toast.makeText(getActivity(), "Todo Updated", Toast.LENGTH_SHORT).show();
                            // ((MainActivity2) getActivity()).addTaskPage();
                        }
                    });
                });
            }
            else {
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!validateFields()) {
                            return; // Exit the click listener if validation fails
                        }
                        saveData();
                    }
                });
            }
        }
        return  view;
    }
    private void setCategorySpinner(List<Category> categories) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories) ;
        categoryDropDownList.setAdapter(adapter);
    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtTodoDate.setText(dayOfMonth + "/"+ (month+1)+"/" +year);
            }
        }, year, month, day
        );
        datePickerDialog.show();
    }

    private void saveData(){
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        String todoDate = txtTodoDate.getText().toString();
        Category category = (Category) categoryDropDownList.getSelectedItem();

        int checkedRadio = rgPriority.getCheckedRadioButtonId();
        int priority =0;
        switch (checkedRadio){
            case R.id.fragment_todo_rb_high:
                priority =0;
                break;
            case R.id.fragment_todo_rb_mid:
                priority = 1;
                break;
            case R.id.fragment_todo_rb_low:
                priority = 2;
                break;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date todoDateOn = null;
        try {
            todoDateOn = formatter.parse(todoDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse exception here
            Toast.makeText(getActivity(), "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }
        Date createdOn = new Date();
        boolean isComplete = chkComlete.isChecked();
        Todo todo = new Todo(title, description, todoDateOn, isComplete, priority, category.getCategoryId(), createdOn);

        todoViewModel.saveTodo(todo);
        Toast.makeText(getActivity(), "Todo Saved", Toast.LENGTH_SHORT).show();

// Navigate to TodoListFragment
        int categoryId = category.getCategoryId();
        TodoListFragment todoListFragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        todoListFragment.setArguments(args);

        // Replace the current fragment with TodoListFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.activity_todo_fragment, todoListFragment)
                .commit();
    }
    private boolean validateFields() {
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        String todoDate = txtTodoDate.getText().toString();
        Category category = (Category) categoryDropDownList.getSelectedItem();

        // Validate title field
        if (title.isEmpty()) {
            txtTitle.setError("Title is required");
            return false;
        }

        // Validate description field
        if (description.isEmpty()) {
            txtDescription.setError("Description is required");
            return false;
        }

        // Validate todo date field
        if (todoDate.isEmpty()) {
            txtTodoDate.setError("Todo date is required");
            return false;
        }

        // Validate radio buttons
        if (rgPriority.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please select a radiobutton", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}