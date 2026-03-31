package com.example.mobileprogrammingproject.labs.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileprogrammingproject.R;

import java.util.ArrayList;
import java.util.List;

public class Lab1GridViewActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1_grid_view);

        gridView = findViewById(R.id.gridView);

        List<String> items = new ArrayList<>();
        items.add("Apple");
        items.add("Banana");
        items.add("Cherry");
        items.add("Date");
        items.add("Elderberry");
        items.add("Fig");

        GridAdapter adapter = new GridAdapter(items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, "Clicked: " + items.get(position), Toast.LENGTH_SHORT).show();
        });
    }

    private class GridAdapter extends BaseAdapter {
        private final List<String> data;

        public GridAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(Lab1GridViewActivity.this)
                        .inflate(R.layout.item_grid_view, parent, false);
            }

            TextView itemText = convertView.findViewById(R.id.itemText);
            itemText.setText(data.get(position));

            return convertView;
        }
    }
}
