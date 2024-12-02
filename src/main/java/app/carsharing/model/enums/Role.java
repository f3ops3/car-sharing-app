package app.carsharing.model.enums;

public enum Role {
    MANAGER("MANAGER"),
    CUSTOMER("CUSTOMER");

    private final String name;

    Role(String roleName) {
        name = roleName;
    }
}
