package com.example.schroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class courseAdapter extends RecyclerView.Adapter<courseAdapter.viewHolder> {

    Context context;
    ArrayList<courseModel> courseArrayList;
    public courseAdapter.OnItemClickListener onItemClickListener;

    public courseAdapter (Context context, ArrayList<com.example.schroll.courseModel> courseArrayList) {
        this.context = context;
        this.courseArrayList = courseArrayList;
    }

    @Override
    public courseAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdflist , viewGroup, false);
        return new courseAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final courseAdapter.viewHolder holder, final int i) {
        holder.title.setText(courseArrayList.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return courseArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image= (ImageView) itemView.findViewById(R.id.image);
            image.setImageResource(R.drawable.pdf);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(courseAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}
