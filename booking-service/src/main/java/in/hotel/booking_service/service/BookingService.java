package in.hotel.booking_service.service;

import in.hotel.booking_service.model.Booking;
import in.hotel.booking_service.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    public List<Booking> findAll() {
        return repository.findAll();
    }

    public Booking findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean isRoomAvailable(String hotelName, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> conflicts = repository.findOverlappingBookings(hotelName, checkIn, checkOut);
        return conflicts.isEmpty();
    }
}
