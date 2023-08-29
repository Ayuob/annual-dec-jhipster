package ly.qubit.service.impl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import ly.qubit.domain.Authority;
import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.domain.User;
import ly.qubit.repository.AuthorityRepository;
import ly.qubit.repository.SocialSecurityPensionerRepository;
import ly.qubit.repository.UserRepository;
import ly.qubit.security.AuthoritiesConstants;
import ly.qubit.security.SecurityUtils;
import ly.qubit.service.SocialSecurityPensionerService;
import ly.qubit.service.UserService;
import ly.qubit.service.dto.AdminUserDTO;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import ly.qubit.service.mapper.SocialSecurityPensionerMapper;
import ly.qubit.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service Implementation for managing {@link SocialSecurityPensioner}.
 */
@Service
@Transactional
public class SocialSecurityPensionerServiceImpl implements SocialSecurityPensionerService {

    private final Logger log = LoggerFactory.getLogger(SocialSecurityPensionerServiceImpl.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final SocialSecurityPensionerRepository socialSecurityPensionerRepository;

    private final SocialSecurityPensionerMapper socialSecurityPensionerMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public SocialSecurityPensionerServiceImpl(
        UserRepository userRepository,
        UserService userService,
        SocialSecurityPensionerRepository socialSecurityPensionerRepository,
        SocialSecurityPensionerMapper socialSecurityPensionerMapper,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.socialSecurityPensionerRepository = socialSecurityPensionerRepository;
        this.socialSecurityPensionerMapper = socialSecurityPensionerMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public SocialSecurityPensionerDTO save(SocialSecurityPensionerDTO socialSecurityPensionerDTO) {
        log.debug("Request to save SocialSecurityPensioner : {}", socialSecurityPensionerDTO);
        SocialSecurityPensioner socialSecurityPensioner = socialSecurityPensionerMapper.toEntity(socialSecurityPensionerDTO);
        socialSecurityPensioner = socialSecurityPensionerRepository.save(socialSecurityPensioner);
        return socialSecurityPensionerMapper.toDto(socialSecurityPensioner);
    }

    public SocialSecurityPensioner savePensioner(ManagedUserVM managedUserVM, String Password) {
        SocialSecurityPensioner pensioner = new SocialSecurityPensioner();
        pensioner.setEmail(managedUserVM.getEmail());
        pensioner.setLogin(managedUserVM.getLogin());
        pensioner.setLangKey(managedUserVM.getLangKey());
        pensioner.setFirstName(managedUserVM.getFirstName());
        pensioner.setLastName(managedUserVM.getLastName());
        String encryptedPassword = passwordEncoder.encode(managedUserVM.getPassword());
        pensioner.setPassword(encryptedPassword);
        pensioner.setNationalNumber(managedUserVM.getLogin());
        pensioner.setAddress("any");

        // new user is not active
        pensioner.setActivated(false);
        // new user gets registration key
        pensioner.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        pensioner.setAuthorities(authorities);
        socialSecurityPensionerRepository.save(pensioner);
        return pensioner;
    }

    @Override
    public SocialSecurityPensionerDTO update(SocialSecurityPensionerDTO socialSecurityPensionerDTO) {
        log.debug("Request to update SocialSecurityPensioner : {}", socialSecurityPensionerDTO);
        SocialSecurityPensioner socialSecurityPensioner = socialSecurityPensionerMapper.toEntity(socialSecurityPensionerDTO);
        socialSecurityPensioner = socialSecurityPensionerRepository.save(socialSecurityPensioner);
        return socialSecurityPensionerMapper.toDto(socialSecurityPensioner);
    }

    public void updateUser(AdminUserDTO userDTO) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                socialSecurityPensionerRepository
                    .findById(user.getId())
                    .ifPresent(pen -> {
                        pen.setNationalNumber(userDTO.getNationalNumber());
                        pen.setPensionNumber(userDTO.getPensionNumber());
                        pen.setAddress(userDTO.getAddress());
                        pen.setLogin(userDTO.getLogin());
                        pen.setFirstName(userDTO.getFirstName());
                        pen.setLastName(userDTO.getLastName());
                        if (userDTO.getEmail() != null) {
                            pen.setEmail(userDTO.getEmail().toLowerCase());
                        }
                        pen.setLangKey(userDTO.getLangKey());
                        pen.setImageUrl(userDTO.getImageUrl());
                    });

                log.debug("Changed Information for User: {}", user);
            });
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
