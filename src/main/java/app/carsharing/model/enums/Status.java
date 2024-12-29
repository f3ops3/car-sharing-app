package app.carsharing.model.enums;

public enum Status {
    PENDING("PENDING"),
    PAID("PAID"),
    EXPIRED("EXPIRED"),
    CANCELED("CANCELED");

    private final String name;

    Status(String statusName) {
        name = statusName;
    }
}
