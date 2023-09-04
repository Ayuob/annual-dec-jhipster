package ly.qubit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ly.qubit.domain.FamilyMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FamilyMemberDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 12, max = 12)
    private String nationalNumber;

    @NotNull
    private String name;

    @NotNull
    private LocalDate dateOfBirth;

    private String gender;

    private SocialSecurityPensionerDTO pensioner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public SocialSecurityPensionerDTO getPensioner() {
        return pensioner;
    }

    public void setPensioner(SocialSecurityPensionerDTO pensioner) {
        this.pensioner = pensioner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FamilyMemberDTO)) {
            return false;
        }

        FamilyMemberDTO familyMemberDTO = (FamilyMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, familyMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilyMemberDTO{" +
            "id=" + getId() +
            ", nationalNumber='" + getNationalNumber() + "'" +
            ", name='" + getName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", pensioner=" + getPensioner() +
            "}";
    }
}
