package activity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void test_manageEnergy_WHEN_currentPrice_does_not_exceed_priceThreshold() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
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
                "Appliances", true,
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_in_Night_Mode() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Security", 1, "Refrigerator", 1, "Lights", 2, "Appliances", 3);
        var currentTime = LocalDateTime.of(2021, 1, 1, 23, 30);
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
                "Security", true,
                "Refrigerator", true,
                "Lights", false,
                "Appliances", false,
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_in_Night_Mode_and_energy_saving_mode() {
        // Given
        double currentPrice = 0.15;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Security", 1, "Refrigerator", 1, "Lights", 2, "Appliances", 2);
        var currentTime = LocalDateTime.of(2021, 1, 1, 23, 30);
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
                "Security", true,
                "Refrigerator", true,
                "Lights", false,
                "Appliances", false,
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertTrue(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_currentTemperature_below_desiredRange() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Heating", 1);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 18.0; // Below desired range
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
                "Heating", true
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertTrue(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_currentTemperature_above_desiredRange() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Cooling", 1);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 27.0; // Above desired range
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
                "Cooling", true
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertTrue(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_currentTemperature_within_desiredRange() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Heating", 1, "Cooling", 1);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 22.0; // Within desired range
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
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_energyUsageLimit_exceeded() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Heating", 1, "Lights", 2, "Appliances", 3);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 18.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 30.0;
        double totalEnergyUsedToday = 31.0; // Equal to limit
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Heating", true,
                "Lights", false,
                "Appliances", false);
        expectedDeviceStatus.forEach((key, expectedValue) -> {
            boolean actualValue = result.deviceStatus.getOrDefault(key, false);
            if (actualValue != expectedValue) {
                System.out.println("Mismatch for key: " + key + ". Expected: " + expectedValue + ", but got: " + actualValue);
            }
        });
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertTrue(result.temperatureRegulationActive);
        assertEquals(29.0, result.totalEnergyUsed); // Reduced by 2
    }

    @Test
    void test_manageEnergy_WHEN_device_is_scheduled() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Oven", 2);
        var currentTime = LocalDateTime.of(2021, 1, 1, 18, 0);
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 100.0;
        double totalEnergyUsedToday = 50.0;
        var scheduledDevices = List.of(
                new SmartEnergyManagementSystem.DeviceSchedule("Oven", currentTime)
        );

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Oven", true,
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_device_is_scheduled_during_energy_saving_mode() {
        // Given
        double currentPrice = 0.15; // Exceeds threshold
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Oven", 3);
        var currentTime = LocalDateTime.of(2021, 1, 1, 18, 0);
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 100.0;
        double totalEnergyUsedToday = 50.0;
        var scheduledDevices = List.of(
                new SmartEnergyManagementSystem.DeviceSchedule("Oven", currentTime)
        );

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Oven", true,
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertTrue(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_device_is_scheduled_during_night_mode() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Oven", 3, "Security", 1, "Refrigerator", 1);
        var currentTime = LocalDateTime.of(2021, 1, 1, 23, 30);
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 100.0;
        double totalEnergyUsedToday = 50.0;
        var scheduledDevices = List.of(
                new SmartEnergyManagementSystem.DeviceSchedule("Oven", currentTime)
        );

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Oven", true,
                "Security", true,
                "Refrigerator", true,
                "Heating", false,
                "Cooling", false
        );

        expectedDeviceStatus.forEach((key, expectedValue) -> {
            boolean actualValue = result.deviceStatus.getOrDefault(key, null);
            if (actualValue != expectedValue) {
                System.out.println("Mismatch for key: " + key + ". Expected: " + expectedValue + ", but got: " + actualValue);
            }
        });

        assertTrue(result.deviceStatus.equals(expectedDeviceStatus), "Device statuses do not match");
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_no_scheduled_devices_and_schedule_list_not_empty() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Washer", 2);
        var currentTime = LocalDateTime.of(2021, 1, 1, 10, 0); // Current time is 10:00 AM
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 24.0};
        double energyUsageLimit = 100.0;
        double totalEnergyUsedToday = 20.0;
        // Scheduled for 9:00 AM and 11:00 AM, but current time is 10:00 AM
        var scheduledDevices = List.of(
                new SmartEnergyManagementSystem.DeviceSchedule("Washer", LocalDateTime.of(2021, 1, 1, 9, 0)),
                new SmartEnergyManagementSystem.DeviceSchedule("Dryer", LocalDateTime.of(2021, 1, 1, 11, 0))
        );

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature,
                desiredTemperatureRange, energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        // Since none of the scheduled times match current time, devices should not be turned on due to scheduling
        final var expectedDeviceStatus = Map.of(
                "Washer", true, // Should be on because it's not in energy-saving mode and no other constraints
                "Heating", false,
                "Cooling", false
        );

        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(20.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_time_is_early_morning_before_6am() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of(
                "Security", 1,
                "Refrigerator", 1,
                "Lights", 2,
                "Appliances", 3
        );
        var currentTime = LocalDateTime.of(2021, 1, 1, 5, 0); // Time is 5 AM
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 24.0};
        double energyUsageLimit = 100.0;
        double totalEnergyUsedToday = 20.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature,
                desiredTemperatureRange, energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Security", true,
                "Refrigerator", true,
                "Heating", false,
                "Cooling", false,
                "Lights", false,
                "Appliances", false
        );

        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(20.0, result.totalEnergyUsed);
    }

    @Test
    void test_manageEnergy_WHEN_energyUsageLimit_not_exceeded() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Heating", 1, "Lights", 2, "Appliances", 3);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 100.0;
        double totalEnergyUsedToday = 50.0; // Below limit
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Heating", false,
                "Lights", true,
                "Appliances", true,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(50.0, result.totalEnergyUsed); // Should remain unchanged
    }

    @Test
    void test_manageEnergy_WHEN_energyUsageLimit_exceeded_but_all_devices_off() {
        // Given
        double currentPrice = 0.15;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Lights", 2, "Appliances", 3);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 22.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 20.0;
        double totalEnergyUsedToday = 21.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Lights", false,
                "Appliances", false,
                "Heating", false,
                "Cooling", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertTrue(result.energySavingMode);
        assertFalse(result.temperatureRegulationActive);
        assertEquals(21.0, result.totalEnergyUsed); // Should remain unchanged
    }

    @Test
    void test_manageEnergy_WHEN_energyUsageLimit_exceeded_and_high_priority_devices_on() {
        // Given
        double currentPrice = 0.05;
        double priceThreshold = 0.1;
        var devicePriorities = Map.of("Heating", 1, "Lights", 2, "Appliances", 3);
        var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
        double currentTemperature = 18.0;
        double[] desiredTemperatureRange = {20.0, 25.0};
        double energyUsageLimit = 20.0;
        double totalEnergyUsedToday = 21.0;
        List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

        // When
        var result = smartEnergyManagementSystem.manageEnergy(
                currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
                energyUsageLimit, totalEnergyUsedToday, scheduledDevices
        );

        // Then
        final var expectedDeviceStatus = Map.of(
                "Heating", true,
                "Lights", false,
                "Appliances", false
        );
        assertEquals(expectedDeviceStatus, result.deviceStatus);
        assertFalse(result.energySavingMode);
        assertTrue(result.temperatureRegulationActive);
        assertEquals(19.0, result.totalEnergyUsed); // Reduced from 21.0 by turning off devices
    }
    @Test
void test_manageEnergy_WHEN_currentPrice_equals_priceThreshold() {
    // Given
    double currentPrice = 0.1;
    double priceThreshold = 0.1;
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
            "Appliances", true,
            "Heating", false,
            "Cooling", false
    );
    assertEquals(expectedDeviceStatus, result.deviceStatus);
    assertFalse(result.energySavingMode);
    assertFalse(result.temperatureRegulationActive);
    assertEquals(50.0, result.totalEnergyUsed);
}

@Test
void test_manageEnergy_WHEN_time_is_exactly_night_mode_start() {
    // Given
    double currentPrice = 0.05;
    double priceThreshold = 0.1;
    var devicePriorities = Map.of("Security", 1, "Refrigerator", 1, "Lights", 2, "Appliances", 3);
    var currentTime = LocalDateTime.of(2021, 1, 1, 23, 0); // 11:00 PM
    double currentTemperature = 22.0;
    double[] desiredTemperatureRange = {20.0, 24.0};
    double energyUsageLimit = 100.0;
    double totalEnergyUsedToday = 20.0;
    List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

    // When
    var result = smartEnergyManagementSystem.manageEnergy(
            currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature,
            desiredTemperatureRange, energyUsageLimit, totalEnergyUsedToday, scheduledDevices
    );

    // Then
    final var expectedDeviceStatus = Map.of(
            "Security", true,
            "Refrigerator", true,
            "Lights", false,
            "Appliances", false,
            "Heating", false,
            "Cooling", false
    );
    assertEquals(expectedDeviceStatus, result.deviceStatus);
    assertFalse(result.energySavingMode);
    assertFalse(result.temperatureRegulationActive);
    assertEquals(20.0, result.totalEnergyUsed);
}

@Test
void test_manageEnergy_WHEN_time_is_exactly_night_mode_end() {
    // Given
    double currentPrice = 0.05;
    double priceThreshold = 0.1;
    var devicePriorities = Map.of("Security", 1, "Refrigerator", 1, "Lights", 2, "Appliances", 3);
    var currentTime = LocalDateTime.of(2021, 1, 1, 6, 0); // 6:00 AM
    double currentTemperature = 22.0;
    double[] desiredTemperatureRange = {20.0, 24.0};
    double energyUsageLimit = 100.0;
    double totalEnergyUsedToday = 20.0;
    List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

    // When
    var result = smartEnergyManagementSystem.manageEnergy(
            currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature,
            desiredTemperatureRange, energyUsageLimit, totalEnergyUsedToday, scheduledDevices
    );

    // Then
    final var expectedDeviceStatus = Map.of(
            "Security", true,
            "Refrigerator", true,
            "Lights", true,
            "Appliances", true,
            "Heating", false,
            "Cooling", false
    );
    assertEquals(expectedDeviceStatus, result.deviceStatus);
    assertFalse(result.energySavingMode);
    assertFalse(result.temperatureRegulationActive);
    assertEquals(20.0, result.totalEnergyUsed);
}
@Test
void test_manageEnergy_WHEN_currentTemperature_equals_desiredTemperatureRange_lower_bound() {
    // Given
    double currentPrice = 0.05;
    double priceThreshold = 0.1;
    var devicePriorities = Map.of("Heating", 1, "Cooling", 1);
    var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
    double currentTemperature = 20.0; // Equal to lower bound
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
            "Heating", false,
            "Cooling", false
    );
    assertEquals(expectedDeviceStatus, result.deviceStatus);
    assertFalse(result.temperatureRegulationActive);
}

@Test
void test_manageEnergy_WHEN_currentTemperature_equals_desiredTemperatureRange_upper_bound() {
    // Given
    double currentPrice = 0.05;
    double priceThreshold = 0.1;
    var devicePriorities = Map.of("Heating", 1, "Cooling", 1);
    var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
    double currentTemperature = 25.0; // Equal to upper bound
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
            "Heating", false,
            "Cooling", false
    );
    assertEquals(expectedDeviceStatus, result.deviceStatus);
    assertFalse(result.temperatureRegulationActive);
}
@Test
void test_manageEnergy_WHEN_totalEnergyUsedToday_equals_energyUsageLimit() {
    // Given
    double currentPrice = 0.05;
    double priceThreshold = 0.1;
    var devicePriorities = Map.of("Lights", 2, "Appliances", 3);
    var currentTime = LocalDateTime.of(2021, 1, 1, 12, 0);
    double currentTemperature = 22.0;
    double[] desiredTemperatureRange = {20.0, 25.0};
    double energyUsageLimit = 50.0;
    double totalEnergyUsedToday = 50.0; // Equal to limit
    List<SmartEnergyManagementSystem.DeviceSchedule> scheduledDevices = emptyList();

    // When
    var result = smartEnergyManagementSystem.manageEnergy(
        currentPrice, priceThreshold, devicePriorities, currentTime, currentTemperature, desiredTemperatureRange,
        energyUsageLimit, totalEnergyUsedToday, scheduledDevices
    );

    // Then
    final var expectedDeviceStatus = Map.of(
            "Lights", false,
            "Appliances", false,
            "Heating", false,
            "Cooling", false
    );
    assertEquals(expectedDeviceStatus, result.deviceStatus);
    assertFalse(result.temperatureRegulationActive);
    assertTrue(result.totalEnergyUsed <= energyUsageLimit);
}








}