package com.komarnitskij.portaltohell;

class Pair {
    String name, startTime, endTime, type, classroom, prepodName;

    Pair(String name, String startTime, String endTime, String type, String classroom, String prepodName) {
        this.classroom = classroom;
        this.endTime = endTime;
        this.name = name;
        this.prepodName = prepodName;
        this.startTime = startTime;
        this.type = type;
    }

    Pair(String name) {
        this.name = name;
        this.classroom = "";
        this.endTime = "";
        this.prepodName = "";
        this.startTime = "";
        this.type = "";
    }
}
