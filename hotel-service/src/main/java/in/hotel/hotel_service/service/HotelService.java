package in.hotel.hotel_service.service;

import in.hotel.hotel_service.model.Hotel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    List<Hotel> getAllHotels();
    Optional<Hotel> getHotelById(String id);
    Hotel createHotel(Hotel hotel);
    Optional<Hotel> updateHotel(String id, Hotel hotel);
    boolean deleteHotel(String id);
    List<Hotel> getHotelsByCity(String city);
    List<Hotel> getHotelsByCountry(String country);
    void saveAll(List<Hotel> hotels);
}
