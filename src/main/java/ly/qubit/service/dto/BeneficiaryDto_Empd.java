package ly.qubit.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import ly.qubit.domain.enumeration.EntitlementType;

/**
 * DTO for {@link ly.qubit.domain.Beneficiary}
 */
public class BeneficiaryDto_Empd implements Serializable {

    private BeneficiaryIdDto id;

    @NotNull
    private EntitlementType entitlementType;

    private String entitlementDetails;

    private FamilyMemberDTO familyMembers;

    private AnnualDeclarationDTO annualDeclaration;

    public BeneficiaryDto_Empd() {}

    public BeneficiaryDto_Empd(BeneficiaryIdDto id) {
        this.id = id;
    }

    public BeneficiaryIdDto getId() {
        return id;
    }

    public void setId(BeneficiaryIdDto id) {
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

    public AnnualDeclarationDTO getAnnualDeclaration() {
        return annualDeclaration;
    }

    public void setAnnualDeclaration(AnnualDeclarationDTO annualDeclaration) {
        this.annualDeclaration = annualDeclaration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeneficiaryDto_Empd entity = (BeneficiaryDto_Empd) o;
        return Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + id + ")";
    }
}
