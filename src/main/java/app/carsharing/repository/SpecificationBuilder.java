package app.carsharing.repository;

import app.carsharing.dto.rental.RentalSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(RentalSearchParameters rentalSearchParameters);
}
