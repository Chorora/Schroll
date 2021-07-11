package com.example.schroll;

public class putPDFdetails {
    public String name;
    public static String url;

    public putPDFdetails(){
    }

    public putPDFdetails(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public static String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

}
