
// BookingRepository.java
package com.example.booking.repository;

import com.example.booking.model.Booking;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
        @Query("SELECT b FROM Booking b WHERE b.hotelName = :hotelName AND " +
           "(b.checkInDate < :checkOut AND b.checkOutDate > :checkIn)")
    List<Booking> findOverlappingBookings(
        @Param("hotelName") String hotelName,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
    );
}
