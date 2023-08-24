package ly.qubit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ly.qubit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialSecurityPensionerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialSecurityPensionerDTO.class);
        SocialSecurityPensionerDTO socialSecurityPensionerDTO1 = new SocialSecurityPensionerDTO();
        socialSecurityPensionerDTO1.setId(1L);
        SocialSecurityPensionerDTO socialSecurityPensionerDTO2 = new SocialSecurityPensionerDTO();
        assertThat(socialSecurityPensionerDTO1).isNotEqualTo(socialSecurityPensionerDTO2);
        socialSecurityPensionerDTO2.setId(socialSecurityPensionerDTO1.getId());
        assertThat(socialSecurityPensionerDTO1).isEqualTo(socialSecurityPensionerDTO2);
        socialSecurityPensionerDTO2.setId(2L);
        assertThat(socialSecurityPensionerDTO1).isNotEqualTo(socialSecurityPensionerDTO2);
        socialSecurityPensionerDTO1.setId(null);
        assertThat(socialSecurityPensionerDTO1).isNotEqualTo(socialSecurityPensionerDTO2);
    }
}
