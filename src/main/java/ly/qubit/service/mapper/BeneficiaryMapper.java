package ly.qubit.service.mapper;

import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.Beneficiary;
import ly.qubit.domain.BeneficiaryId;
import ly.qubit.domain.FamilyMember;
import ly.qubit.service.dto.*;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Beneficiary} and its DTO {@link BeneficiaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface BeneficiaryMapper extends EntityMapper<BeneficiaryDto_Empd, Beneficiary> {
    @Mapping(target = "familyMembers", source = "familyMembers", qualifiedByName = "familyMemberName")
    @Mapping(target = "annualDeclaration", source = "annualDeclaration", qualifiedByName = "annualDeclarationSubmissionDate")
    @Mapping(target = "id", source = "id", qualifiedByName = "id")
    BeneficiaryDto_Empd toDto(Beneficiary s);

    @Named("familyMemberName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FamilyMemberDTO toDtoFamilyMemberName(FamilyMember familyMember);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "familyMemberId", source = "familyMemberId")
    @Mapping(target = "annualDeclarationId", source = "annualDeclarationId")
    BeneficiaryIdDto toDtoId(BeneficiaryId id);

    @Named("annualDeclarationSubmissionDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "submissionDate", source = "submissionDate")
    AnnualDeclarationDTO toDtoAnnualDeclarationSubmissionDate(AnnualDeclaration annualDeclaration);
}
