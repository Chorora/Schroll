package com.example.schroll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OtherCoursesAdapter extends RecyclerView.Adapter<OtherCoursesAdapter.OthersViewHolder> {

    private ArrayList<OtherCoursesHelper> othersLocations;
    private onCoursesListener mOnCoursesListener;
    public OtherCoursesAdapter(ArrayList<OtherCoursesHelper> othersLocations, onCoursesListener oncoursesListener) {
        this.othersLocations = othersLocations;
        this.mOnCoursesListener = oncoursesListener;
    }

    @NonNull
    @Override
    public OtherCoursesAdapter.OthersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_courses_card_design,parent,false);
        OtherCoursesAdapter.OthersViewHolder othersViewHolder = new OtherCoursesAdapter.OthersViewHolder(view, mOnCoursesListener);
        return othersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OtherCoursesAdapter.OthersViewHolder holder, int position) {
        OtherCoursesHelper otherCoursesHelper = othersLocations.get(position);
        holder.courseImage.setImageResource(otherCoursesHelper.getCourseImage());
        holder.courseTitle.setText(otherCoursesHelper.getCourseTitle());
        holder.courseDesc.setText(otherCoursesHelper.getCourseDesc());
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
