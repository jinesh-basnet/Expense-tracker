package com.example.mobileprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {

    private TransactionAdapter adapter;
    private List<Transaction> list = new ArrayList<>();
    private DatabaseHelper db;

    public TransactionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        db = new DatabaseHelper(requireContext());
        
        RecyclerView rv = view.findViewById(R.id.recyclerViewFragTransactions);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TransactionAdapter(list);
        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView r, RecyclerView.ViewHolder v, RecyclerView.ViewHolder t) { return false; }

            @Override
            public void onSwiped(RecyclerView.ViewHolder v, int d) {
                int pos = v.getAdapterPosition();
                TransactionDeleteDialogFragment dialog = new TransactionDeleteDialogFragment();
                dialog.setListener(new TransactionDeleteDialogFragment.ConfirmationListener() {
                    @Override
                    public void onConfirmDelete() {
                        db.deleteTransaction(list.get(pos).getId());
                        list.remove(pos);
                        adapter.notifyItemRemoved(pos);
                    }
                    @Override
                    public void onCancel() { adapter.notifyItemChanged(pos); }
                });
                dialog.show(getParentFragmentManager(), "confirm");
            }
        }).attachToRecyclerView(rv);

        view.findViewById(R.id.fabFragAddTransaction).setOnClickListener(v -> startActivity(new Intent(getActivity(), AddTransactionActivity.class)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int userId = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getInt("user_id", -1);
        if (userId != -1) {
            list.clear();
            list.addAll(db.getAllTransactions(userId));
            adapter.notifyDataSetChanged();
        }
    }
}
