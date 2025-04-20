package in.hotel.booking_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exchanges {
    NOTIFICATION_EXCHANGE("notification.exchange"),
    AUDIT_EXCHANGE("audit.exchange");

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }
}