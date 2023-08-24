package ly.qubit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ly.qubit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnualDeclarationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnualDeclaration.class);
        AnnualDeclaration annualDeclaration1 = new AnnualDeclaration();
        annualDeclaration1.setId(1L);
        AnnualDeclaration annualDeclaration2 = new AnnualDeclaration();
        annualDeclaration2.setId(annualDeclaration1.getId());
        assertThat(annualDeclaration1).isEqualTo(annualDeclaration2);
        annualDeclaration2.setId(2L);
        assertThat(annualDeclaration1).isNotEqualTo(annualDeclaration2);
        annualDeclaration1.setId(null);
        assertThat(annualDeclaration1).isNotEqualTo(annualDeclaration2);
    }
}
