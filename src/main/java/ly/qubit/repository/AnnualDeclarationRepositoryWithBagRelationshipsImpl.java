package ly.qubit.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ly.qubit.domain.AnnualDeclaration;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AnnualDeclarationRepositoryWithBagRelationshipsImpl implements AnnualDeclarationRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AnnualDeclaration> fetchBagRelationships(Optional<AnnualDeclaration> annualDeclaration) {
        return annualDeclaration.map(this::fetchFamilyMembers);
    }

    @Override
    public Page<AnnualDeclaration> fetchBagRelationships(Page<AnnualDeclaration> annualDeclarations) {
        return new PageImpl<>(
            fetchBagRelationships(annualDeclarations.getContent()),
            annualDeclarations.getPageable(),
            annualDeclarations.getTotalElements()
        );
    }

    @Override
    public List<AnnualDeclaration> fetchBagRelationships(List<AnnualDeclaration> annualDeclarations) {
        return Optional.of(annualDeclarations).map(this::fetchFamilyMembers).orElse(Collections.emptyList());
    }

    AnnualDeclaration fetchFamilyMembers(AnnualDeclaration result) {
        return entityManager
            .createQuery(
                "select annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.familyMembers where annualDeclaration is :annualDeclaration",
                AnnualDeclaration.class
            )
            .setParameter("annualDeclaration", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<AnnualDeclaration> fetchFamilyMembers(List<AnnualDeclaration> annualDeclarations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, annualDeclarations.size()).forEach(index -> order.put(annualDeclarations.get(index).getId(), index));
        List<AnnualDeclaration> result = entityManager
            .createQuery(
                "select distinct annualDeclaration from AnnualDeclaration annualDeclaration left join fetch annualDeclaration.familyMembers where annualDeclaration in :annualDeclarations",
                AnnualDeclaration.class
            )
            .setParameter("annualDeclarations", annualDeclarations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
