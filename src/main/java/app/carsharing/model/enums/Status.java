package app.carsharing.model.enums;

public enum Status {
    PENDING("PENDING"),
    PAID("PAID");

    private final String name;

    Status(String statusName) {
        name = statusName;
    }
}
