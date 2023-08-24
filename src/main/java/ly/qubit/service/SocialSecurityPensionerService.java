package ly.qubit.service;

import java.util.Optional;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ly.qubit.domain.SocialSecurityPensioner}.
 */
public interface SocialSecurityPensionerService {
    /**
     * Save a socialSecurityPensioner.
     *
     * @param socialSecurityPensionerDTO the entity to save.
     * @return the persisted entity.
     */
    SocialSecurityPensionerDTO save(SocialSecurityPensionerDTO socialSecurityPensionerDTO);

    /**
     * Updates a socialSecurityPensioner.
     *
     * @param socialSecurityPensionerDTO the entity to update.
     * @return the persisted entity.
     */
    SocialSecurityPensionerDTO update(SocialSecurityPensionerDTO socialSecurityPensionerDTO);

    /**
     * Partially updates a socialSecurityPensioner.
     *
     * @param socialSecurityPensionerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SocialSecurityPensionerDTO> partialUpdate(SocialSecurityPensionerDTO socialSecurityPensionerDTO);

    /**
     * Get all the socialSecurityPensioners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SocialSecurityPensionerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" socialSecurityPensioner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SocialSecurityPensionerDTO> findOne(Long id);

    /**
     * Delete the "id" socialSecurityPensioner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
