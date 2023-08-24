package ly.qubit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import ly.qubit.domain.enumeration.DeclarationStatus;

/**
 * A DTO for the {@link ly.qubit.domain.AnnualDeclaration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnualDeclarationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate submissionDate;

    @NotNull
    private DeclarationStatus status;

    private SocialSecurityPensionerDTO pensioner;

    private Set<FamilyMemberDTO> familyMembers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public DeclarationStatus getStatus() {
        return status;
    }

    public void setStatus(DeclarationStatus status) {
        this.status = status;
    }

    public SocialSecurityPensionerDTO getPensioner() {
        return pensioner;
    }

    public void setPensioner(SocialSecurityPensionerDTO pensioner) {
        this.pensioner = pensioner;
    }

    public Set<FamilyMemberDTO> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(Set<FamilyMemberDTO> familyMembers) {
        this.familyMembers = familyMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnualDeclarationDTO)) {
            return false;
        }

        AnnualDeclarationDTO annualDeclarationDTO = (AnnualDeclarationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, annualDeclarationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnualDeclarationDTO{" +
            "id=" + getId() +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", pensioner=" + getPensioner() +
            ", familyMembers=" + getFamilyMembers() +
            "}";
    }
}
