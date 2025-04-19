package in.hotel.common_library.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exchanges {
    NOTIFICATION_EXCHANGE("notification_exchange"),
    AUDIT_EXCHANGE("audit_exchange");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
