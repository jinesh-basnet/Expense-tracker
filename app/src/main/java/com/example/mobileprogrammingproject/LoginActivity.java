package com.example.mobileprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHelper db = new DatabaseHelper(this);
        EditText etE = findViewById(R.id.etLoginEmail), etP = findViewById(R.id.etLoginPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String e = etE.getText().toString().trim(), p = etP.getText().toString().trim();
            
            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                Toast.makeText(this, "Enter a valid email (e.g. user@gmail.com)", Toast.LENGTH_SHORT).show();
            } else {
                int uid = db.checkUser(e, p);
                if (uid != -1) {
                    getSharedPreferences("user_prefs", MODE_PRIVATE).edit().putInt("user_id", uid).apply();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                } else Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tvToRegister).setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
