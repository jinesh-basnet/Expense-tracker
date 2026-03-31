package com.example.mobileprogrammingproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyConverterActivity extends AppCompatActivity {

    private EditText etAmount;
    private Spinner spinnerCurrency;
    private Button btnConvert;
    private CardView cardResult;
    private TextView tvConvertedAmount, tvExchangeRate, tvStatus;
    private ProgressBar progressBar;

    private final String[] currencyNames = {"USD - US Dollar", "EUR - Euro", "GBP - British Pound", "INR - Indian Rupee", "AUD - Australian Dollar", "CAD - Canadian Dollar", "JPY - Japanese Yen", "CNY - Chinese Yuan"};
    private final String[] currencyCodes = {"USD", "EUR", "GBP", "INR", "AUD", "CAD", "JPY", "CNY"};

    private static final String API_URL = "https://open.er-api.com/v6/latest/NPR";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        Toolbar toolbar = findViewById(R.id.toolbarCurrency);
        toolbar.setNavigationOnClickListener(v -> finish());

        etAmount = findViewById(R.id.etCurrencyAmount);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        btnConvert = findViewById(R.id.btnConvert);
        cardResult = findViewById(R.id.cardResult);
        tvConvertedAmount = findViewById(R.id.tvConvertedAmount);
        tvExchangeRate = findViewById(R.id.tvExchangeRate);
        tvStatus = findViewById(R.id.tvStatus);
        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        btnConvert.setOnClickListener(v -> convertCurrency());
    }

    private void convertCurrency() {
        String amountStr = etAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        String targetCurrency = currencyCodes[spinnerCurrency.getSelectedItemPosition()];

        progressBar.setVisibility(View.VISIBLE);
        btnConvert.setEnabled(false);
        cardResult.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject rates = jsonResponse.getJSONObject("rates");
                    double rate = rates.getDouble(targetCurrency);

                    double convertedAmount = amount * rate;

                    mainHandler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnConvert.setEnabled(true);
                        cardResult.setVisibility(View.VISIBLE);
                        tvConvertedAmount.setText(String.format("%s %.2f", targetCurrency, convertedAmount));
                        tvExchangeRate.setText(String.format("Rate: 1 NRS = %s %.6f", targetCurrency, rate));
                    });
                } else {
                    mainHandler.post(() -> showError("Server error: " + responseCode));
                }
                connection.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> showError("Network error: " + e.getMessage()));
            }
        });
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        btnConvert.setEnabled(true);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
