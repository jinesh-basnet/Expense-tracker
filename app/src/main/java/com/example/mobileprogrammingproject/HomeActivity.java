package com.example.mobileprogrammingproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottomNav);
        loadFragment(new DashboardFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();
            if (id == R.id.nav_home) selected = new DashboardFragment();
            else if (id == R.id.nav_transactions) selected = new TransactionFragment();

            if (selected != null) {
                loadFragment(selected);
                return true;
            }
            return false;
        });

        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void switchToTransactionFragment() {
        bottomNav.setSelectedItemId(R.id.nav_transactions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            getSharedPreferences("user_prefs", MODE_PRIVATE).edit().remove("user_id").apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (id == R.id.action_refresh) {
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (current instanceof DashboardFragment) loadFragment(new DashboardFragment());
            else if (current instanceof TransactionFragment) loadFragment(new TransactionFragment());
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(this, "Expense Tracker v1.0\nDeveloped by Jinesh Basnet", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_statistics) {
            startActivity(new Intent(this, StatisticsActivity.class));
            return true;
        } else if (id == R.id.action_currency) {
            startActivity(new Intent(this, CurrencyConverterActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
