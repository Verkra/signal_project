package com.cardio_generator;

import com.alerts.AlertFactory;
import com.data_management.PatientRecord;
import java.util.List;

public interface AlertStrategy {
    void checkAlert(List<PatientRecord> records, AlertFactory alertFactory);
}

