package com.example.mobileprogrammingproject.labs.lab2;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileprogrammingproject.R;

public class Lab2LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private CheckBox checkTerms;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        checkTerms = findViewById(R.id.checkTerms);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> validateAndLogin());
    }

    private void validateAndLogin() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Username is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (!checkTerms.isChecked()) {
            Toast.makeText(this, "You must agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // All validations passed
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
    }
}
