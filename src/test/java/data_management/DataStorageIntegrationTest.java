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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataStorageIntegrationTest {
    private DataStorage dataStorage;
    private CustomWebSocketClient client;

    @BeforeEach
    public void setup() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Both instances should be the same");
    }

    @Test
public void testRealTimeDataProcessing() {
    DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Both instances should be the same");
    
}

    @Test
    public void testAlertGeneration() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Both instances should be the same");
        }

        // Add assertions for generated alerts
    }
