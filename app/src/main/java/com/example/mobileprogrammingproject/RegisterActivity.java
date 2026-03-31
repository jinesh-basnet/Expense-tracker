package com.example.mobileprogrammingproject;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DatabaseHelper db = new DatabaseHelper(this);
        EditText etN = findViewById(R.id.etRegName), etE = findViewById(R.id.etRegEmail), etP = findViewById(R.id.etRegPassword);

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            String n = etN.getText().toString().trim(), e = etE.getText().toString().trim(), p = etP.getText().toString().trim();
            
            if (n.isEmpty() || e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Empty fields!", Toast.LENGTH_SHORT).show();
            } else if (n.length() < 3) {
                Toast.makeText(this, "Name too short!", Toast.LENGTH_SHORT).show();
            } else if (n.matches(".*\\d.*")) {
                Toast.makeText(this, "Name cannot contain numbers!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show();
            } else if (p.length() < 6) {
                Toast.makeText(this, "Password must be 6+ chars!", Toast.LENGTH_SHORT).show();
            } else {
                if (db.insertUser(n, e, p) > 0) {
                    Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
                    finish();
                } else Toast.makeText(this, "Email already registered!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tvToLogin).setOnClickListener(v -> finish());
    }
}
