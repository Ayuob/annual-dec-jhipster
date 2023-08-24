package ly.qubit.service.impl;

import java.util.Optional;
import ly.qubit.domain.FamilyMember;
import ly.qubit.repository.FamilyMemberRepository;
import ly.qubit.service.FamilyMemberService;
import ly.qubit.service.dto.FamilyMemberDTO;
import ly.qubit.service.mapper.FamilyMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FamilyMember}.
 */
@Service
@Transactional
public class FamilyMemberServiceImpl implements FamilyMemberService {

    private final Logger log = LoggerFactory.getLogger(FamilyMemberServiceImpl.class);

    private final FamilyMemberRepository familyMemberRepository;

    private final FamilyMemberMapper familyMemberMapper;

    public FamilyMemberServiceImpl(FamilyMemberRepository familyMemberRepository, FamilyMemberMapper familyMemberMapper) {
        this.familyMemberRepository = familyMemberRepository;
        this.familyMemberMapper = familyMemberMapper;
    }

    @Override
    public FamilyMemberDTO save(FamilyMemberDTO familyMemberDTO) {
        log.debug("Request to save FamilyMember : {}", familyMemberDTO);
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDTO);
        familyMember = familyMemberRepository.save(familyMember);
        return familyMemberMapper.toDto(familyMember);
    }

    @Override
    public FamilyMemberDTO update(FamilyMemberDTO familyMemberDTO) {
        log.debug("Request to update FamilyMember : {}", familyMemberDTO);
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDTO);
        familyMember = familyMemberRepository.save(familyMember);
        return familyMemberMapper.toDto(familyMember);
    }

    @Override
    public Optional<FamilyMemberDTO> partialUpdate(FamilyMemberDTO familyMemberDTO) {
        log.debug("Request to partially update FamilyMember : {}", familyMemberDTO);

        return familyMemberRepository
            .findById(familyMemberDTO.getId())
            .map(existingFamilyMember -> {
                familyMemberMapper.partialUpdate(existingFamilyMember, familyMemberDTO);

                return existingFamilyMember;
            })
            .map(familyMemberRepository::save)
            .map(familyMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FamilyMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FamilyMembers");
        return familyMemberRepository.findAll(pageable).map(familyMemberMapper::toDto);
    }

    public Page<FamilyMemberDTO> findAllWithEagerRelationships(Pageable pageable) {
        return familyMemberRepository.findAllWithEagerRelationships(pageable).map(familyMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FamilyMemberDTO> findOne(Long id) {
        log.debug("Request to get FamilyMember : {}", id);
        return familyMemberRepository.findOneWithEagerRelationships(id).map(familyMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FamilyMember : {}", id);
        familyMemberRepository.deleteById(id);
    }
}
