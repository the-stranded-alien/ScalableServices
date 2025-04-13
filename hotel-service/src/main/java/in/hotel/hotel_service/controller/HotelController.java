package in.hotel.hotel_service.controller;

import in.hotel.hotel_service.model.Hotel;
import in.hotel.hotel_service.repository.HotelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotel Management", description = "APIs for managing hotel data")
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Operation(summary = "Get all hotels")
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Operation(summary = "Create a new hotel")
    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelRepository.save(hotel);
    }
}
