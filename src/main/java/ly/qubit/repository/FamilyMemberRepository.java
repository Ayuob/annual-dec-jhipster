package ly.qubit.repository;

import java.util.List;
import java.util.Optional;
import ly.qubit.domain.FamilyMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FamilyMember entity.
 */
@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    default Optional<FamilyMember> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FamilyMember> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FamilyMember> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct familyMember from FamilyMember familyMember left join fetch familyMember.pensioner",
        countQuery = "select count(distinct familyMember) from FamilyMember familyMember"
    )
    Page<FamilyMember> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct familyMember from FamilyMember familyMember left join fetch familyMember.pensioner")
    List<FamilyMember> findAllWithToOneRelationships();

    @Query("select familyMember from FamilyMember familyMember left join fetch familyMember.pensioner where familyMember.id =:id")
    Optional<FamilyMember> findOneWithToOneRelationships(@Param("id") Long id);
}
