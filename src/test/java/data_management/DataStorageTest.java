package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataStorageTest {

    private DataStorage dataStorage;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
    }

    @Test
    public void testAddPatientData() {
        dataStorage.addPatientData(1, 75.0, "HeartRate", 1600000000000L);
        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE, "HeartRate");
        assertEquals(1, records.size());

        PatientRecord record = records.get(0);
        assertEquals(75.0, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1600000000000L, record.getTimestamp());
    }

    @Test
    public void testGetRecordsNoRecords() {
        List<PatientRecord> records = dataStorage.getRecords(1, 1600000000000L, 1700000000000L, "HeartRate");
        assertTrue(records.isEmpty());
    }

    @Test
    public void testGetRecordsWithinTimeRange() {
        dataStorage.addPatientData(1, 80, "HeartRate", 1600000000000L);
        dataStorage.addPatientData(1, 90, "HeartRate", 1700000000000L);
        dataStorage.addPatientData(1, 95, "HeartRate", 1750000000000L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1600000000000L, 1700000000000L, "HeartRate");
        assertEquals(2, records.size());
    }

    @Test
    public void testGetAllPatients() {
        dataStorage.addPatientData(1, 80, "HeartRate", 1600000000000L);
        dataStorage.addPatientData(2, 120, "BloodPressure", 1700000000000L);
        List<Patient> patients = dataStorage.getAllPatients();
        assertEquals(2, patients.size());
    }

    @Test
    public void testGetRecordsNoMatch() {
        dataStorage.addPatientData(1, 80, "HeartRate", 1600000000000L);
        dataStorage.addPatientData(1, 90, "BloodPressure", 1700000000000L);
        dataStorage.addPatientData(1, 95, "ECG", 1750000000000L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1600000000000L, 1700000000000L, "ECG");
        assertEquals(0, records.size());
    }

    @Test
    public void testAddMultipleRecords() {
        dataStorage.addPatientData(1, 80, "HeartRate", 1600000000000L);
        dataStorage.addPatientData(1, 85, "HeartRate", 1600003600000L);
        dataStorage.addPatientData(1, 90, "HeartRate", 1600007200000L);

        List<PatientRecord> records = dataStorage.getRecords(1, 1600000000000L, Long.MAX_VALUE, "HeartRate");
        assertEquals(3, records.size());
    }
}