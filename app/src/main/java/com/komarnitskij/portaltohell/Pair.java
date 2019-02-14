package com.komarnitskij.portaltohell;

import java.util.ArrayList;

class Pair {
    String name;
    String startTime;
    String endTime;
    String type;
    String date;
    ArrayList<String> classrooms, prepodName;

    Pair(String name, String startTime, String endTime, String type, ArrayList<String> classrooms, ArrayList<String> prepodName, String date) {
        this.classrooms = classrooms;
        this.endTime = endTime;
        this.name = name;
        this.prepodName = prepodName;
        this.startTime = startTime;
        this.type = type;
        this.date = date;
    }
}
