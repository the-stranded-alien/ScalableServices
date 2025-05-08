package in.hotel.booking_service.service;

import in.hotel.booking_service.model.Booking;
import in.hotel.booking_service.repository.BookingRepository;
import in.hotel.booking_service.util.NotificationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository repository;
    private final NotificationUtil notificationUtil;

    public BookingService(BookingRepository repository, NotificationUtil notificationUtil) {
        this.repository = repository;
        this.notificationUtil = notificationUtil;
    }

    public Booking save(Booking booking) {
        Booking savedBooking = repository.save(booking);
        notificationUtil.sendCreateBookingAudit(savedBooking.getId().toString());
        notificationUtil.sendCreateBookingNotification(savedBooking.getId().toString());
        return savedBooking;
    }

    public List<Booking> findAll() {
        notificationUtil.sendAllBookingViewedAudit();
        notificationUtil.sendBookingViewNotification();
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
