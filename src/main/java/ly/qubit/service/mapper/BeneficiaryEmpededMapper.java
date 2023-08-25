package ly.qubit.service.mapper;

import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.Beneficiary;
import ly.qubit.domain.FamilyMember;
import ly.qubit.service.dto.AnnualDeclarationDTO;
import ly.qubit.service.dto.BeneficiaryDTO;
import ly.qubit.service.dto.BeneficiaryDto_Empd;
import ly.qubit.service.dto.FamilyMemberDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeneficiaryEmpededMapper extends EntityMapper<BeneficiaryDto_Empd, Beneficiary> {
    @Mapping(target = "familyMembers", source = "familyMembers", qualifiedByName = "familyMemberName")
    @Mapping(target = "annualDeclaration", source = "annualDeclaration", qualifiedByName = "annualDeclarationSubmissionDate")
    @Mapping(target = "id", source = "id", qualifiedByName = "annualDeclarationSubmissionDate")
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
}
