package in.hotel.hotel_service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoLocation {
    private double latitude;
    private double longitude;
}
