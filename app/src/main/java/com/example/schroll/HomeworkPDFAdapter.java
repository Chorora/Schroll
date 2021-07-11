package com.example.schroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeworkPDFAdapter extends RecyclerView.Adapter<HomeworkPDFAdapter.viewHolder> {

    Context context;
    ArrayList<HomeworkPDFModel> pdfArrayList;
    public OnItemClickListener onItemClickListener;

    public HomeworkPDFAdapter(Context context, ArrayList<HomeworkPDFModel> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdflist, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int i) {
        holder.title.setText(pdfArrayList.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        public viewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            image.setImageResource(R.drawable.pdf);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}