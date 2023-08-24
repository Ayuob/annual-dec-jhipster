package ly.qubit.repository;

import java.util.List;
import java.util.Optional;
import ly.qubit.domain.AnnualDeclaration;
import org.springframework.data.domain.Page;

public interface AnnualDeclarationRepositoryWithBagRelationships {
    Optional<AnnualDeclaration> fetchBagRelationships(Optional<AnnualDeclaration> annualDeclaration);

    List<AnnualDeclaration> fetchBagRelationships(List<AnnualDeclaration> annualDeclarations);

    Page<AnnualDeclaration> fetchBagRelationships(Page<AnnualDeclaration> annualDeclarations);
}
