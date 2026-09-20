package com.ev.stationservice.Service;

import com.ev.stationservice.DTO.BookingRequest;
import com.ev.stationservice.DTO.BookingResponse;
import com.ev.stationservice.DTO.BookingUpdateRequest;
import com.ev.stationservice.Entity.Booking;
import com.ev.stationservice.Kafka.Event.BookingCreatedEvent;
import com.ev.stationservice.Kafka.Producer.BookingEventProducer;
import com.ev.stationservice.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingEventProducer bookingEventProducer;

    public String createBooking(BookingRequest bookingRequest) {
        if(bookingRequest.getUserId() == null || bookingRequest.getStationId() == null || bookingRequest.getChargerId() == null){
            throw new RuntimeException("give required field before booking ");
        } else if (bookingRepository.findByUserId(bookingRequest.getUserId()).isPresent()) {
            throw new RuntimeException("user is already booked for charger ");
        }
        else if (bookingRepository.existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                bookingRequest.getChargerId() , bookingRequest.getBookingDate() , bookingRequest.getEndTime(),
                bookingRequest.getStartTime())){
            throw new RuntimeException("booking is not allowed for this particular charger id because , this charger is already booked by some other user ");
        }
        else{
            Booking booking = new Booking();
            booking.setBookingId(UUID.randomUUID().toString());
            booking.setChargerId(bookingRequest.getChargerId());
            booking.setBookingDate(bookingRequest.getBookingDate());
            booking.setEndTime(bookingRequest.getEndTime());
            booking.setStartTime(bookingRequest.getStartTime());
            booking.setUserId(bookingRequest.getUserId());
            booking.setStationId(bookingRequest.getStationId());
            booking.setStatus("PENDING");
            booking.setCreatedAt(LocalDateTime.now());
            bookingRepository.save(booking);

            bookingEventProducer.publishingBookingDetailsToEvent(
                    new BookingCreatedEvent(
                            booking.getBookingId(),
                            booking.getUserId(),
                            booking.getStationId(),
                            booking.getChargerId(),
                            booking.getBookingDate(),
                            booking.getStartTime(),
                            booking.getEndTime(),
                            booking.getStatus(),
                            booking.getCreatedAt() ));

            return "booking details sent to db successfully : " ;
        }
    }

    public BookingResponse getBookingListById(String bookingId) {

        Optional<Booking> isAvail = bookingRepository.findById(bookingId);

        if(isAvail.isPresent()){
            Booking booking = isAvail.get();
            BookingResponse response = new BookingResponse();
            response.setBookingId(booking.getBookingId());
            response.setBookingDate(booking.getBookingDate());
            response.setChargerId(booking.getChargerId());
            response.setStationId(booking.getStationId());
            response.setCreatedAt(booking.getCreatedAt());
            response.setStartTime(booking.getStartTime());
            response.setEndTime(booking.getEndTime());
            response.setUserId(booking.getUserId());
            response.setStatus(booking.getStatus());

            return response;
        }
        else{
            throw new RuntimeException("booking id is not found : " +  bookingId);
        }
    }

    public List<BookingResponse> getUsersBookingList(Integer userId) {

          List<BookingResponse> responseList = new ArrayList<>();

          List<Booking> bookings = bookingRepository.findAllByUserId(userId);

          for(Booking booking : bookings){
              BookingResponse response = new BookingResponse();
              response.setUserId(booking.getUserId());
              response.setStationId(booking.getStationId());
              response.setChargerId(booking.getChargerId());
              response.setBookingDate(booking.getBookingDate());
              response.setStartTime(booking.getStartTime());
              response.setEndTime(booking.getEndTime());
              response.setCreatedAt(booking.getCreatedAt());
              response.setBookingId(booking.getBookingId());
              response.setStatus(booking.getStatus());
              responseList.add(response);
          }
          return responseList;
    }

    public List<BookingResponse> getAllBookingList() {
        List<Booking> responses = bookingRepository.findAll();

        List<BookingResponse> responseList = new ArrayList<>();

        for(Booking booking : responses){

            BookingResponse response = new BookingResponse();

            response.setUserId(booking.getUserId());
            response.setStationId(booking.getStationId());
            response.setChargerId(booking.getChargerId());
            response.setBookingDate(booking.getBookingDate());
            response.setStartTime(booking.getStartTime());
            response.setEndTime(booking.getEndTime());
            response.setCreatedAt(booking.getCreatedAt());
            response.setBookingId(booking.getBookingId());
            response.setStatus(booking.getStatus());
            responseList.add(response);

        }
        return responseList;

    }

    public BookingResponse updateBooking(String bookingId, BookingUpdateRequest bookingUpdateRequest) {

        Optional<Booking> isAvail = bookingRepository.findById(bookingId);

        BookingResponse response = new BookingResponse();

        if(isAvail.isPresent()){
            if(bookingUpdateRequest.getStationId() == null ||
                    bookingUpdateRequest.getChargerId() == null ||
                    bookingUpdateRequest.getBookingDate() == null ||
                    bookingUpdateRequest.getStartTime() == null ||
                    bookingUpdateRequest.getEndTime() == null){
                throw new RuntimeException("all booking details are required");
            }
            else if(bookingRepository.existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndBookingIdNot(
                    bookingUpdateRequest.getChargerId() , bookingUpdateRequest.getBookingDate(),
                    bookingUpdateRequest.getStartTime(),bookingUpdateRequest.getEndTime(),bookingId)){

                throw new RuntimeException(
                        "this charger is already booked for the selected time"
                );
            }
            else{
                Booking booking = isAvail.get();
                booking.setStationId(bookingUpdateRequest.getStationId());
                booking.setChargerId(bookingUpdateRequest.getChargerId());
                booking.setBookingDate(bookingUpdateRequest.getBookingDate());
                booking.setStartTime(bookingUpdateRequest.getStartTime());
                booking.setEndTime(bookingUpdateRequest.getEndTime());
                bookingRepository.save(booking);


                response.setBookingId(booking.getBookingId());
                response.setBookingDate(booking.getBookingDate());
                response.setChargerId(booking.getChargerId());
                response.setStationId(booking.getStationId());
                response.setCreatedAt(booking.getCreatedAt());
                response.setStartTime(booking.getStartTime());
                response.setEndTime(booking.getEndTime());
                response.setUserId(booking.getUserId());
                response.setStatus(booking.getStatus());

                return response;
            }



        }
        else{
            throw new RuntimeException("booking id is not found : " + bookingId);
        }
    }


    public String cancelBooking(String bookingId) {

        Optional<Booking> isAvail = bookingRepository.findById(bookingId);
        if(isAvail.isPresent()){
            bookingRepository.deleteById(bookingId);
            return "booking cancel successfully : " + bookingId ;
        }
        else{
            return " booking is not found for delete or cancel booking :" + bookingId ;
        }

    }
}
