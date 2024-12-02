package app.carsharing.model.enums;

public enum CarType {
    SEDAN("SEDAN"),
    SUV("SUV"),
    HATCHBACK("HATCHBACK"),
    UNIVERSAL("UNIVERSAL");

    private final String name;

    CarType(String carType) {
        name = carType;
    }
}
