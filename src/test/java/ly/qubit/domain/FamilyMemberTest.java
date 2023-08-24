package ly.qubit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ly.qubit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyMember.class);
        FamilyMember familyMember1 = new FamilyMember();
        familyMember1.setId(1L);
        FamilyMember familyMember2 = new FamilyMember();
        familyMember2.setId(familyMember1.getId());
        assertThat(familyMember1).isEqualTo(familyMember2);
        familyMember2.setId(2L);
        assertThat(familyMember1).isNotEqualTo(familyMember2);
        familyMember1.setId(null);
        assertThat(familyMember1).isNotEqualTo(familyMember2);
    }
}
