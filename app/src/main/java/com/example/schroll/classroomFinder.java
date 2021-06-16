package com.example.schroll;


public class classroomFinder {
    private String Year;
    private String Classroom;

    public classroomFinder() {
        // public no-arg constructor needed
    }

    public classroomFinder(String Year, String Classroom){
        this.Year = Year;
        this.Classroom = Classroom;
    }

    public String getYear(){
        return Year;
    }

    public String getClassroom() {
        return Classroom;
    }


}
