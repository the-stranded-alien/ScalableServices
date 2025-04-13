package in.hotel.hotel_service.repository;

import in.hotel.hotel_service.model.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotelRepository extends MongoRepository<Hotel, String> { }
