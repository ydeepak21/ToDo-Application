package com.example.final_todo;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.final_todo.viewmodel.loginViewModel;

public class LoginFragment extends Fragment {
    loginViewModel loginViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginViewModel = new ViewModelProvider(getActivity()).get(loginViewModel.class);

        EditText txtUserName = view.findViewById(R.id.login_username);
        EditText txtPassword = view.findViewById(R.id.login_password);
        Button btnLogin = view.findViewById(R.id.login_btn_login);
        Button btnCancel = view.findViewById(R.id.login_btn_cancel);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtUserName.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    loginViewModel.getUserByEmail(email, password).observe(getViewLifecycleOwner(), user -> {
                        if (user != null) {
                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loginToken", "Loggedin");
                            editor.apply();
                            Intent intent = new Intent(requireActivity(), MainActivity2.class);
                            startActivity(intent);
                        } else {
                            // Invalid email or password
                            Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // Empty email or password
                    Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_LONG).show();
                }
            }
        });


        TextView registerLink = view.findViewById(R.id.register_link);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                FirstActivity mainActivity = (FirstActivity) getActivity();
                mainActivity.replaceFragmentRegister();
            }
        });


        return view;
    }
}
