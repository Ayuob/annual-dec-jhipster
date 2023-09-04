package ly.qubit.service.impl;

import java.util.Optional;
import ly.qubit.domain.FamilyMember;
import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.domain.User;
import ly.qubit.repository.FamilyMemberRepository;
import ly.qubit.security.AuthoritiesConstants;
import ly.qubit.security.SecurityUtils;
import ly.qubit.service.FamilyMemberService;
import ly.qubit.service.UserService;
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
    private final UserService userService;

    private final FamilyMemberMapper familyMemberMapper;

    public FamilyMemberServiceImpl(
        FamilyMemberRepository familyMemberRepository,
        UserService userService,
        FamilyMemberMapper familyMemberMapper
    ) {
        this.familyMemberRepository = familyMemberRepository;
        this.userService = userService;
        this.familyMemberMapper = familyMemberMapper;
    }

    @Override
    public FamilyMemberDTO save(FamilyMemberDTO familyMemberDTO) {
        SocialSecurityPensioner pensioner = new SocialSecurityPensioner();
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDTO);

        if (!SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            pensioner.setUser(userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get());
            familyMember.setPensioner(pensioner);
        }
        log.debug("Request to save FamilyMember : {}", familyMemberDTO);

        if (familyMemberDTO.getNationalNumber().startsWith("1")) {
            familyMember.setGender("male");
        } else if (familyMemberDTO.getNationalNumber().startsWith("2")) {
            familyMember.setGender("female");
        }
        //todo remove Gender from Ui and apply SSN validation to Family Members
        familyMember = familyMemberRepository.save(familyMember);
        return familyMemberMapper.toDto(familyMember);
    }

    @Override
    public FamilyMemberDTO update(FamilyMemberDTO familyMemberDTO) {
        log.debug("Request to update FamilyMember : {}", familyMemberDTO);
        SocialSecurityPensioner pensioner = new SocialSecurityPensioner();
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDTO);

        if (!SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            pensioner.setUser(userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get());
            familyMember.setPensioner(pensioner);
        }
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
