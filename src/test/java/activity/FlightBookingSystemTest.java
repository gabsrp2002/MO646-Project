package activity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlightBookingSystemTest {

    private final FlightBookingSystem flightBookingSystem = new FlightBookingSystem();

    @Test
    void test_bookFlight_WHEN_there_are_no_seats_available() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        5,
                        LocalDateTime.now(),
                        4,
                        100,
                        100,
                        false,
                        LocalDateTime.now(),
                        0
                );

        assertFalse(bookingResult.confirmation);
        assertEquals(0, bookingResult.totalPrice, 0.01);
        assertEquals(0, bookingResult.refundAmount, 0.01);
        assertFalse(bookingResult.pointsUsed);
    }

    @Test
    void test_bookFlight_WHEN_hours_to_departure_is_less_than_24() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        2,
                        LocalDateTime.now(),
                        5,
                        100,
                        100,
                        false,
                        LocalDateTime.now().plusHours(23),
                        0
                );

        assertTrue(bookingResult.confirmation);
        assertEquals(260, bookingResult.totalPrice, 0.01);
        assertEquals(0, bookingResult.refundAmount, 0.01);
        assertFalse(bookingResult.pointsUsed);
    }

    @Test
    void test_bookFlight_WHEN_passengers_are_more_than_4() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        5,
                        LocalDateTime.now(),
                        5,
                        100,
                        100,
                        false,
                        LocalDateTime.now().plusHours(25),
                        0
                );

        assertTrue(bookingResult.confirmation);
        assertEquals(380, bookingResult.totalPrice, 0.01);
        assertEquals(0, bookingResult.refundAmount, 0.01);
        assertFalse(bookingResult.pointsUsed);
    }

    @Test
    void test_bookFlight_WHEN_reward_points_are_available() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        2,
                        LocalDateTime.now(),
                        5,
                        100,
                        100,
                        false,
                        LocalDateTime.now().plusHours(25),
                        100
                );

        assertTrue(bookingResult.confirmation);
        assertEquals(159, bookingResult.totalPrice, 0.01);
        assertEquals(0, bookingResult.refundAmount, 0.01);
        assertTrue(bookingResult.pointsUsed);
    }

    @Test
    void test_bookFlight_WHEN_cancellation_is_requested_AND_hours_to_departure_is_less_than_48() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        2,
                        LocalDateTime.now(),
                        5,
                        100,
                        100,
                        true,
                        LocalDateTime.now().plusHours(47),
                        0
                );

        assertFalse(bookingResult.confirmation);
        assertEquals(0, bookingResult.totalPrice, 0.01);
        assertEquals(80, bookingResult.refundAmount, 0.01);
        assertFalse(bookingResult.pointsUsed);
    }

    @Test
    void test_bookFlight_WHEN_cancellation_is_requested_AND_hours_to_departure_is_more_than_48() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        2,
                        LocalDateTime.now(),
                        5,
                        100,
                        100,
                        true,
                        LocalDateTime.now().plusHours(49),
                        0
                );

        assertFalse(bookingResult.confirmation);
        assertEquals(0, bookingResult.totalPrice, 0.01);
        assertEquals(160, bookingResult.refundAmount, 0.01);
        assertFalse(bookingResult.pointsUsed);
    }

    @Test
    void test_bookFlight_WHEN_cancellation_AND_4_passengers_AND_24_hours_to_departure() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        4,
                        LocalDateTime.now(),
                        5,
                        100,
                        100,
                        true,
                        LocalDateTime.now().plusHours(24),
                        0
                );

        assertFalse(bookingResult.confirmation);
        assertEquals(0, bookingResult.totalPrice, 0.01);
        assertEquals(160, bookingResult.refundAmount, 0.01);
        assertFalse(bookingResult.pointsUsed);
    }

    @Test
    void test_bookFlight_WHEN_cancellation_AND_4_passengers_AND_48_hours_to_departure() {
        final var bookingResult = flightBookingSystem
                .bookFlight(
                        4,
                        LocalDateTime.now(),
                        5,
                        100,
                        100,
                        true,
                        LocalDateTime.now().plusHours(48),
                        0
                );

        assertFalse(bookingResult.confirmation);
        assertEquals(0, bookingResult.totalPrice, 0.01);
        assertEquals(320, bookingResult.refundAmount, 0.01);
        assertFalse(bookingResult.pointsUsed);
    }

}
