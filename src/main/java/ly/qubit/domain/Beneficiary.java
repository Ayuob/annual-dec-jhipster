package ly.qubit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import ly.qubit.domain.enumeration.EntitlementType;

/**
 * A Beneficiary.
 */
@Entity
@Table(name = "beneficiary")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Beneficiary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entitlement_type", nullable = false)
    private EntitlementType entitlementType;

    @Column(name = "entitlement_details")
    private String entitlementDetails;

    @JsonIgnoreProperties(value = { "pensioner", "beneficiary", "annualDeclarations" }, allowSetters = true)
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private FamilyMember familyMembers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Beneficiary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntitlementType getEntitlementType() {
        return this.entitlementType;
    }

    public Beneficiary entitlementType(EntitlementType entitlementType) {
        this.setEntitlementType(entitlementType);
        return this;
    }

    public void setEntitlementType(EntitlementType entitlementType) {
        this.entitlementType = entitlementType;
    }

    public String getEntitlementDetails() {
        return this.entitlementDetails;
    }

    public Beneficiary entitlementDetails(String entitlementDetails) {
        this.setEntitlementDetails(entitlementDetails);
        return this;
    }

    public void setEntitlementDetails(String entitlementDetails) {
        this.entitlementDetails = entitlementDetails;
    }

    public FamilyMember getFamilyMembers() {
        return this.familyMembers;
    }

    public void setFamilyMembers(FamilyMember familyMember) {
        this.familyMembers = familyMember;
    }

    public Beneficiary familyMembers(FamilyMember familyMember) {
        this.setFamilyMembers(familyMember);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beneficiary)) {
            return false;
        }
        return id != null && id.equals(((Beneficiary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Beneficiary{" +
            "id=" + getId() +
            ", entitlementType='" + getEntitlementType() + "'" +
            ", entitlementDetails='" + getEntitlementDetails() + "'" +
            "}";
    }
}
