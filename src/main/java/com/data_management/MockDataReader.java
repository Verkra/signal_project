package com.data_management;

public class MockDataReader implements DataReader {
    private boolean isReading;

    @Override
    public void startReading(DataStorage dataStorage) {
        if (!isReading) {
            isReading = true;
            // Add predefined patient data to the dataStorage
            dataStorage.addPatientData(1, 75.0, "HeartRate", 1600000000000L);
            dataStorage.addPatientData(1, 120.0, "BloodPressure", 1600000001000L);
            // Add more patient data as needed
        }
    }

    @Override
    public void stopReading() {
        isReading = false;
    }
}