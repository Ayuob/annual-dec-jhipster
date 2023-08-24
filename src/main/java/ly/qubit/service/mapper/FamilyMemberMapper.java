package ly.qubit.service.mapper;

import ly.qubit.domain.FamilyMember;
import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.service.dto.FamilyMemberDTO;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FamilyMember} and its DTO {@link FamilyMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface FamilyMemberMapper extends EntityMapper<FamilyMemberDTO, FamilyMember> {
    @Mapping(target = "pensioner", source = "pensioner", qualifiedByName = "socialSecurityPensionerNationalNumber")
    FamilyMemberDTO toDto(FamilyMember s);

    @Named("socialSecurityPensionerNationalNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nationalNumber", source = "nationalNumber")
    SocialSecurityPensionerDTO toDtoSocialSecurityPensionerNationalNumber(SocialSecurityPensioner socialSecurityPensioner);
}
