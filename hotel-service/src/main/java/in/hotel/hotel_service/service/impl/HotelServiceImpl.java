package in.hotel.hotel_service.service.impl;

import in.hotel.hotel_service.model.Hotel;
import in.hotel.hotel_service.repository.HotelRepository;
import in.hotel.hotel_service.service.HotelService;
import in.hotel.hotel_service.util.NotificationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final NotificationUtil notificationUtil;

    @Override
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        notificationUtil.sendAllHotelViewedAudit();
        notificationUtil.sendHotelViewNotification();
        return hotels;
    }

    @Override
    public Optional<Hotel> getHotelById(String id) {
        return hotelRepository.findById(id);
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        Hotel newHotel = hotelRepository.save(hotel);
        notificationUtil.sendCreateHotelNotification(newHotel.getId(), newHotel.getName());
        notificationUtil.sendCreateHotelAudit(newHotel.getId(), newHotel.getName());
        return newHotel;
    }

    @Override
    public Optional<Hotel> updateHotel(String id, Hotel hotel) {
        notificationUtil.sendUpdateHotelNotification(id, hotel.getName());
        notificationUtil.sendUpdateHotelAudit(id, hotel.getName());
        return hotelRepository.findById(id)
                .map(existingHotel -> {
                    copyNonNullProperties(hotel, existingHotel);
                    return hotelRepository.save(existingHotel);
                });
    }

    @Override
    public boolean deleteHotel(String id) {
        notificationUtil.sendDeleteHotelNotification(id);
        notificationUtil.sendDeleteHotelAudit(id);
        return hotelRepository.findById(id).map(hotel -> {
            hotelRepository.delete(hotel);
            return true;
        }).orElse(false);
    }

    @Override
    public List<Hotel> getHotelsByCity(String city) {
        return hotelRepository.findByAddressCityIgnoreCase(city);
    }

    @Override
    public List<Hotel> getHotelsByCountry(String country) {
        return hotelRepository.findByAddressCountryIgnoreCase(country);
    }

    @Override
    public void saveAll(List<Hotel> hotels) {
        hotelRepository.saveAll(hotels);
    }

    private void copyNonNullProperties(Hotel source, Hotel target) {
        if (source.getName() != null) target.setName(source.getName());
        if (source.getDescription() != null) target.setDescription(source.getDescription());
        if (source.getEmail() != null) target.setEmail(source.getEmail());
        if (source.getPhone() != null) target.setPhone(source.getPhone());
        if (source.getWebsite() != null) target.setWebsite(source.getWebsite());
        if (source.getAddress() != null) target.setAddress(source.getAddress());
        if (source.getLocation() != null) target.setLocation(source.getLocation());
        if (source.getAmenities() != null) target.setAmenities(source.getAmenities());
        if (source.getRooms() != null) target.setRooms(source.getRooms());
        if (source.getImages() != null) target.setImages(source.getImages());
        if (source.getActive() != null) target.setActive(source.getActive());
        if (source.getRating() != null) target.setRating(source.getRating());
        if (source.getTotalReviews() != null) target.setTotalReviews(source.getTotalReviews());
    }
}
