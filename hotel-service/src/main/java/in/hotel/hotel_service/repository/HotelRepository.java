package in.hotel.hotel_service.repository;

import in.hotel.hotel_service.model.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HotelRepository extends MongoRepository<Hotel, String> {
    List<Hotel> findByAddressCityIgnoreCase(String city);
    List<Hotel> findByAddressCountryIgnoreCase(String country);
}
