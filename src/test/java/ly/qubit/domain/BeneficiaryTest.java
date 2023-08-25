package ly.qubit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BeneficiaryTest {

    BeneficiaryId id = new BeneficiaryId(1L, 1L);
    BeneficiaryId id2 = new BeneficiaryId(2L, 2L);

    @Test
    void equalsVerifier() throws Exception {
        // TestUtil.equalsVerifier(Beneficiary.class);
        Beneficiary beneficiary1 = new Beneficiary();
        beneficiary1.setId(id);
        Beneficiary beneficiary2 = new Beneficiary();
        beneficiary2.setId(beneficiary1.getId());
        assertThat(beneficiary1).isEqualTo(beneficiary2);
        beneficiary2.setId(id2);
        assertThat(beneficiary1).isNotEqualTo(beneficiary2);
        beneficiary1.setId(null);
        assertThat(beneficiary1).isNotEqualTo(beneficiary2);
    }
}
