package ly.qubit.service;

import java.util.Optional;
import ly.qubit.service.dto.AnnualDeclarationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ly.qubit.domain.AnnualDeclaration}.
 */
public interface AnnualDeclarationService {
    /**
     * Save a annualDeclaration.
     *
     * @param annualDeclarationDTO the entity to save.
     * @return the persisted entity.
     */
    AnnualDeclarationDTO save(AnnualDeclarationDTO annualDeclarationDTO);

    /**
     * Updates a annualDeclaration.
     *
     * @param annualDeclarationDTO the entity to update.
     * @return the persisted entity.
     */
    AnnualDeclarationDTO update(AnnualDeclarationDTO annualDeclarationDTO);

    /**
     * Partially updates a annualDeclaration.
     *
     * @param annualDeclarationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnnualDeclarationDTO> partialUpdate(AnnualDeclarationDTO annualDeclarationDTO);

    /**
     * Get all the annualDeclarations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnnualDeclarationDTO> findAll(Pageable pageable);

    /**
     * Get all the annualDeclarations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnnualDeclarationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" annualDeclaration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnnualDeclarationDTO> findOne(Long id);

    /**
     * Delete the "id" annualDeclaration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
