package ly.qubit.service.impl;

import java.util.Optional;
import ly.qubit.domain.Beneficiary;
import ly.qubit.repository.BeneficiaryRepository;
import ly.qubit.repository.FamilyMemberRepository;
import ly.qubit.service.BeneficiaryService;
import ly.qubit.service.dto.BeneficiaryDTO;
import ly.qubit.service.mapper.BeneficiaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Beneficiary}.
 */
@Service
@Transactional
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final Logger log = LoggerFactory.getLogger(BeneficiaryServiceImpl.class);

    private final BeneficiaryRepository beneficiaryRepository;

    private final BeneficiaryMapper beneficiaryMapper;

    private final FamilyMemberRepository familyMemberRepository;

    public BeneficiaryServiceImpl(
        BeneficiaryRepository beneficiaryRepository,
        BeneficiaryMapper beneficiaryMapper,
        FamilyMemberRepository familyMemberRepository
    ) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.beneficiaryMapper = beneficiaryMapper;
        this.familyMemberRepository = familyMemberRepository;
    }

    @Override
    public BeneficiaryDTO save(BeneficiaryDTO beneficiaryDTO) {
        log.debug("Request to save Beneficiary : {}", beneficiaryDTO);
        Beneficiary beneficiary = beneficiaryMapper.toEntity(beneficiaryDTO);
        Long familyMemberId = beneficiaryDTO.getFamilyMembers().getId();
        familyMemberRepository.findById(familyMemberId).ifPresent(beneficiary::familyMembers);
        beneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDto(beneficiary);
    }

    @Override
    public BeneficiaryDTO update(BeneficiaryDTO beneficiaryDTO) {
        log.debug("Request to update Beneficiary : {}", beneficiaryDTO);
        Beneficiary beneficiary = beneficiaryMapper.toEntity(beneficiaryDTO);
        Long familyMemberId = beneficiaryDTO.getFamilyMembers().getId();
        familyMemberRepository.findById(familyMemberId).ifPresent(beneficiary::familyMembers);
        beneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDto(beneficiary);
    }

    @Override
    public Optional<BeneficiaryDTO> partialUpdate(BeneficiaryDTO beneficiaryDTO) {
        log.debug("Request to partially update Beneficiary : {}", beneficiaryDTO);

        return beneficiaryRepository
            .findById(beneficiaryDTO.getId())
            .map(existingBeneficiary -> {
                beneficiaryMapper.partialUpdate(existingBeneficiary, beneficiaryDTO);

                return existingBeneficiary;
            })
            .map(beneficiaryRepository::save)
            .map(beneficiaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Beneficiaries");
        return beneficiaryRepository.findAll(pageable).map(beneficiaryMapper::toDto);
    }

    public Page<BeneficiaryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return beneficiaryRepository.findAllWithEagerRelationships(pageable).map(beneficiaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeneficiaryDTO> findOne(Long id) {
        log.debug("Request to get Beneficiary : {}", id);
        return beneficiaryRepository.findOneWithEagerRelationships(id).map(beneficiaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Beneficiary : {}", id);
        beneficiaryRepository.deleteById(id);
    }
}
