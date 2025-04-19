package in.hotel.common_library.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Queues {
    HOTEL_NOTIFICATION("hotel_notifications", "notifications"),
    USER_NOTIFICATION("user_notifications", "notifications"),
    BOOKING_NOTIFICATION("booking_notifications", "notifications"),
    HOTEL_AUDIT("hotel_audit", "audit"),
    USER_AUDIT("user_audit", "audit"),
    BOOKING_AUDIT("booking_audit", "audit");

    private final String name;
    private final String type;

    @Override
    public String toString() {
        return this.name;
    }
}
