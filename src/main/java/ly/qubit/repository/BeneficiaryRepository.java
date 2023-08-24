package ly.qubit.repository;

import java.util.List;
import java.util.Optional;
import ly.qubit.domain.Beneficiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Beneficiary entity.
 */
@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    default Optional<Beneficiary> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Beneficiary> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Beneficiary> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct beneficiary from Beneficiary beneficiary left join fetch beneficiary.familyMembers left join fetch beneficiary.annualDeclaration",
        countQuery = "select count(distinct beneficiary) from Beneficiary beneficiary"
    )
    Page<Beneficiary> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct beneficiary from Beneficiary beneficiary left join fetch beneficiary.familyMembers left join fetch beneficiary.annualDeclaration"
    )
    List<Beneficiary> findAllWithToOneRelationships();

    @Query(
        "select beneficiary from Beneficiary beneficiary left join fetch beneficiary.familyMembers left join fetch beneficiary.annualDeclaration where beneficiary.id =:id"
    )
    Optional<Beneficiary> findOneWithToOneRelationships(@Param("id") Long id);
}
