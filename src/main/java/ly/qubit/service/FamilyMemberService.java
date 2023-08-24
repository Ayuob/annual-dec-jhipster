package ly.qubit.service;

import java.util.Optional;
import ly.qubit.service.dto.FamilyMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ly.qubit.domain.FamilyMember}.
 */
public interface FamilyMemberService {
    /**
     * Save a familyMember.
     *
     * @param familyMemberDTO the entity to save.
     * @return the persisted entity.
     */
    FamilyMemberDTO save(FamilyMemberDTO familyMemberDTO);

    /**
     * Updates a familyMember.
     *
     * @param familyMemberDTO the entity to update.
     * @return the persisted entity.
     */
    FamilyMemberDTO update(FamilyMemberDTO familyMemberDTO);

    /**
     * Partially updates a familyMember.
     *
     * @param familyMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FamilyMemberDTO> partialUpdate(FamilyMemberDTO familyMemberDTO);

    /**
     * Get all the familyMembers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FamilyMemberDTO> findAll(Pageable pageable);

    /**
     * Get all the familyMembers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FamilyMemberDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" familyMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FamilyMemberDTO> findOne(Long id);

    /**
     * Delete the "id" familyMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
