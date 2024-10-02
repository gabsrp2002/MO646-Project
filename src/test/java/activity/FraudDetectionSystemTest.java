package activity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FraudDetectionSystemTest {

    private final FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem();

    @Test
    void test_checkForFraud_WHEN_transaction_amount_is_high(){
        final var currentTransaction = new FraudDetectionSystem.Transaction(
                10000.01,
                LocalDateTime.now(),
                "location"
        );
        final var checkResult = fraudDetectionSystem
                .checkForFraud(
                        currentTransaction,
                        new ArrayList<>(),
                        new ArrayList<>()
                );
        assertTrue(checkResult.isFraudulent);
        assertFalse(checkResult.isBlocked);
        assertTrue(checkResult.verificationRequired);
        assertEquals(50, checkResult.riskScore);

    }

    @Test
    void test_checkForFraud_WHEN_lots_of_transactions_in_short_time(){
        final var currentTransaction = new FraudDetectionSystem.Transaction(
                10,
                LocalDateTime.now(),
                "location"
        );
        final var pastTransactions = Collections.nCopies(11, currentTransaction);
        final var checkResult = fraudDetectionSystem
                .checkForFraud(
                        currentTransaction,
                        pastTransactions,
                        new ArrayList<>()
                );
        assertFalse(checkResult.isFraudulent);
        assertTrue(checkResult.isBlocked);
        assertFalse(checkResult.verificationRequired);
        assertEquals(30, checkResult.riskScore);
    }

    @Test
    void test_checkForFraud_WHEN_location_changed_within_a_short_time_frame(){
        final var pastTransaction = new FraudDetectionSystem.Transaction(
                10,
                LocalDateTime.now(),
                "location_1"
        );
        final var pastTransactions = List.of(pastTransaction);
        final var currentTransaction = new FraudDetectionSystem.Transaction(
                10,
                LocalDateTime.now(),
                "location_2"
        );
        final var checkResult = fraudDetectionSystem
                .checkForFraud(
                        currentTransaction,
                        pastTransactions,
                        new ArrayList<>()
                );
        assertTrue(checkResult.isFraudulent);
        assertFalse(checkResult.isBlocked);
        assertTrue(checkResult.verificationRequired);
        assertEquals(20, checkResult.riskScore);
    }

    @Test
    void test_checkForFraud_WHEN_location_is_in_blacklist(){
        final var currentTransaction = new FraudDetectionSystem.Transaction(
                10,
                LocalDateTime.now(),
                "blacklisted_location"
        );
        final var blackListedLocations = List.of("blacklisted_location");
        final var checkResult = fraudDetectionSystem
                .checkForFraud(
                        currentTransaction,
                        new ArrayList<>(),
                        blackListedLocations
                );
        assertFalse(checkResult.isFraudulent);
        assertTrue(checkResult.isBlocked);
        assertFalse(checkResult.verificationRequired);
        assertEquals(100, checkResult.riskScore);
    }
}