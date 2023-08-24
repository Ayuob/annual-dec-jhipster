package ly.qubit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import ly.qubit.domain.enumeration.EntitlementType;

/**
 * A DTO for the {@link ly.qubit.domain.Beneficiary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BeneficiaryDTO implements Serializable {

    private Long id;

    @NotNull
    private EntitlementType entitlementType;

    private String entitlementDetails;

    private FamilyMemberDTO familyMembers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntitlementType getEntitlementType() {
        return entitlementType;
    }

    public void setEntitlementType(EntitlementType entitlementType) {
        this.entitlementType = entitlementType;
    }

    public String getEntitlementDetails() {
        return entitlementDetails;
    }

    public void setEntitlementDetails(String entitlementDetails) {
        this.entitlementDetails = entitlementDetails;
    }

    public FamilyMemberDTO getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(FamilyMemberDTO familyMembers) {
        this.familyMembers = familyMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeneficiaryDTO)) {
            return false;
        }

        BeneficiaryDTO beneficiaryDTO = (BeneficiaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, beneficiaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeneficiaryDTO{" +
            "id=" + getId() +
            ", entitlementType='" + getEntitlementType() + "'" +
            ", entitlementDetails='" + getEntitlementDetails() + "'" +
            ", familyMembers=" + getFamilyMembers() +
            "}";
    }
}
