package com.example.schroll;

public class Students {
    private String Surname;
    private String studentID;

    public Students(String surname, String studentID) {
        this.Surname = surname;
        this.studentID = studentID;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    @Override
    public String toString() {
        return Surname;
    }
}
