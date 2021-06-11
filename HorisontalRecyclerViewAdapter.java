package com.example.niceweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class HorisontalRecyclerViewAdapter extends RecyclerView.Adapter<HorisontalRecyclerViewAdapter.ViewHolder> {

    Context context;
    String[] time;
    String[] weather;
    String[] temp;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView timeName;
        TextView weatherName;
        TextView tempName;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            timeName = itemView.findViewById(R.id.textView1);
            weatherName = itemView.findViewById(R.id.textView2);
            tempName = itemView.findViewById(R.id.textView3);
        }
    }

    public HorisontalRecyclerViewAdapter(Context context, String[] time, String[] weather, String[] temp){
        this.context = context;
        this.time = time;
        this.weather = weather;
        this.temp = temp;
    }


    @NonNull
    @Override
    public HorisontalRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.secondrecycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HorisontalRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.timeName.setText(time[position]);
        holder.weatherName.setText(weather[position]);
        holder.tempName.setText(temp[position]);
    }

    @Override
    public int getItemCount() {
        return time.length;
    }
}