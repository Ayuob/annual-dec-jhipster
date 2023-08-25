package ly.qubit.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
public class BeneficiaryId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "family_member_id")
    private Long familyMemberId;

    @Column(name = "annual_declaration_id")
    private Long annualDeclarationId;

    public BeneficiaryId() {}

    public BeneficiaryId(Long familyMemberId, Long annualDeclarationId) {
        super();
        this.familyMemberId = familyMemberId;
        this.annualDeclarationId = annualDeclarationId;
    }

    public Long getFamilyMemberId() {
        return familyMemberId;
    }

    public Long getAnnualDeclarationId() {
        return annualDeclarationId;
    }

    public void setFamilyMemberId(Long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public void setAnnualDeclarationId(Long annualDeclarationId) {
        this.annualDeclarationId = annualDeclarationId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((familyMemberId == null) ? 0 : familyMemberId.hashCode());
        result = prime * result + ((annualDeclarationId == null) ? 0 : annualDeclarationId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BeneficiaryId other = (BeneficiaryId) obj;
        return (
            Objects.equals(getFamilyMemberId(), other.getFamilyMemberId()) &&
            Objects.equals(getAnnualDeclarationId(), other.getAnnualDeclarationId())
        );
    }
}
