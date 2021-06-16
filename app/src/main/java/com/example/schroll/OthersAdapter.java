package com.example.schroll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.OthersViewHolder> {

    private ArrayList<OthersHelperClass> othersLocations;
    private onCoursesListener mOnCoursesListener;
    public OthersAdapter(ArrayList<OthersHelperClass> othersLocations, onCoursesListener oncoursesListener) {
        this.othersLocations = othersLocations;
        this.mOnCoursesListener = oncoursesListener;
    }

    @NonNull
    @Override
    public OthersAdapter.OthersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.others_card_design,parent,false);
        OthersAdapter.OthersViewHolder othersViewHolder = new OthersAdapter.OthersViewHolder(view, mOnCoursesListener);
        return othersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OthersAdapter.OthersViewHolder holder, int position) {
        OthersHelperClass othersHelperClass = othersLocations.get(position);
        holder.courseImage.setImageResource(othersHelperClass.getCourseImage());
        holder.courseTitle.setText(othersHelperClass.getCourseTitle());
        holder.courseDesc.setText(othersHelperClass.getCourseDesc());
    }

    @Override
    public int getItemCount() {
        return othersLocations.size();
    }


    public static class OthersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView courseImage;
        TextView courseTitle, courseDesc;
        onCoursesListener mOncoursesListener;

        public OthersViewHolder(@NonNull View itemView, onCoursesListener oncoursesListener) {
            super(itemView);

            //Hooks
            courseImage = itemView.findViewById(R.id.courseImage02);
            courseTitle = itemView.findViewById(R.id.courseTitle02);
            courseDesc = itemView.findViewById(R.id.courseDesc02);

            mOncoursesListener = oncoursesListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOncoursesListener.onCoursesClick(getAdapterPosition());
        }
    }

    public interface onCoursesListener{
        void onCoursesClick(int position);
    }
}
