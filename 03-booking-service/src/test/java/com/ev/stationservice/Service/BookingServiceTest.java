package com.ev.stationservice.Service;

import com.ev.stationservice.DTO.BookingRequest;
import com.ev.stationservice.DTO.BookingResponse;
import com.ev.stationservice.DTO.BookingUpdateRequest;
import com.ev.stationservice.Entity.Booking;
import com.ev.stationservice.Kafka.ConsumerDataStore;
import com.ev.stationservice.Kafka.Producer.BookingEventProducer;
import com.ev.stationservice.Repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingEventProducer bookingEventProducer;

    @Mock
    private ConsumerDataStore consumerDataStore;

    @InjectMocks
    private BookingService bookingService;


    // ---------------------------------------------------------
    // createBooking()
    // ---------------------------------------------------------

    @Test
    void createBooking_shouldCreateBookingSuccessfully() {

        BookingRequest request = createBookingRequest();

        when(bookingRepository.findByUserId(101))
                .thenReturn(Optional.empty());

        when(bookingRepository
                .existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        "CH001",
                        request.getBookingDate(),
                        request.getEndTime(),
                        request.getStartTime()))
                .thenReturn(false);

        when(consumerDataStore.containsUserId(101))
                .thenReturn(true);

        when(consumerDataStore.containsStationId("ST001"))
                .thenReturn(true);

        when(consumerDataStore.containsChargerId("CH001"))
                .thenReturn(true);

        String result = bookingService.createBooking(request);

        assertEquals(
                "booking details sent to db successfully : ",
                result
        );

        verify(bookingRepository).save(any(Booking.class));
        verify(bookingEventProducer)
                .publishingBookingDetailsToEvent(any());
    }


    @Test
    void createBooking_shouldThrowException_whenRequiredFieldIsMissing() {

        BookingRequest request = new BookingRequest();

        request.setUserId(null);
        request.setStationId("ST001");
        request.setChargerId("CH001");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.createBooking(request)
        );

        assertEquals(
                "give required field before booking ",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    @Test
    void createBooking_shouldThrowException_whenUserAlreadyHasBooking() {

        BookingRequest request = createBookingRequest();

        when(bookingRepository.findByUserId(101))
                .thenReturn(Optional.of(new Booking()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.createBooking(request)
        );

        assertEquals(
                "user is already booked for charger ",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    @Test
    void createBooking_shouldThrowException_whenChargerAlreadyBooked() {

        BookingRequest request = createBookingRequest();

        when(bookingRepository.findByUserId(101))
                .thenReturn(Optional.empty());

        when(bookingRepository
                .existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        "CH001",
                        request.getBookingDate(),
                        request.getEndTime(),
                        request.getStartTime()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.createBooking(request)
        );

        assertEquals(
                "booking is not allowed for this particular charger id because , this charger is already booked by some other user ",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    @Test
    void createBooking_shouldThrowException_whenUserDataIsNotConsumed() {

        BookingRequest request = createBookingRequest();

        when(bookingRepository.findByUserId(101))
                .thenReturn(Optional.empty());

        when(bookingRepository
                .existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        "CH001",
                        request.getBookingDate(),
                        request.getEndTime(),
                        request.getStartTime()))
                .thenReturn(false);

        when(consumerDataStore.containsUserId(101))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.createBooking(request)
        );

        assertEquals(
                "user ID is not available in consumed user Data",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    @Test
    void createBooking_shouldThrowException_whenStationDataIsNotConsumed() {

        BookingRequest request = createBookingRequest();

        when(bookingRepository.findByUserId(101))
                .thenReturn(Optional.empty());

        when(bookingRepository
                .existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        "CH001",
                        request.getBookingDate(),
                        request.getEndTime(),
                        request.getStartTime()))
                .thenReturn(false);

        when(consumerDataStore.containsUserId(101))
                .thenReturn(true);

        when(consumerDataStore.containsStationId("ST001"))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.createBooking(request)
        );

        assertEquals(
                "station ID is not available in consumer station data",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    @Test
    void createBooking_shouldThrowException_whenChargerDataIsNotConsumed() {

        BookingRequest request = createBookingRequest();

        when(bookingRepository.findByUserId(101))
                .thenReturn(Optional.empty());

        when(bookingRepository
                .existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        "CH001",
                        request.getBookingDate(),
                        request.getEndTime(),
                        request.getStartTime()))
                .thenReturn(false);

        when(consumerDataStore.containsUserId(101))
                .thenReturn(true);

        when(consumerDataStore.containsStationId("ST001"))
                .thenReturn(true);

        when(consumerDataStore.containsChargerId("CH001"))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.createBooking(request)
        );

        assertEquals(
                "charger ID is not available in consumed charger Data ",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    // ---------------------------------------------------------
    // getBookingListById()
    // ---------------------------------------------------------

    @Test
    void getBookingListById_shouldReturnBooking_whenBookingExists() {

        Booking booking = createBookingEntity();

        when(bookingRepository.findById("B001"))
                .thenReturn(Optional.of(booking));

        BookingResponse response =
                bookingService.getBookingListById("B001");

        assertNotNull(response);
        assertEquals("B001", response.getBookingId());
        assertEquals(101, response.getUserId());
        assertEquals("ST001", response.getStationId());
        assertEquals("CH001", response.getChargerId());
        assertEquals("PENDING", response.getStatus());
    }


    @Test
    void getBookingListById_shouldThrowException_whenBookingDoesNotExist() {

        when(bookingRepository.findById("B999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.getBookingListById("B999")
        );

        assertEquals(
                "booking id is not found : B999",
                exception.getMessage()
        );
    }


    // ---------------------------------------------------------
    // getUsersBookingList()
    // ---------------------------------------------------------

    @Test
    void getUsersBookingList_shouldReturnBookings() {

        Booking booking1 = createBookingEntity();
        Booking booking2 = createBookingEntity();

        booking2.setBookingId("B002");

        when(bookingRepository.findAllByUserId(101))
                .thenReturn(List.of(booking1, booking2));

        List<BookingResponse> result =
                bookingService.getUsersBookingList(101);

        assertEquals(2, result.size());
        assertEquals("B001", result.get(0).getBookingId());
        assertEquals("B002", result.get(1).getBookingId());
    }


    @Test
    void getUsersBookingList_shouldReturnEmptyList_whenNoBookingsExist() {

        when(bookingRepository.findAllByUserId(101))
                .thenReturn(new ArrayList<>());

        List<BookingResponse> result =
                bookingService.getUsersBookingList(101);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    // ---------------------------------------------------------
    // getAllBookingList()
    // ---------------------------------------------------------

    @Test
    void getAllBookingList_shouldReturnAllBookings() {

        Booking booking1 = createBookingEntity();
        Booking booking2 = createBookingEntity();

        booking2.setBookingId("B002");

        when(bookingRepository.findAll())
                .thenReturn(List.of(booking1, booking2));

        List<BookingResponse> result =
                bookingService.getAllBookingList();

        assertEquals(2, result.size());
        assertEquals("B001", result.get(0).getBookingId());
        assertEquals("B002", result.get(1).getBookingId());
    }


    @Test
    void getAllBookingList_shouldReturnEmptyList_whenNoBookingsExist() {

        when(bookingRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<BookingResponse> result =
                bookingService.getAllBookingList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    // ---------------------------------------------------------
    // updateBooking()
    // ---------------------------------------------------------

    @Test
    void updateBooking_shouldUpdateBookingSuccessfully() {

        Booking booking = createBookingEntity();

        BookingUpdateRequest request = createBookingUpdateRequest();

        when(bookingRepository.findById("B001"))
                .thenReturn(Optional.of(booking));

        when(bookingRepository
                .existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndBookingIdNot(
                        "CH002",
                        request.getBookingDate(),
                        request.getStartTime(),
                        request.getEndTime(),
                        "B001"))
                .thenReturn(false);

        when(bookingRepository.save(booking))
                .thenReturn(booking);

        BookingResponse response =
                bookingService.updateBooking("B001", request);

        assertNotNull(response);
        assertEquals("B001", response.getBookingId());
        assertEquals("ST002", response.getStationId());
        assertEquals("CH002", response.getChargerId());
        assertEquals("PENDING", response.getStatus());

        verify(bookingRepository).save(booking);
    }


    @Test
    void updateBooking_shouldThrowException_whenBookingDoesNotExist() {

        BookingUpdateRequest request = createBookingUpdateRequest();

        when(bookingRepository.findById("B999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.updateBooking("B999", request)
        );

        assertEquals(
                "booking id is not found : B999",
                exception.getMessage()
        );
    }


    @Test
    void updateBooking_shouldThrowException_whenRequiredDetailsAreMissing() {

        Booking booking = createBookingEntity();

        BookingUpdateRequest request = new BookingUpdateRequest();

        when(bookingRepository.findById("B001"))
                .thenReturn(Optional.of(booking));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.updateBooking("B001", request)
        );

        assertEquals(
                "all booking details are required",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    @Test
    void updateBooking_shouldThrowException_whenChargerIsAlreadyBooked() {

        Booking booking = createBookingEntity();

        BookingUpdateRequest request = createBookingUpdateRequest();

        when(bookingRepository.findById("B001"))
                .thenReturn(Optional.of(booking));

        when(bookingRepository
                .existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndBookingIdNot(
                        "CH002",
                        request.getBookingDate(),
                        request.getStartTime(),
                        request.getEndTime(),
                        "B001"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.updateBooking("B001", request)
        );

        assertEquals(
                "this charger is already booked for the selected time",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any(Booking.class));
    }


    // ---------------------------------------------------------
    // cancelBooking()
    // ---------------------------------------------------------

    @Test
    void cancelBooking_shouldCancelBookingSuccessfully() {

        Booking booking = createBookingEntity();

        when(bookingRepository.findById("B001"))
                .thenReturn(Optional.of(booking));

        String result =
                bookingService.cancelBooking("B001");

        assertEquals(
                "booking cancel successfully : B001",
                result
        );

        verify(bookingRepository).deleteById("B001");
    }


    @Test
    void cancelBooking_shouldReturnMessage_whenBookingDoesNotExist() {

        when(bookingRepository.findById("B999"))
                .thenReturn(Optional.empty());

        String result =
                bookingService.cancelBooking("B999");

        assertEquals(
                " booking is not found for delete or cancel booking :B999",
                result
        );

        verify(bookingRepository, never())
                .deleteById(anyString());
    }


    // ---------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------

    private BookingRequest createBookingRequest() {

        BookingRequest request = new BookingRequest();

        request.setUserId(101);
        request.setStationId("ST001");
        request.setChargerId("CH001");
        request.setBookingDate(LocalDate.of(2026, 10, 10));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(11, 0));

        return request;
    }


    private BookingUpdateRequest createBookingUpdateRequest() {

        BookingUpdateRequest request = new BookingUpdateRequest();

        request.setStationId("ST002");
        request.setChargerId("CH002");
        request.setBookingDate(LocalDate.of(2026, 10, 11));
        request.setStartTime(LocalTime.of(12, 0));
        request.setEndTime(LocalTime.of(13, 0));

        return request;
    }


    private Booking createBookingEntity() {

        Booking booking = new Booking();

        booking.setBookingId("B001");
        booking.setUserId(101);
        booking.setStationId("ST001");
        booking.setChargerId("CH001");
        booking.setBookingDate(LocalDate.of(2026, 10, 10));
        booking.setStartTime(LocalTime.of(10, 0));
        booking.setEndTime(LocalTime.of(11, 0));
        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.of(
                2026, 10, 1, 10, 0
        ));

        return booking;
    }
}