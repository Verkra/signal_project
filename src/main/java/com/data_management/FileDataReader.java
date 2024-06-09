package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class FileDataReader implements DataReader {
    private static final Logger logger = Logger.getLogger(FileDataReader.class.getName());
    private String filePath;

    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void startReading(DataStorage dataStorage) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int patientId = Integer.parseInt(parts[0]);
                long timestamp = Long.parseLong(parts[1]);
                String recordType = parts[2];
                double measurementValue = Double.parseDouble(parts[3]);

                if (patientId <= 0 || timestamp <= 0) {
                    logger.warning("Invalid file data: patientId=" + patientId + ", timestamp=" + timestamp);
                    continue;
                }

                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
        } catch (IOException e) {
            logger.severe("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void stopReading() {
        // No action needed for file reading
    }
}
