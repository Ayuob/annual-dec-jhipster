package ly.qubit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ly.qubit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialSecurityPensionerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialSecurityPensioner.class);
        SocialSecurityPensioner socialSecurityPensioner1 = new SocialSecurityPensioner();
        socialSecurityPensioner1.setId(1L);
        SocialSecurityPensioner socialSecurityPensioner2 = new SocialSecurityPensioner();
        socialSecurityPensioner2.setId(socialSecurityPensioner1.getId());
        assertThat(socialSecurityPensioner1).isEqualTo(socialSecurityPensioner2);
        socialSecurityPensioner2.setId(2L);
        assertThat(socialSecurityPensioner1).isNotEqualTo(socialSecurityPensioner2);
        socialSecurityPensioner1.setId(null);
        assertThat(socialSecurityPensioner1).isNotEqualTo(socialSecurityPensioner2);
    }
}
