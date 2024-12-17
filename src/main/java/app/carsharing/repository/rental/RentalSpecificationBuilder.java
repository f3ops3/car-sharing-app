package app.carsharing.repository.rental;

import app.carsharing.dto.rental.RentalSearchParameters;
import app.carsharing.model.Rental;
import app.carsharing.repository.SpecificationBuilder;
import app.carsharing.repository.SpecificationProviderManager;
import app.carsharing.repository.rental.spec.IsActiveSpecificationProvider;
import app.carsharing.repository.rental.spec.UserIdSpecificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalSpecificationBuilder implements SpecificationBuilder<Rental> {
    private final SpecificationProviderManager<Rental> rentalSpecificationProviderManager;

    @Override
    public Specification<Rental> build(RentalSearchParameters searchParameters) {
        Specification<Rental> specification = Specification.where(null);
        if (searchParameters.getUserId() != null) {
            specification = specification.and(rentalSpecificationProviderManager
                    .getSpecificationProvider(UserIdSpecificationProvider.USER)
                    .getSpecification(searchParameters.getUserId()));
        }
        if (searchParameters.getIsActive() != null) {
            specification = specification.and(rentalSpecificationProviderManager
                    .getSpecificationProvider(IsActiveSpecificationProvider.IS_ACTIVE)
                    .getSpecification(searchParameters.getIsActive()));
        }
        return specification;
    }
}
