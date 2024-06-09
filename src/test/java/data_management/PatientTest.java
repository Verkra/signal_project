package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientTest {

    private Patient patient;

    @BeforeEach
    public void setUp() {
        patient = new Patient(1); // Create a new Patient instance for testing
    }

    @Test
    public void testGetRecordsNoRecords() {
        List<PatientRecord> records = patient.getRecords(0, 0); // Assuming 0,0 as invalid time range
        assertEquals(0, records.size()); // Expecting an empty list
    }

    @Test
    public void testGetRecordsWithinTimeRange() {
        // Add sample records
        patient.addRecord(80, "HeartRate", 1600000000000L);
        patient.addRecord(90, "HeartRate", 1700000000000L);
        patient.addRecord(95, "HeartRate", 1750000000000L);

        // Test for records within a specific time range
        List<PatientRecord> records = patient.getRecords(1600000000000L, 1700000000000L);
        assertEquals(2, records.size()); // Expecting 2 records within the time range
    }

    @Test
    public void testGetRecordsSpecificRecordType() {
        // Add sample records
        patient.addRecord(80, "HeartRate", 1600000000000L);
        patient.addRecord(90, "BloodPressure", 1700000000000L);
        patient.addRecord(95, "HeartRate", 1750000000000L);

        // Test for records of a specific type
        List<PatientRecord> records = patient.getRecords(1600000000000L, 1750000000000L, "HeartRate");
        assertEquals(2, records.size()); // Expecting 2 records of type "HeartRate"
    }

    @Test
    public void testGetRecordsNoMatch() {
        // Add sample records
        patient.addRecord(80, "HeartRate", 1600000000000L);
        patient.addRecord(90, "BloodPressure", 1700000000000L);
        patient.addRecord(95, "ECG", 1750000000000L);

        // Test for records within a specific time range and type
        List<PatientRecord> records = patient.getRecords(1600000000000L, 1700000000000L, "ECG");
        assertEquals(0, records.size()); // Expecting no records matching the criteria
    }
}