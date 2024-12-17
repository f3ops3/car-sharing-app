package app.carsharing.repository.rental.spec;

import app.carsharing.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsActiveSpecificationProvider implements SpecificationProvider {
    public static final String IS_ACTIVE = "isActive";

    @Override
    public String getKey() {
        return IS_ACTIVE;
    }

    @Override
    public Specification getSpecification(String param) {
        return (root, query, criteriaBuilder) -> {
            if (param.equals("true")) {
                return criteriaBuilder.isNull(root.get("actualReturnDate"));
            } else if (param.equals("false")) {
                return criteriaBuilder.isNotNull(root.get("actualReturnDate"));
            } else {
                throw new IllegalArgumentException(
                        "Params should contain true or false, but contains: " + param);
            }
        };
    }
}
