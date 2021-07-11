package com.example.schroll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FundamentalCoursesAdapter extends RecyclerView.Adapter<FundamentalCoursesAdapter.fundamentalViewHolder> {

    private ArrayList<FundamentalCoursesHelper> fundamentalLocations;
    private onCourseListener mOnCourseListener;

    public FundamentalCoursesAdapter(ArrayList<FundamentalCoursesHelper> fundamentalLocations, onCourseListener oncourseListener) {
        this.fundamentalLocations = fundamentalLocations;
        this.mOnCourseListener = oncourseListener;
    }

    @NonNull
    @Override
    public fundamentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fundamental_courses_card_design,parent,false);
       fundamentalViewHolder fundamentalViewHolder = new fundamentalViewHolder(view, mOnCourseListener);
        return fundamentalViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull fundamentalViewHolder holder, int position) {
        FundamentalCoursesHelper fundamentalCoursesHelper = fundamentalLocations.get(position);
        holder.courseImage.setImageResource(fundamentalCoursesHelper.getCourseImage());
        holder.courseTitle.setText(fundamentalCoursesHelper.getCourseTitle());
        holder.courseDesc.setText(fundamentalCoursesHelper.getCourseDesc());
    }

    @Override
    public int getItemCount() {
        return fundamentalLocations.size();
    }


    public static class fundamentalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView courseImage;
        TextView courseTitle, courseDesc;
        onCourseListener mOncourseListener;
        public fundamentalViewHolder(@NonNull View itemView, onCourseListener oncourseListener) {
            super(itemView);
            //Hooks of the recycler view
            courseImage = itemView.findViewById(R.id.courseImage01);
            courseTitle = itemView.findViewById(R.id.courseTitle01);
            courseDesc = itemView.findViewById(R.id.courseDesc01);

            mOncourseListener = oncourseListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOncourseListener.onCourseClick(getAdapterPosition());
        }
    }

    public interface onCourseListener{
        void onCourseClick(int position);
    }

}
