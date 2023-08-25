package ly.qubit.service;

import java.util.Optional;
import ly.qubit.service.dto.BeneficiaryDto_Empd;
import ly.qubit.service.dto.BeneficiaryIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link ly.qubit.domain.Beneficiary}.
 */
public interface BeneficiaryService {
    /**
     * Save a beneficiary.
     *
     * @param beneficiaryDTO the entity to save.
     * @return the persisted entity.
     */
    BeneficiaryDto_Empd save(BeneficiaryDto_Empd beneficiaryDTO);

    /**
     * Updates a beneficiary.
     *
     * @param beneficiaryDTO the entity to update.
     * @return the persisted entity.
     */
    BeneficiaryDto_Empd update(BeneficiaryDto_Empd beneficiaryDTO);

    /**
     * Partially updates a beneficiary.
     *
     * @param beneficiaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BeneficiaryDto_Empd> partialUpdate(BeneficiaryDto_Empd beneficiaryDTO);

    /**
     * Get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BeneficiaryDto_Empd> findAll(Pageable pageable);

    /**
     * Get all the beneficiaries with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BeneficiaryDto_Empd> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" beneficiary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    Optional<BeneficiaryDto_Empd> findOne(BeneficiaryIdDto id);

    void delete(BeneficiaryIdDto id);
}
