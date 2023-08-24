package ly.qubit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ly.qubit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnualDeclarationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnualDeclarationDTO.class);
        AnnualDeclarationDTO annualDeclarationDTO1 = new AnnualDeclarationDTO();
        annualDeclarationDTO1.setId(1L);
        AnnualDeclarationDTO annualDeclarationDTO2 = new AnnualDeclarationDTO();
        assertThat(annualDeclarationDTO1).isNotEqualTo(annualDeclarationDTO2);
        annualDeclarationDTO2.setId(annualDeclarationDTO1.getId());
        assertThat(annualDeclarationDTO1).isEqualTo(annualDeclarationDTO2);
        annualDeclarationDTO2.setId(2L);
        assertThat(annualDeclarationDTO1).isNotEqualTo(annualDeclarationDTO2);
        annualDeclarationDTO1.setId(null);
        assertThat(annualDeclarationDTO1).isNotEqualTo(annualDeclarationDTO2);
    }
}
