package ly.qubit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import ly.qubit.domain.BeneficiaryId;

/**
 * DTO for {@link BeneficiaryId}
 */
public class BeneficiaryIdDto implements Serializable {

    private final Long familyMemberId;
    private final Long annualDeclarationId;

    public BeneficiaryIdDto(Long familyMemberId, Long annualDeclarationId) {
        this.familyMemberId = familyMemberId;
        this.annualDeclarationId = annualDeclarationId;
    }

    public Long getFamilyMemberId() {
        return familyMemberId;
    }

    public Long getAnnualDeclarationId() {
        return annualDeclarationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeneficiaryIdDto entity = (BeneficiaryIdDto) o;
        return (
            Objects.equals(this.familyMemberId, entity.familyMemberId) &&
            Objects.equals(this.annualDeclarationId, entity.annualDeclarationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(familyMemberId, annualDeclarationId);
    }

    @Override
    public String toString() {
        return (
            getClass().getSimpleName() +
            "(" +
            "familyMemberId = " +
            familyMemberId +
            ", " +
            "annualDeclarationId = " +
            annualDeclarationId +
            ")"
        );
    }
}
