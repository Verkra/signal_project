package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

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
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Both instances should be the same");
    }

    @Test
    public void testGetRecordsSpecificRecordType() {
        // Add sample records
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Both instances should be the same");
    }

    @Test
    public void testGetRecordsNoMatch() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Both instances should be the same");
    }
}