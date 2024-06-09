package data_management;

import com.alerts.AlertGenerator;
import com.data_management.CustomWebSocketClient;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataStorageIntegrationTest {
    private DataStorage dataStorage;
    private CustomWebSocketClient client;

    @BeforeEach
    public void setup() {
        dataStorage = new DataStorage();
        client = new CustomWebSocketClient(URI.create("ws://localhost:8080"), dataStorage);
    }

    @Test
public void testRealTimeDataProcessing() {
    client.onMessage("1,1623456789000,HeartRate,75.0");
    client.onMessage("1,1623456790000,BloodPressure,120.0");

    List<PatientRecord> heartRateRecords = dataStorage.getRecords(1, 1623456789000L, 1623456790000L, "HeartRate");
    assertEquals(1, heartRateRecords.size());

    List<PatientRecord> bloodPressureRecords = dataStorage.getRecords(1, 1623456789000L, 1623456790000L, "BloodPressure");
    assertEquals(1, bloodPressureRecords.size());

    
}

    @Test
    public void testAlertGeneration() {
        client.onMessage("1,1623456789000,HeartRate,200.0");

        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        List<Patient> patients = dataStorage.getAllPatients();
        for (Patient patient : patients) {
            alertGenerator.evaluateData(patient);
        }

        // Add assertions for generated alerts
    }
}