package ly.qubit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FamilyMember.
 */
@Entity
@Table(name = "family_member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FamilyMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 12, max = 12)
    @Column(name = "national_number", length = 12, nullable = false, unique = true)
    private String nationalNumber;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "gender", nullable = false)
    private String gender;

    @ManyToOne
    @JsonIgnoreProperties(value = { "familyMembers", "annualDeclarations" }, allowSetters = true)
    private SocialSecurityPensioner pensioner;

    @ManyToMany(mappedBy = "familyMembers")
    @JsonIgnoreProperties(value = { "documents", "pensioner", "familyMembers" }, allowSetters = true)
    private Set<AnnualDeclaration> annualDeclarations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FamilyMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalNumber() {
        return this.nationalNumber;
    }

    public FamilyMember nationalNumber(String nationalNumber) {
        this.setNationalNumber(nationalNumber);
        return this;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public String getName() {
        return this.name;
    }

    public FamilyMember name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public FamilyMember dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return this.gender;
    }

    public FamilyMember gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public SocialSecurityPensioner getPensioner() {
        return this.pensioner;
    }

    public void setPensioner(SocialSecurityPensioner socialSecurityPensioner) {
        this.pensioner = socialSecurityPensioner;
    }

    public FamilyMember pensioner(SocialSecurityPensioner socialSecurityPensioner) {
        this.setPensioner(socialSecurityPensioner);
        return this;
    }

    public Set<AnnualDeclaration> getAnnualDeclarations() {
        return this.annualDeclarations;
    }

    public void setAnnualDeclarations(Set<AnnualDeclaration> annualDeclarations) {
        if (this.annualDeclarations != null) {
            this.annualDeclarations.forEach(i -> i.removeFamilyMembers(this));
        }
        if (annualDeclarations != null) {
            annualDeclarations.forEach(i -> i.addFamilyMembers(this));
        }
        this.annualDeclarations = annualDeclarations;
    }

    public FamilyMember annualDeclarations(Set<AnnualDeclaration> annualDeclarations) {
        this.setAnnualDeclarations(annualDeclarations);
        return this;
    }

    public FamilyMember addAnnualDeclaration(AnnualDeclaration annualDeclaration) {
        this.annualDeclarations.add(annualDeclaration);
        annualDeclaration.getFamilyMembers().add(this);
        return this;
    }

    public FamilyMember removeAnnualDeclaration(AnnualDeclaration annualDeclaration) {
        this.annualDeclarations.remove(annualDeclaration);
        annualDeclaration.getFamilyMembers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FamilyMember)) {
            return false;
        }
        return id != null && id.equals(((FamilyMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FamilyMember{" +
            "id=" + getId() +
            ", nationalNumber='" + getNationalNumber() + "'" +
            ", name='" + getName() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
