package com.example.mobileprogrammingproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetSummaryAdapter extends BaseAdapter {

    private final Context context;
    private final List<String[]> data; 
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

    public BudgetSummaryAdapter(Context context, List<String[]> data, boolean isExpense) {
        this.context = context;
        this.data = data;
        this.isExpense = isExpense;
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public Object getItem(int position) { return data.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_budget_summary, parent, false);
        }

        String[] item = data.get(position);
        String category = item[0];
        String total = item[1];
        String count = item[2];

        TextView tvEmoji = convertView.findViewById(R.id.tvCategoryEmoji);
        TextView tvCategory = convertView.findViewById(R.id.tvBudgetCategory);
        TextView tvCount = convertView.findViewById(R.id.tvBudgetCount);
        TextView tvAmount = convertView.findViewById(R.id.tvBudgetAmount);

        tvEmoji.setText(CATEGORY_EMOJIS.getOrDefault(category, "📌"));
        tvCategory.setText(category);
        tvCount.setText(count + " transaction" + (Integer.parseInt(count) != 1 ? "s" : ""));

        double amount = Double.parseDouble(total);
        String prefix = isExpense ? "-NRS " : "+NRS ";
        tvAmount.setText(String.format("%s%.2f", prefix, amount));
        tvAmount.setTextColor(context.getResources().getColor(isExpense ? R.color.expense_red : R.color.income_green, null));

        return convertView;
    }
}
