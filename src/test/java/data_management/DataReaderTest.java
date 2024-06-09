package data_management;

import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.DataReader;
import com.data_management.FileDataReader;
import com.data_management.MockDataReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataReaderTest {

    @Test
    public void testReadData() throws IOException {
        DataStorage storage = new DataStorage();
        DataReader reader = new MockDataReader();
        
        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertTrue(patients.size() > 0);

        Patient patient = patients.get(0);
        assertEquals(1, patient.getRecords().size());
    }
}
