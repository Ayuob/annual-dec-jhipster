package ly.qubit.service.impl;

import java.util.Optional;
import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.repository.SocialSecurityPensionerRepository;
import ly.qubit.service.SocialSecurityPensionerService;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import ly.qubit.service.mapper.SocialSecurityPensionerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SocialSecurityPensioner}.
 */
@Service
@Transactional
public class SocialSecurityPensionerServiceImpl implements SocialSecurityPensionerService {

    private final Logger log = LoggerFactory.getLogger(SocialSecurityPensionerServiceImpl.class);

    private final SocialSecurityPensionerRepository socialSecurityPensionerRepository;

    private final SocialSecurityPensionerMapper socialSecurityPensionerMapper;

    public SocialSecurityPensionerServiceImpl(
        SocialSecurityPensionerRepository socialSecurityPensionerRepository,
        SocialSecurityPensionerMapper socialSecurityPensionerMapper
    ) {
        this.socialSecurityPensionerRepository = socialSecurityPensionerRepository;
        this.socialSecurityPensionerMapper = socialSecurityPensionerMapper;
    }

    @Override
    public SocialSecurityPensionerDTO save(SocialSecurityPensionerDTO socialSecurityPensionerDTO) {
        log.debug("Request to save SocialSecurityPensioner : {}", socialSecurityPensionerDTO);
        SocialSecurityPensioner socialSecurityPensioner = socialSecurityPensionerMapper.toEntity(socialSecurityPensionerDTO);
        socialSecurityPensioner = socialSecurityPensionerRepository.save(socialSecurityPensioner);
        return socialSecurityPensionerMapper.toDto(socialSecurityPensioner);
    }

    @Override
    public SocialSecurityPensionerDTO update(SocialSecurityPensionerDTO socialSecurityPensionerDTO) {
        log.debug("Request to update SocialSecurityPensioner : {}", socialSecurityPensionerDTO);
        SocialSecurityPensioner socialSecurityPensioner = socialSecurityPensionerMapper.toEntity(socialSecurityPensionerDTO);
        socialSecurityPensioner = socialSecurityPensionerRepository.save(socialSecurityPensioner);
        return socialSecurityPensionerMapper.toDto(socialSecurityPensioner);
    }

    @Override
    public Optional<SocialSecurityPensionerDTO> partialUpdate(SocialSecurityPensionerDTO socialSecurityPensionerDTO) {
        log.debug("Request to partially update SocialSecurityPensioner : {}", socialSecurityPensionerDTO);

        return socialSecurityPensionerRepository
            .findById(socialSecurityPensionerDTO.getId())
            .map(existingSocialSecurityPensioner -> {
                socialSecurityPensionerMapper.partialUpdate(existingSocialSecurityPensioner, socialSecurityPensionerDTO);

                return existingSocialSecurityPensioner;
            })
            .map(socialSecurityPensionerRepository::save)
            .map(socialSecurityPensionerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SocialSecurityPensionerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SocialSecurityPensioners");
        return socialSecurityPensionerRepository.findAll(pageable).map(socialSecurityPensionerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SocialSecurityPensionerDTO> findOne(Long id) {
        log.debug("Request to get SocialSecurityPensioner : {}", id);
        return socialSecurityPensionerRepository.findById(id).map(socialSecurityPensionerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SocialSecurityPensioner : {}", id);
        socialSecurityPensionerRepository.deleteById(id);
    }
}
