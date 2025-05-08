package in.hotel.booking_service.controller;

import in.hotel.booking_service.model.Booking;
import in.hotel.booking_service.service.BookingService;
import in.hotel.booking_service.util.NotificationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(service.save(booking));
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        Booking booking = service.findById(id);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability")
    public Map<String, Object> checkAvailability(
            @RequestParam String hotelName,
            @RequestParam String checkIn,
            @RequestParam String checkOut
    ) {
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);
        boolean available = service.isRoomAvailable(hotelName, checkInDate, checkOutDate);

        return Map.of(
            "hotelName", hotelName,
            "checkInDate", checkInDate,
            "checkOutDate", checkOutDate,
            "available", available
        );
    }

}
