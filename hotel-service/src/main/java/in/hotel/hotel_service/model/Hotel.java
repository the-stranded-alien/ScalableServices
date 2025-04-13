package in.hotel.hotel_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("hotels")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Hotel {
    @Id
    private String id;

    private String name;
    private String description;
    private String email;
    private String phone;
    private String website;

    private Address address;
    private GeoLocation location;

    private List<String> amenities;
    private List<Room> rooms;
    private List<String> images;
    private Boolean active;
    private Double rating;
    private Integer totalReviews;
}
