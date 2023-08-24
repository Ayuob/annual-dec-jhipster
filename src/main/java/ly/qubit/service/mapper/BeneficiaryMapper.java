package ly.qubit.service.mapper;

import ly.qubit.domain.Beneficiary;
import ly.qubit.domain.FamilyMember;
import ly.qubit.service.dto.BeneficiaryDTO;
import ly.qubit.service.dto.FamilyMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Beneficiary} and its DTO {@link BeneficiaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface BeneficiaryMapper extends EntityMapper<BeneficiaryDTO, Beneficiary> {
    @Mapping(target = "familyMembers", source = "familyMembers", qualifiedByName = "familyMemberName")
    BeneficiaryDTO toDto(Beneficiary s);

    @Named("familyMemberName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FamilyMemberDTO toDtoFamilyMemberName(FamilyMember familyMember);
}
