package com.example.final_todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_todo.model.User;
import com.example.final_todo.viewmodel.registerViewModel;

public class RegisterFragment extends Fragment {
    private registerViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inlfater to read xml file
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // Initialize the ViewModel
        viewModel = new ViewModelProvider(this).get(registerViewModel.class);


        EditText name = view.findViewById(R.id.register_username);
        EditText email = view.findViewById(R.id.register_email);
        EditText passwordEditText = view.findViewById(R.id.register_password);
        EditText confirmPasswordEditText = view.findViewById(R.id.register_confirm_password);

        TextView registerLink = view.findViewById(R.id.login_link);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                FirstActivity mainActivity = (FirstActivity) getActivity();
                mainActivity.replaceFragmentLogin();
            }
        });

        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (!password.equals(confirmPassword)) {
                    // Passwords do not match
                    confirmPasswordEditText.setError("Passwords do not match");
                } else {
                    // Passwords match
                    confirmPasswordEditText.setError(null);
                }
            }
        };

        passwordEditText.addTextChangedListener(passwordTextWatcher);
        confirmPasswordEditText.addTextChangedListener(passwordTextWatcher);

        Button registerButton = view.findViewById(R.id.create_activity_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String userEmail = email.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(getActivity(), "Please enter an email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(getActivity(), "Please confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // All fields are filled and passwords match, proceed with registration
                User newUser = new User(username, userEmail, password);
                viewModel.insertUser(newUser);
                FirstActivity mainActivity = (FirstActivity) getActivity();
                mainActivity.replaceFragmentLogin();
                String message = "Username: " + username + "\nEmail: " + userEmail + "\nPassword: " + password;
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }
}

