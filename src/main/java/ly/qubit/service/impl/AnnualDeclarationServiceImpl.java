package ly.qubit.service.impl;

import java.util.Optional;
import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.repository.AnnualDeclarationRepository;
import ly.qubit.repository.SocialSecurityPensionerRepository;
import ly.qubit.security.AuthoritiesConstants;
import ly.qubit.security.SecurityUtils;
import ly.qubit.service.AnnualDeclarationService;
import ly.qubit.service.dto.AnnualDeclarationDTO;
import ly.qubit.service.mapper.AnnualDeclarationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AnnualDeclaration}.
 */
@Service
@Transactional
public class AnnualDeclarationServiceImpl implements AnnualDeclarationService {

    private final Logger log = LoggerFactory.getLogger(AnnualDeclarationServiceImpl.class);

    private final AnnualDeclarationRepository annualDeclarationRepository;

    private final AnnualDeclarationMapper annualDeclarationMapper;
    private final SocialSecurityPensionerRepository pensionerRepository;

    public AnnualDeclarationServiceImpl(
        AnnualDeclarationRepository annualDeclarationRepository,
        AnnualDeclarationMapper annualDeclarationMapper,
        SocialSecurityPensionerServiceImpl pensionerService,
        SocialSecurityPensionerRepository pensionerRepository
    ) {
        this.annualDeclarationRepository = annualDeclarationRepository;
        this.annualDeclarationMapper = annualDeclarationMapper;
        this.pensionerRepository = pensionerRepository;
    }

    @Override
    public AnnualDeclarationDTO save(AnnualDeclarationDTO annualDeclarationDTO) {
        log.debug("Request to save AnnualDeclaration : {}", annualDeclarationDTO);
        SocialSecurityPensioner pensioner = new SocialSecurityPensioner();
        AnnualDeclaration annualDeclaration = annualDeclarationMapper.toEntity(annualDeclarationDTO);

        if (!SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            annualDeclaration.setPensioner(pensionerRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get());

            annualDeclaration = annualDeclarationRepository.save(annualDeclaration);
        }
        annualDeclaration = annualDeclarationRepository.save(annualDeclaration);
        return annualDeclarationMapper.toDto(annualDeclaration);
    }

    @Override
    public AnnualDeclarationDTO update(AnnualDeclarationDTO annualDeclarationDTO) {
        log.debug("Request to update AnnualDeclaration : {}", annualDeclarationDTO);
        AnnualDeclaration annualDeclaration = annualDeclarationMapper.toEntity(annualDeclarationDTO);
        annualDeclaration = annualDeclarationRepository.save(annualDeclaration);
        return annualDeclarationMapper.toDto(annualDeclaration);
    }

    @Override
    public Optional<AnnualDeclarationDTO> partialUpdate(AnnualDeclarationDTO annualDeclarationDTO) {
        log.debug("Request to partially update AnnualDeclaration : {}", annualDeclarationDTO);

        return annualDeclarationRepository
            .findById(annualDeclarationDTO.getId())
            .map(existingAnnualDeclaration -> {
                annualDeclarationMapper.partialUpdate(existingAnnualDeclaration, annualDeclarationDTO);

                return existingAnnualDeclaration;
            })
            .map(annualDeclarationRepository::save)
            .map(annualDeclarationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnualDeclarationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnnualDeclarations");
        return annualDeclarationRepository.findAll(pageable).map(annualDeclarationMapper::toDto);
    }

    public Page<AnnualDeclarationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return annualDeclarationRepository.findAllWithEagerRelationships(pageable).map(annualDeclarationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnnualDeclarationDTO> findOne(Long id) {
        log.debug("Request to get AnnualDeclaration : {}", id);
        return annualDeclarationRepository.findOneWithEagerRelationships(id).map(annualDeclarationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnnualDeclaration : {}", id);
        annualDeclarationRepository.deleteById(id);
    }
}
