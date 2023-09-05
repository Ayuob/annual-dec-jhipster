package ly.qubit.repository;

import java.util.List;
import java.util.Optional;
import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.SocialSecurityPensioner;
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

    @Query(
        value = "select distinct annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.pensioner where annualDeclaration.pensioner.login =: login ",
        countQuery = "select count(distinct annualDeclaration) from AnnualDeclaration annualDeclaration where annualDeclaration.pensioner.login =: login "
    )
    Page<AnnualDeclaration> findAllByLoginWithToOneRelationships(Pageable pageable, @Param("login") String login);

    @Query(
        value = "select distinct annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.pensioner p where p = :pensioner",
        countQuery = "select count(distinct annualDeclaration) from AnnualDeclaration annualDeclaration where annualDeclaration.pensioner = :pensioner"
    )
    Page<AnnualDeclaration> findAllByPensionerWithToOneRelationships(
        Pageable pageable,
        @Param("pensioner") SocialSecurityPensioner pensioner
    );

    @Query("select distinct annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.pensioner")
    List<AnnualDeclaration> findAllWithToOneRelationships();

    Page<AnnualDeclaration> findAllByPensionerLogin(Pageable pageable, String login);

    @Query(
        "select annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.pensioner where annualDeclaration.id =:id"
    )
    Optional<AnnualDeclaration> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "SELECT annualDeclaration FROM AnnualDeclaration annualDeclaration " +
        "LEFT JOIN FETCH annualDeclaration.pensioner " +
        "WHERE annualDeclaration.pensioner = :pensioner " +
        "ORDER BY annualDeclaration.submissionDate DESC"
    )
    Optional<AnnualDeclaration> findLastAnnualDeclarationByPensionerAndSubmissionDate(
        @Param("pensioner") SocialSecurityPensioner pensioner
    );
}
