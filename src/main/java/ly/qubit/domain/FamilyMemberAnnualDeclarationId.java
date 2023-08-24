package ly.qubit.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class FamilyMemberAnnualDeclarationId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long familyMemberId;
    private Long annualDeclarationId;

    public FamilyMemberAnnualDeclarationId() {}

    public FamilyMemberAnnualDeclarationId(Long bookId, Long publisherId) {
        super();
        this.familyMemberId = familyMemberId;
        this.annualDeclarationId = annualDeclarationId;
    }

    public Long getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(Long familyMemberId) {
        familyMemberId = familyMemberId;
    }

    public Long getAnnualDeclarationId() {
        return annualDeclarationId;
    }

    public void setAnnualDeclarationId(Long annualDeclarationId) {
        annualDeclarationId = annualDeclarationId;
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
        FamilyMemberAnnualDeclarationId other = (FamilyMemberAnnualDeclarationId) obj;
        return (
            Objects.equals(getFamilyMemberId(), other.getFamilyMemberId()) &&
            Objects.equals(getAnnualDeclarationId(), other.getAnnualDeclarationId())
        );
    }
}
