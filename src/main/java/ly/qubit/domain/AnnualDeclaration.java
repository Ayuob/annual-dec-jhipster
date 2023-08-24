package ly.qubit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import ly.qubit.domain.enumeration.DeclarationStatus;

/**
 * A AnnualDeclaration.
 */
@Entity
@Table(name = "annual_declaration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnualDeclaration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeclarationStatus status;

    @OneToMany(mappedBy = "annualDeclaration")
    @JsonIgnoreProperties(value = { "annualDeclaration" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "familyMembers", "annualDeclarations" }, allowSetters = true)
    private SocialSecurityPensioner pensioner;

    @ManyToMany
    @JoinTable(
        name = "rel_annual_declaration__family_members",
        joinColumns = @JoinColumn(name = "annual_declaration_id"),
        inverseJoinColumns = @JoinColumn(name = "family_members_id")
    )
    @JsonIgnoreProperties(value = { "pensioner", "beneficiary", "annualDeclarations" }, allowSetters = true)
    private Set<FamilyMember> familyMembers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnnualDeclaration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSubmissionDate() {
        return this.submissionDate;
    }

    public AnnualDeclaration submissionDate(LocalDate submissionDate) {
        this.setSubmissionDate(submissionDate);
        return this;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public DeclarationStatus getStatus() {
        return this.status;
    }

    public AnnualDeclaration status(DeclarationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DeclarationStatus status) {
        this.status = status;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setAnnualDeclaration(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setAnnualDeclaration(this));
        }
        this.documents = documents;
    }

    public AnnualDeclaration documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public AnnualDeclaration addDocuments(Document document) {
        this.documents.add(document);
        document.setAnnualDeclaration(this);
        return this;
    }

    public AnnualDeclaration removeDocuments(Document document) {
        this.documents.remove(document);
        document.setAnnualDeclaration(null);
        return this;
    }

    public SocialSecurityPensioner getPensioner() {
        return this.pensioner;
    }

    public void setPensioner(SocialSecurityPensioner socialSecurityPensioner) {
        this.pensioner = socialSecurityPensioner;
    }

    public AnnualDeclaration pensioner(SocialSecurityPensioner socialSecurityPensioner) {
        this.setPensioner(socialSecurityPensioner);
        return this;
    }

    public Set<FamilyMember> getFamilyMembers() {
        return this.familyMembers;
    }

    public void setFamilyMembers(Set<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public AnnualDeclaration familyMembers(Set<FamilyMember> familyMembers) {
        this.setFamilyMembers(familyMembers);
        return this;
    }

    public AnnualDeclaration addFamilyMembers(FamilyMember familyMember) {
        this.familyMembers.add(familyMember);
        familyMember.getAnnualDeclarations().add(this);
        return this;
    }

    public AnnualDeclaration removeFamilyMembers(FamilyMember familyMember) {
        this.familyMembers.remove(familyMember);
        familyMember.getAnnualDeclarations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnualDeclaration)) {
            return false;
        }
        return id != null && id.equals(((AnnualDeclaration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnualDeclaration{" +
            "id=" + getId() +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
