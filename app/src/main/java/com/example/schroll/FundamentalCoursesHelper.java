package com.example.schroll;

public class FundamentalCoursesHelper {

    int  courseImage;
    String courseTitle, courseDesc;

    public FundamentalCoursesHelper(int courseImage, String courseTitle, String courseDesc) {
        this.courseImage = courseImage;
        this.courseTitle = courseTitle;
        this.courseDesc = courseDesc;
    }

    public int getCourseImage() {
        return courseImage;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseDesc() {
        return courseDesc;
    }


}
