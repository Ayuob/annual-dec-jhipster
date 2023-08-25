package ly.qubit.service.impl;

import java.util.Optional;
import ly.qubit.domain.Beneficiary;
import ly.qubit.domain.BeneficiaryId;
import ly.qubit.repository.BeneficiaryRepository;
import ly.qubit.service.BeneficiaryService;
import ly.qubit.service.dto.BeneficiaryDto_Empd;
import ly.qubit.service.dto.BeneficiaryIdDto;
import ly.qubit.service.mapper.BeneficiaryEmpededMapper;
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

    private final BeneficiaryEmpededMapper beneficiaryMapper;

    public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository, BeneficiaryEmpededMapper beneficiaryMapper) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.beneficiaryMapper = beneficiaryMapper;
    }

    @Override
    public BeneficiaryDto_Empd save(BeneficiaryDto_Empd beneficiaryDTO) {
        log.debug("Request to save Beneficiary : {}", beneficiaryDTO);
        Beneficiary beneficiary = beneficiaryMapper.toEntity(beneficiaryDTO);

        BeneficiaryId id = new BeneficiaryId(beneficiary.getFamilyMembers().getId(), beneficiary.getAnnualDeclaration().getId());
        beneficiary.setId(id);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDto(savedBeneficiary);
    }

    @Override
    public BeneficiaryDto_Empd update(BeneficiaryDto_Empd beneficiaryDTO) {
        log.debug("Request to update Beneficiary : {}", beneficiaryDTO);
        Beneficiary beneficiary = beneficiaryMapper.toEntity(beneficiaryDTO);
        beneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDto(beneficiary);
    }

    @Override
    public Optional<BeneficiaryDto_Empd> partialUpdate(BeneficiaryDto_Empd beneficiaryDTO) {
        log.debug("Request to partially update Beneficiary : {}", beneficiaryDTO);

        return beneficiaryRepository
            .findById(beneficiaryMapper.toEntity(beneficiaryDTO).getId())
            .map(existingBeneficiary -> {
                beneficiaryMapper.partialUpdate(existingBeneficiary, beneficiaryDTO);

                return existingBeneficiary;
            })
            .map(beneficiaryRepository::save)
            .map(beneficiaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryDto_Empd> findAll(Pageable pageable) {
        log.debug("Request to get all Beneficiaries");
        return beneficiaryRepository.findAll(pageable).map(beneficiaryMapper::toDto);
    }

    public Page<BeneficiaryDto_Empd> findAllWithEagerRelationships(Pageable pageable) {
        return beneficiaryRepository.findAllWithEagerRelationships(pageable).map(beneficiaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeneficiaryDto_Empd> findOne(BeneficiaryIdDto id) {
        log.debug("Request to get Beneficiary : {}", id);
        return beneficiaryRepository
            .findOneWithEagerRelationships(new BeneficiaryId(id.getFamilyMemberId(), id.getAnnualDeclarationId()))
            .map(beneficiaryMapper::toDto);
    }

    @Override
    public void delete(BeneficiaryIdDto id) {
        log.debug("Request to delete Beneficiary : {}", id);
        beneficiaryRepository.deleteById(new BeneficiaryId(id.getFamilyMemberId(), id.getAnnualDeclarationId()));
    }
}
