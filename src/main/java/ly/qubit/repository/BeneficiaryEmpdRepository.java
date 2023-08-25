package ly.qubit.repository;

import java.util.List;
import java.util.Optional;
import ly.qubit.domain.Beneficiary;
import ly.qubit.domain.BeneficiaryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BeneficiaryEmpdRepository extends JpaRepository<Beneficiary, BeneficiaryId> {
    default Optional<Beneficiary> findOneWithEagerRelationships(BeneficiaryId id) {
        return this.findOneWithToOneRelationships(id.getAnnualDeclarationId(), id.getFamilyMemberId());
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
        "select beneficiary from Beneficiary beneficiary left join fetch beneficiary.familyMembers left join fetch beneficiary.annualDeclaration where beneficiary.annualDeclaration.id =:adId and beneficiary.familyMembers.id =:fmId"
    )
    Optional<Beneficiary> findOneWithToOneRelationships(@Param("adId") long annualDeclarationId, @Param("fmId") long familyMemberId);
}
