package data_management;

import com.cardio_generator.generators.AlertGenerator;
import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class AlertGeneratorSimulationTest {
    private OutputStrategy mockOutputStrategy;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setup() {
        mockOutputStrategy = mock(OutputStrategy.class);
        alertGenerator = new AlertGenerator(10); // 10 patients
    }

    @Test
    public void testAlertSimulation() {
        for (int i = 1; i <= 10; i++) {
            alertGenerator.generate(i, mockOutputStrategy);
        }

        // Verify that outputStrategy.output was called appropriately
        verify(mockOutputStrategy, atLeastOnce()).output(anyInt(), anyLong(), eq("Alert"), anyString());
    }
}

