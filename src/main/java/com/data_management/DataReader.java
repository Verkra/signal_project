package com.data_management;

public interface DataReader {
    void startReading(DataStorage dataStorage);
    void stopReading();
}