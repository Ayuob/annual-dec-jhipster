package ly.qubit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import ly.qubit.security.AuthoritiesConstants;
import tech.jhipster.security.RandomUtil;

/**
 * A SocialSecurityPensioner.
 */
@Entity
@Table(name = "social_security_pensioner")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialSecurityPensioner extends User implements Serializable {

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

    @Column(name = "pension_number")
    private String pensionNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "pensioner")
    @JsonIgnoreProperties(value = { "pensioner", "annualDeclarations" }, allowSetters = true)
    private Set<FamilyMember> familyMembers = new HashSet<>();

    @OneToMany(mappedBy = "pensioner")
    @JsonIgnoreProperties(value = { "documents", "pensioner", "familyMembers" }, allowSetters = true)
    private Set<AnnualDeclaration> annualDeclarations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SocialSecurityPensioner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalNumber() {
        return this.nationalNumber;
    }

    public SocialSecurityPensioner nationalNumber(String nationalNumber) {
        this.setNationalNumber(nationalNumber);
        return this;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public String getPensionNumber() {
        return this.pensionNumber;
    }

    public SocialSecurityPensioner pensionNumber(String pensionNumber) {
        this.setPensionNumber(pensionNumber);
        return this;
    }

    public void setPensionNumber(String pensionNumber) {
        this.pensionNumber = pensionNumber;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public SocialSecurityPensioner dateOfBirth(LocalDate dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return this.address;
    }

    public SocialSecurityPensioner address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<FamilyMember> getFamilyMembers() {
        return this.familyMembers;
    }

    public void setFamilyMembers(Set<FamilyMember> familyMembers) {
        if (this.familyMembers != null) {
            this.familyMembers.forEach(i -> i.setPensioner(null));
        }
        if (familyMembers != null) {
            familyMembers.forEach(i -> i.setPensioner(this));
        }
        this.familyMembers = familyMembers;
    }

    public SocialSecurityPensioner familyMembers(Set<FamilyMember> familyMembers) {
        this.setFamilyMembers(familyMembers);
        return this;
    }

    public SocialSecurityPensioner addFamilyMembers(FamilyMember familyMember) {
        this.familyMembers.add(familyMember);
        familyMember.setPensioner(this);
        return this;
    }

    public SocialSecurityPensioner removeFamilyMembers(FamilyMember familyMember) {
        this.familyMembers.remove(familyMember);
        familyMember.setPensioner(null);
        return this;
    }

    public Set<AnnualDeclaration> getAnnualDeclarations() {
        return this.annualDeclarations;
    }

    public void setAnnualDeclarations(Set<AnnualDeclaration> annualDeclarations) {
        if (this.annualDeclarations != null) {
            this.annualDeclarations.forEach(i -> i.setPensioner(null));
        }
        if (annualDeclarations != null) {
            annualDeclarations.forEach(i -> i.setPensioner(this));
        }
        this.annualDeclarations = annualDeclarations;
    }

    public SocialSecurityPensioner annualDeclarations(Set<AnnualDeclaration> annualDeclarations) {
        this.setAnnualDeclarations(annualDeclarations);
        return this;
    }

    public SocialSecurityPensioner addAnnualDeclaration(AnnualDeclaration annualDeclaration) {
        this.annualDeclarations.add(annualDeclaration);
        annualDeclaration.setPensioner(this);
        return this;
    }

    public SocialSecurityPensioner removeAnnualDeclaration(AnnualDeclaration annualDeclaration) {
        this.annualDeclarations.remove(annualDeclaration);
        annualDeclaration.setPensioner(null);
        return this;
    }

    public User getUser() {
        User user = new User();
        user.setEmail(this.getEmail());
        user.setLogin(this.getLogin());
        user.setLangKey(this.getLangKey());
        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setPassword(this.getPassword());
        user.setActivationKey(this.getActivationKey());
        //        user.setPensionNumber(managedUserVM.getPensionNumber());
        //        user.setNationalNumber(managedUserVM.getNationalNumber());
        //        user.setAddress(managedUserVM.getAddress());
        user.setAuthorities(this.getAuthorities());
        return user;
    }

    public void setUser(User user) {
        this.setEmail(user.getEmail());
        this.setLogin(user.getLogin());
        this.setLangKey(user.getLangKey());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setPassword(user.getPassword());
        //        this.setPensionNumber(pensioner.getPensionNumber());
        //        this.setNationalNumber(pensioner.getNationalNumber());
        //        this.setAddress(pensioner.getAddress())
        //        this.setAuthorities(pensioner.getAuthorities());
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialSecurityPensioner)) {
            return false;
        }
        return id != null && id.equals(((SocialSecurityPensioner) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialSecurityPensioner{" +
            "id=" + getId() +
            ", nationalNumber='" + getNationalNumber() + "'" +
            ", pensionNumber='" + getPensionNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
