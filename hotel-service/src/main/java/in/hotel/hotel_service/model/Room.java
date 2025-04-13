package in.hotel.hotel_service.model;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    private String roomType;
    private String description;
    private int capacity;
    private double pricePerNight;
    private boolean available;
    private List<String> amenities;
    private List<String> images;
}
