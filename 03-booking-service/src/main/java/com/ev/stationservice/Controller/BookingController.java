package com.ev.stationservice.Controller;

import com.ev.stationservice.DTO.BookingRequest;
import com.ev.stationservice.DTO.BookingResponse;
import com.ev.stationservice.DTO.BookingUpdateRequest;
import com.ev.stationservice.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    //    New booking create :
    @PostMapping("/create")
    public ResponseEntity<String> newBookingCreate(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(bookingRequest));
    }

    //    Booking by ID
    @GetMapping("/bookingList/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingListById(@PathVariable String bookingId){
        return ResponseEntity.ok(bookingService.getBookingListById(bookingId));
    }

    //    User ki bookings
    @GetMapping("/userBooking/{userId}")
    public ResponseEntity<List<BookingResponse>> getUsersBookingList(@PathVariable Integer userId){
        return ResponseEntity.ok(bookingService.getUsersBookingList(userId));
    }

    //   All bookings
    @GetMapping("/allBookingList")
    public ResponseEntity<List<BookingResponse>> getAllBookingList(){
        return ResponseEntity.ok(bookingService.getAllBookingList());
    }

    //    Booking details update
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable String bookingId ,
                                                              @RequestBody BookingUpdateRequest bookingUpdateRequest){
        return ResponseEntity.ok(bookingService.updateBooking(bookingId, bookingUpdateRequest));
    }

    //    Booking cancel/delete
    @DeleteMapping("/cencelBooking/{bookingId}")
    public ResponseEntity<String> cancelBookingRequest(@PathVariable String bookingId){
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

}
