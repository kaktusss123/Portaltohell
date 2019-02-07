package com.komarnitskij.portaltohell;

class DataTransfer {
    private static final DataTransfer ourInstance = new DataTransfer();

    WebInterface web;

    static DataTransfer getInstance() {
        return ourInstance;
    }

    private DataTransfer() {
    }
}
