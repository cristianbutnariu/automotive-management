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
import com.example.automotivemanagement.shop.Dealership;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Dealership> {

    private List<Dealership> dealershipList;
    Context context;

    private static class ViewHolder {
        TextView dealerName;
        TextView dealerAdrress;
        TextView dealerSchedule;
        TextView dealerNumber;
    }

    public ListViewAdapter(List<Dealership> list, Context context) {
        super(context, R.layout.listviewlayout, list);
        this.dealershipList = list;
        this.context=context;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Dealership dealership = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView==null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listviewlayout, parent, false);
            viewHolder.dealerName=convertView.findViewById(R.id.dealerName);
            viewHolder.dealerAdrress=convertView.findViewById(R.id.dealerAddress);
            viewHolder.dealerSchedule=convertView.findViewById(R.id.dealerSchedule);
            viewHolder.dealerNumber=convertView.findViewById(R.id.dealerNumber);

            result=convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.dealerName.setText(dealership.getName());
        viewHolder.dealerAdrress.setText(dealership.getAddress());
        viewHolder.dealerSchedule.setText(dealership.getDescription());
        viewHolder.dealerNumber.setText(dealership.getPhoneNumber());

        return convertView;
    }
}
