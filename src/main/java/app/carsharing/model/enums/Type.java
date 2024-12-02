package app.carsharing.model.enums;

public enum Type {
    PAYMENT("PAYMENT"),
    FINE("FINE");

    private final String name;

    Type(String type) {
        name = type;
    }
}
