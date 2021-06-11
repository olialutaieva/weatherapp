package com.example.niceweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    int[] night;
    String[] days;
    int[] day;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowName;
        TextView rowDescription;
        ImageView rowImage;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            rowName = itemView.findViewById(R.id.textView2);
            rowDescription = itemView.findViewById(R.id.textView1);
//            rowImage = itemView.findViewById(R.id.imageView);
        }
    }

    public RecyclerViewAdapter(Context context, String[] days, int[] night, int[] day){
        this.context = context;
        this.days = days;
        this.night = night;
        this.day = day;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
    holder.rowName.setText(night[position] + "°C / " + day[position] + "°C");
    holder.rowDescription.setText(days[position]);
    }

    @Override
    public int getItemCount() {
        return days.length;
    }
}