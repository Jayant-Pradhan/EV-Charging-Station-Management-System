package com.ev.stationservice.Repository;

import com.ev.stationservice.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,String> {

    Optional<Booking> findByUserId(Integer userId);
    List<Booking> findAllByUserId(Integer userId);

    boolean existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
            String chargerId,
            LocalDate bookingDate,
            LocalTime endTime,
            LocalTime startTime
    );

    boolean existsByChargerIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThanAndBookingIdNot(
            String chargerId,
            LocalDate bookingDate,
            LocalTime endTime,
            LocalTime startTime,
            String bookingId
    );



}
