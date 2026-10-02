package com.ev.stationservice.Controller;

import com.ev.stationservice.DTO.BookingRequest;
import com.ev.stationservice.DTO.BookingResponse;
import com.ev.stationservice.DTO.BookingUpdateRequest;
import com.ev.stationservice.Service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;


    @Test
    void newBookingCreate_shouldReturnCreatedStatus() {

        BookingRequest request = new BookingRequest();

        when(bookingService.createBooking(request))
                .thenReturn("booking details sent to db successfully : ");

        ResponseEntity<String> response =
                bookingController.newBookingCreate(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(
                "booking details sent to db successfully : ",
                response.getBody()
        );

        verify(bookingService).createBooking(request);
    }


    @Test
    void getBookingListById_shouldReturnBooking() {

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId("B001");

        when(bookingService.getBookingListById("B001"))
                .thenReturn(bookingResponse);

        ResponseEntity<BookingResponse> response =
                bookingController.getBookingListById("B001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "B001",
                response.getBody().getBookingId()
        );

        verify(bookingService)
                .getBookingListById("B001");
    }


    @Test
    void getUsersBookingList_shouldReturnBookings() {

        BookingResponse booking1 = new BookingResponse();
        booking1.setBookingId("B001");

        BookingResponse booking2 = new BookingResponse();
        booking2.setBookingId("B002");

        when(bookingService.getUsersBookingList(101))
                .thenReturn(List.of(booking1, booking2));

        ResponseEntity<List<BookingResponse>> response =
                bookingController.getUsersBookingList(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(bookingService)
                .getUsersBookingList(101);
    }


    @Test
    void getAllBookingList_shouldReturnAllBookings() {

        BookingResponse booking1 = new BookingResponse();
        booking1.setBookingId("B001");

        BookingResponse booking2 = new BookingResponse();
        booking2.setBookingId("B002");

        when(bookingService.getAllBookingList())
                .thenReturn(List.of(booking1, booking2));

        ResponseEntity<List<BookingResponse>> response =
                bookingController.getAllBookingList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(bookingService).getAllBookingList();
    }


    @Test
    void updateBooking_shouldReturnUpdatedBooking() {

        BookingUpdateRequest request = new BookingUpdateRequest();

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId("B001");
        bookingResponse.setStationId("ST002");
        bookingResponse.setChargerId("CH002");

        when(bookingService.updateBooking("B001", request))
                .thenReturn(bookingResponse);

        ResponseEntity<BookingResponse> response =
                bookingController.updateBooking("B001", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "B001",
                response.getBody().getBookingId()
        );
        assertEquals(
                "ST002",
                response.getBody().getStationId()
        );

        verify(bookingService)
                .updateBooking("B001", request);
    }


    @Test
    void cancelBookingRequest_shouldReturnSuccessMessage() {

        when(bookingService.cancelBooking("B001"))
                .thenReturn(
                        "booking cancel successfully : B001"
                );

        ResponseEntity<String> response =
                bookingController.cancelBookingRequest("B001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "booking cancel successfully : B001",
                response.getBody()
        );

        verify(bookingService)
                .cancelBooking("B001");
    }
}