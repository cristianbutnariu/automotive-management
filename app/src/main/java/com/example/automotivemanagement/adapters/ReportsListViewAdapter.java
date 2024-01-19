package com.example.automotivemanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.automotivemanagement.R;
import com.example.automotivemanagement.expenses.Expenses;
import com.example.automotivemanagement.expenses.ExpensesDB;
import com.example.automotivemanagement.fragments.ReportsFragment;
import com.example.automotivemanagement.reports.FuelReport;
import com.example.automotivemanagement.reports.OtherReport;
import com.example.automotivemanagement.reports.ServiceReport;
import com.example.automotivemanagement.reports.TaxesReport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReportsListViewAdapter extends ArrayAdapter<Expenses> {
    final int idSelected = ReportsFragment.idSelected;
    Context context;
    Intent intent;
    private List<Expenses> expensesList;

    public ReportsListViewAdapter(List<Expenses> list, Context context) {
        super(context, R.layout.reportslistviewlayout, list);
        this.expensesList = list;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Expenses expenses = getItem(position);
        ViewHolder viewHolder;

        ExpensesDB edb = ExpensesDB.getInstance(getContext());

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.reportslistviewlayout, parent, false);
            viewHolder.value = convertView.findViewById(R.id.detailsValue);
            viewHolder.mileage = convertView.findViewById(R.id.reportsMileage);
            viewHolder.date = convertView.findViewById(R.id.reportsDate);
            viewHolder.details = convertView.findViewById(R.id.reportsDetails);
            viewHolder.viewPhoto = convertView.findViewById(R.id.viewImageButton);
            viewHolder.deleteRecord = convertView.findViewById(R.id.deleteImageButton);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(expenses.getDate());

        viewHolder.value.setText(String.valueOf(expenses.getValue() + " RON"));
        viewHolder.mileage.setText(String.valueOf(expenses.getMileage() + " km"));
        viewHolder.date.setText(formattedDate);
        viewHolder.details.setText(expenses.getDetails());

        viewHolder.deleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edb.getExpensesDao().deleteExpense(expenses.getType(), expenses.getValue(),
                        expenses.getMileage(), expenses.getDetails(), expenses.getDate(),
                        expenses.getTime(), expenses.getCarId());
                switch (idSelected) {
                    case 1:
                        intent = new Intent(getContext(), FuelReport.class);
                        break;
                    case 2:
                        intent = new Intent(getContext(), ServiceReport.class);
                        break;
                    case 3:
                        intent = new Intent(getContext(), TaxesReport.class);
                        break;
                    case 4:
                        intent = new Intent(getContext(), OtherReport.class);
                        break;
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(intent);

            }
        });

        viewHolder.viewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpensesDB database = ExpensesDB.getInstance(getContext());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String path = database.getExpensesDao().getExpensePhotoPath(expenses.getType(),
                        expenses.getValue(), expenses.getMileage(), expenses.getDetails(),
                        expenses.getDate(), expenses.getTime(), expenses.getCarId());
                if (path.isEmpty()) {
                    Toast.makeText(getContext(), "There is no photo uploaded for this expense", Toast.LENGTH_LONG).show();
                } else {
                    File file = new File(path);
                    Uri dirUri = FileProvider.getUriForFile(getContext(), "com.example.automotivemanagement.provider", file);
                    intent.setData(dirUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            }
        });

        return result;


    }

    private static class ViewHolder {
        TextView value;
        TextView mileage;
        TextView date;
        TextView details;
        ImageButton viewPhoto;
        ImageButton deleteRecord;
        ImageView imgView;
    }
}
