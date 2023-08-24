package ly.qubit.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ly.qubit.domain.SocialSecurityPensioner} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialSecurityPensionerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 12, max = 12)
    private String nationalNumber;

    @NotNull
    private String pensionNumber;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private String address;

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

    public String getPensionNumber() {
        return pensionNumber;
    }

    public void setPensionNumber(String pensionNumber) {
        this.pensionNumber = pensionNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialSecurityPensionerDTO)) {
            return false;
        }

        SocialSecurityPensionerDTO socialSecurityPensionerDTO = (SocialSecurityPensionerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, socialSecurityPensionerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialSecurityPensionerDTO{" +
            "id=" + getId() +
            ", nationalNumber='" + getNationalNumber() + "'" +
            ", pensionNumber='" + getPensionNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
