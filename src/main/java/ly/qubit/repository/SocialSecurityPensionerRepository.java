package ly.qubit.repository;

import ly.qubit.domain.SocialSecurityPensioner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SocialSecurityPensioner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialSecurityPensionerRepository extends JpaRepository<SocialSecurityPensioner, Long> {}
