package com.example.automotivemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.automotivemanagement.R;
import com.example.automotivemanagement.expenses.Expenses;

import java.util.List;

public class EmptyListViewAdapter extends ArrayAdapter<Expenses> {
    Context context;
    private List<Expenses> expensesList;

    public EmptyListViewAdapter(List<Expenses> list, Context context) {
        super(context, R.layout.emptylistviewlayout, list);
        this.expensesList = list;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Expenses expenses = getItem(0);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.emptylistviewlayout, parent, false);

            viewHolder.details = convertView.findViewById(R.id.emptyDetailsValue);
            viewHolder.details2=convertView.findViewById(R.id.emptyDetailsValue2);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.details.setText(expenses.getDetails());

        return convertView;
    }

    private static class ViewHolder {
        TextView details;
        TextView details2;
    }
}
