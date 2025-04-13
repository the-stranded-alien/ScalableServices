package in.hotel.hotel_service.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.hotel.hotel_service.model.Hotel;
import in.hotel.hotel_service.model.Room;
import in.hotel.hotel_service.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotel Management", description = "APIs for managing hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(summary = "Get all hotels")
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Get hotel by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable String id) {
        Optional<Hotel> hotel = hotelService.getHotelById(id);
        return hotel
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new hotel")
    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }

    @Operation(summary = "Update a hotel by Id")
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable String id, @RequestBody Hotel hotel) {
        return hotelService.updateHotel(id, hotel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get Hotels by city")
    @GetMapping("/by-city")
    public ResponseEntity<List<Hotel>> getHotelsByCity(@RequestParam String city) {
        List<Hotel> hotels = hotelService.getHotelsByCity(city);
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Get Hotels by country")
    @GetMapping("/by-country")
    public ResponseEntity<List<Hotel>> getHotelsByCountry(@RequestParam String country) {
        List<Hotel> hotels = hotelService.getHotelsByCountry(country);
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Get Rooms in a hotel")
    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<Room>> getRooms(@PathVariable String id) {
        return hotelService.getHotelById(id)
                .map(hotel -> ResponseEntity.ok(hotel.getRooms()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get available rooms in a hotel")
    @GetMapping("/{id}/rooms/available")
    public ResponseEntity<List<Room>> getAvailableRooms(@PathVariable String id) {
        return hotelService.getHotelById(id)
                .map(hotel -> {
                    List<Room> availableRooms = hotel.getRooms()
                            .stream()
                            .filter(Room::isAvailable)
                            .toList();
                    return ResponseEntity.ok(availableRooms);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get count of available rooms in a hotel")
    @GetMapping("/{id}/rooms/available/count")
    public ResponseEntity<Integer> getAvailableRoomsCount(@PathVariable String id) {
        return hotelService.getHotelById(id)
                .map(hotel -> {
                    long count = hotel.getRooms()
                            .stream()
                            .filter(Room::isAvailable)
                            .count();
                    return ResponseEntity.ok((int) count);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a hotel by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String id) {
        if (hotelService.deleteHotel(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Load dummy hotel data from JSON")
    @GetMapping("/load")
    public ResponseEntity<String> loadHotels() {
        try {
            ClassPathResource resource = new ClassPathResource("static/hotels.json");
            InputStream inputStream = resource.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            List<Hotel> hotels = mapper.readValue(inputStream, new TypeReference<>() {});
            hotelService.saveAll(hotels);
            return ResponseEntity.ok("Dummy Hotel data loaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading JSON: " + e.getMessage());
        }
    }
}
