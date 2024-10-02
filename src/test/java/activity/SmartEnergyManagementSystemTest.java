package activity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmartEnergyManagementSystemTest {

    final private SmartEnergyManagementSystem smartEnergyManagementSystem = new SmartEnergyManagementSystem();

    @Test
    void test_manageEnergy_WHEN_currentPrice_exceeds_priceThreshold() {
        // Given
        double currentPrice = 0.1;
        double priceThreshold = 0.05;
        var devicePriorities = Map.of("Lights", 1, "Appliances", 2);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 100.0;
        double totalEnergyUsedToday = 50.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Lights", true,
                "Appliances", false,
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertTrue(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

}
