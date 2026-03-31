package com.example.mobileprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.List;

public class DashboardFragment extends Fragment {

    private TextView tvUserName, tvHomeBalance, tvIncome, tvExpense;
    private DatabaseHelper dbHelper;

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dbHelper = new DatabaseHelper(requireContext());

        tvUserName = view.findViewById(R.id.tvFragUserName);
        tvHomeBalance = view.findViewById(R.id.tvFragHomeBalance);
        tvIncome = view.findViewById(R.id.tvFragTotalIncome);
        tvExpense = view.findViewById(R.id.tvFragTotalExpense);

        view.findViewById(R.id.cardFragAdd).setOnClickListener(v -> startActivity(new Intent(getActivity(), AddTransactionActivity.class)));
        view.findViewById(R.id.cardFragHistory).setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity) ((HomeActivity) getActivity()).switchToTransactionFragment();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        int userId = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) return;

        tvUserName.setText("Hi, " + dbHelper.getUserName(userId) + "!");
        List<Transaction> list = dbHelper.getAllTransactions(userId);
        double total = 0, inc = 0, exp = 0;
        
        for (Transaction t : list) {
            try {
                double a = Double.parseDouble(t.getAmount());
                if (t.isExpense()) { total -= a; exp += a; }
                else { total += a; inc += a; }
            } catch (Exception ignored) {}
        }
        
        tvHomeBalance.setText(String.format("NRS %.2f", total));
        tvIncome.setText(String.format("+NRS %.2f", inc));
        tvExpense.setText(String.format("-NRS %.2f", exp));
    }
}
