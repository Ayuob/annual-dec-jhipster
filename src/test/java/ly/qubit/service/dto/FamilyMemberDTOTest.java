package ly.qubit.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ly.qubit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyMemberDTO.class);
        FamilyMemberDTO familyMemberDTO1 = new FamilyMemberDTO();
        familyMemberDTO1.setId(1L);
        FamilyMemberDTO familyMemberDTO2 = new FamilyMemberDTO();
        assertThat(familyMemberDTO1).isNotEqualTo(familyMemberDTO2);
        familyMemberDTO2.setId(familyMemberDTO1.getId());
        assertThat(familyMemberDTO1).isEqualTo(familyMemberDTO2);
        familyMemberDTO2.setId(2L);
        assertThat(familyMemberDTO1).isNotEqualTo(familyMemberDTO2);
        familyMemberDTO1.setId(null);
        assertThat(familyMemberDTO1).isNotEqualTo(familyMemberDTO2);
    }
}
