package ly.qubit.repository;

import java.util.List;
import java.util.Optional;
import ly.qubit.domain.AnnualDeclaration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AnnualDeclaration entity.
 *
 * When extending this class, extend AnnualDeclarationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AnnualDeclarationRepository
    extends AnnualDeclarationRepositoryWithBagRelationships, JpaRepository<AnnualDeclaration, Long> {
    default Optional<AnnualDeclaration> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<AnnualDeclaration> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<AnnualDeclaration> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.pensioner",
        countQuery = "select count(distinct annualDeclaration) from AnnualDeclaration annualDeclaration"
    )
    Page<AnnualDeclaration> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.pensioner")
    List<AnnualDeclaration> findAllWithToOneRelationships();

    @Query(
        "select annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.pensioner where annualDeclaration.id =:id"
    )
    Optional<AnnualDeclaration> findOneWithToOneRelationships(@Param("id") Long id);
}
