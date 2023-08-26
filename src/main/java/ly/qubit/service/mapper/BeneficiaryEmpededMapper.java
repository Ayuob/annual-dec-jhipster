package ly.qubit.service.mapper;

import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.Beneficiary;
import ly.qubit.domain.BeneficiaryId;
import ly.qubit.domain.FamilyMember;
import ly.qubit.service.dto.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeneficiaryEmpededMapper extends EntityMapper<BeneficiaryDto_Empd, Beneficiary> {
    @Mapping(target = "familyMembers", source = "familyMembers", qualifiedByName = "familyMemberName")
    @Mapping(target = "annualDeclaration", source = "annualDeclaration", qualifiedByName = "annualDeclarationSubmissionDate")
    @Mapping(target = "id", source = "id", qualifiedByName = "beneficiaryId")
    BeneficiaryDto_Empd toDto(Beneficiary s);

    @Named("familyMemberName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FamilyMemberDTO toDtoFamilyMemberName(FamilyMember familyMember);

    @Named("annualDeclarationSubmissionDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "submissionDate", source = "submissionDate")
    AnnualDeclarationDTO toDtoAnnualDeclarationSubmissionDate(AnnualDeclaration annualDeclaration);

    @Named("beneficiaryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "familyMemberId", source = "familyMemberId")
    @Mapping(target = "annualDeclarationId", source = "annualDeclarationId")
    BeneficiaryIdDto toDtoBeneficiaryId(BeneficiaryId beneficiaryId);
}
