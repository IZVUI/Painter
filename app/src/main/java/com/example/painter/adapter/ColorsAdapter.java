package com.example.painter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.painter.MyColor;
import com.example.painter.R;

import java.util.List;

public class ColorsAdapter
        extends RecyclerView.Adapter<ColorsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<MyColor> colors;

    public ColorsAdapter(Context context, List<MyColor> colors) {
        this.colors = colors;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ColorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_color, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ColorsAdapter.ViewHolder holder, int position) {
        MyColor color = colors.get(position);
        holder.color_name.setText(color.getName());
        holder.color.setBackgroundColor(color.getColor());
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView color;
        final TextView color_name;
        ViewHolder(View view){
            super(view);
            color = (TextView) view.findViewById(R.id.color);
            color_name = (TextView) view.findViewById(R.id.color_name);
        }
    }
}
