package ly.qubit.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.FamilyMember;
import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.service.dto.AnnualDeclarationDTO;
import ly.qubit.service.dto.FamilyMemberDTO;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnnualDeclaration} and its DTO {@link AnnualDeclarationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnnualDeclarationMapper extends EntityMapper<AnnualDeclarationDTO, AnnualDeclaration> {
    @Mapping(target = "pensioner", source = "pensioner", qualifiedByName = "socialSecurityPensionerNationalNumber")
    @Mapping(target = "familyMembers", source = "familyMembers", qualifiedByName = "familyMemberNationalNumberSet")
    AnnualDeclarationDTO toDto(AnnualDeclaration s);

    @Mapping(target = "removeFamilyMembers", ignore = true)
    AnnualDeclaration toEntity(AnnualDeclarationDTO annualDeclarationDTO);

    @Named("socialSecurityPensionerNationalNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nationalNumber", source = "nationalNumber")
    SocialSecurityPensionerDTO toDtoSocialSecurityPensionerNationalNumber(SocialSecurityPensioner socialSecurityPensioner);

    @Named("familyMemberNationalNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nationalNumber", source = "nationalNumber")
    FamilyMemberDTO toDtoFamilyMemberNationalNumber(FamilyMember familyMember);

    @Named("familyMemberNationalNumberSet")
    default Set<FamilyMemberDTO> toDtoFamilyMemberNationalNumberSet(Set<FamilyMember> familyMember) {
        return familyMember.stream().map(this::toDtoFamilyMemberNationalNumber).collect(Collectors.toSet());
    }
}
