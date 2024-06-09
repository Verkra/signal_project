package com.data_management;

public class MockDataReader implements DataReader {
    @Override
    public void readData(DataStorage dataStorage) {
        // Add predefined patient data to the dataStorage
        dataStorage.addPatientData(1, 75.0, "HeartRate", 1600000000000L);
        // Add more patient data as needed
    }
}