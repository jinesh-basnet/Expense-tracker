package com.example.mobileprogrammingproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

public class CategoryGridAdapter extends BaseAdapter {

    private final Context context;
    private final String[][] data; 
    private final boolean isExpense;

    private static final Map<String, String> CATEGORY_EMOJIS = new HashMap<>();
    static {
        CATEGORY_EMOJIS.put("Food", "🍔");
        CATEGORY_EMOJIS.put("Shopping", "🛍️");
        CATEGORY_EMOJIS.put("Transport", "🚗");
        CATEGORY_EMOJIS.put("Bills", "📄");
        CATEGORY_EMOJIS.put("Entertainment", "🎬");
        CATEGORY_EMOJIS.put("Healthcare", "🏥");
        CATEGORY_EMOJIS.put("Education", "📚");
        CATEGORY_EMOJIS.put("Debt Payment", "💳");
        CATEGORY_EMOJIS.put("Salary", "💰");
        CATEGORY_EMOJIS.put("Bonus", "🎉");
        CATEGORY_EMOJIS.put("Pocket Money", "👛");
        CATEGORY_EMOJIS.put("Investment", "📈");
        CATEGORY_EMOJIS.put("Business", "💼");
        CATEGORY_EMOJIS.put("Gift", "🎁");
        CATEGORY_EMOJIS.put("Part-time", "⏰");
        CATEGORY_EMOJIS.put("Refund", "💵");
        CATEGORY_EMOJIS.put("Other", "📌");
    }

    public CategoryGridAdapter(Context context, String[][] data, boolean isExpense) {
        this.context = context;
        this.data = data;
        this.isExpense = isExpense;
    }

    @Override
    public int getCount() { return data.length; }

    @Override
    public Object getItem(int position) { return data[position]; }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category_grid, parent, false);
        }

        String[] item = data[position];
        String category = item[0];
        String total = item[1];

        TextView tvEmoji = convertView.findViewById(R.id.tvGridEmoji);
        TextView tvCategory = convertView.findViewById(R.id.tvGridCategory);
        TextView tvAmount = convertView.findViewById(R.id.tvGridAmount);

        tvEmoji.setText(CATEGORY_EMOJIS.getOrDefault(category, "📌"));
        tvCategory.setText(category);

        double amount = Double.parseDouble(total);
        if (amount > 0) {
            tvAmount.setText(String.format("NRS %.0f", amount));
            tvAmount.setTextColor(context.getResources().getColor(isExpense ? R.color.expense_red : R.color.income_green, null));
            convertView.setBackgroundResource(R.drawable.category_grid_item_selected);
        } else {
            tvAmount.setText("NRS 0");
            tvAmount.setTextColor(context.getResources().getColor(R.color.text_muted, null));
            convertView.setBackgroundResource(R.drawable.category_grid_item_bg);
        }

        return convertView;
    }
}
