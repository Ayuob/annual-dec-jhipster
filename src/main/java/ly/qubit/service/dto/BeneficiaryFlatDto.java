package ly.qubit.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link ly.qubit.domain.Beneficiary}
 */
public class BeneficiaryFlatDto implements Serializable {

    private final Long idFamilyMemberId;
    private final Long idAnnualDeclarationId;

    public BeneficiaryFlatDto(Long idFamilyMemberId, Long idAnnualDeclarationId) {
        this.idFamilyMemberId = idFamilyMemberId;
        this.idAnnualDeclarationId = idAnnualDeclarationId;
    }

    public Long getIdFamilyMemberId() {
        return idFamilyMemberId;
    }

    public Long getIdAnnualDeclarationId() {
        return idAnnualDeclarationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeneficiaryFlatDto entity = (BeneficiaryFlatDto) o;
        return (
            Objects.equals(this.idFamilyMemberId, entity.idFamilyMemberId) &&
            Objects.equals(this.idAnnualDeclarationId, entity.idAnnualDeclarationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFamilyMemberId, idAnnualDeclarationId);
    }

    @Override
    public String toString() {
        return (
            getClass().getSimpleName() +
            "(" +
            "idFamilyMemberId = " +
            idFamilyMemberId +
            ", " +
            "idAnnualDeclarationId = " +
            idAnnualDeclarationId +
            ")"
        );
    }
}
