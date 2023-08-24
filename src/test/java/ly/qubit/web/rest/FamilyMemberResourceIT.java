package ly.qubit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ly.qubit.IntegrationTest;
import ly.qubit.domain.FamilyMember;
import ly.qubit.repository.FamilyMemberRepository;
import ly.qubit.service.FamilyMemberService;
import ly.qubit.service.dto.FamilyMemberDTO;
import ly.qubit.service.mapper.FamilyMemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FamilyMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FamilyMemberResourceIT {

    private static final String DEFAULT_NATIONAL_NUMBER = "AAAAAAAAAAAA";
    private static final String UPDATED_NATIONAL_NUMBER = "BBBBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/family-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Mock
    private FamilyMemberRepository familyMemberRepositoryMock;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Mock
    private FamilyMemberService familyMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyMemberMockMvc;

    private FamilyMember familyMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyMember createEntity(EntityManager em) {
        FamilyMember familyMember = new FamilyMember()
            .nationalNumber(DEFAULT_NATIONAL_NUMBER)
            .name(DEFAULT_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER);
        return familyMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyMember createUpdatedEntity(EntityManager em) {
        FamilyMember familyMember = new FamilyMember()
            .nationalNumber(UPDATED_NATIONAL_NUMBER)
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER);
        return familyMember;
    }

    @BeforeEach
    public void initTest() {
        familyMember = createEntity(em);
    }

    @Test
    @Transactional
    void createFamilyMember() throws Exception {
        int databaseSizeBeforeCreate = familyMemberRepository.findAll().size();
        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);
        restFamilyMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeCreate + 1);
        FamilyMember testFamilyMember = familyMemberList.get(familyMemberList.size() - 1);
        assertThat(testFamilyMember.getNationalNumber()).isEqualTo(DEFAULT_NATIONAL_NUMBER);
        assertThat(testFamilyMember.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilyMember.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testFamilyMember.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void createFamilyMemberWithExistingId() throws Exception {
        // Create the FamilyMember with an existing ID
        familyMember.setId(1L);
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        int databaseSizeBeforeCreate = familyMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNationalNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyMemberRepository.findAll().size();
        // set the field null
        familyMember.setNationalNumber(null);

        // Create the FamilyMember, which fails.
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        restFamilyMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyMemberRepository.findAll().size();
        // set the field null
        familyMember.setName(null);

        // Create the FamilyMember, which fails.
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        restFamilyMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyMemberRepository.findAll().size();
        // set the field null
        familyMember.setDateOfBirth(null);

        // Create the FamilyMember, which fails.
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        restFamilyMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyMemberRepository.findAll().size();
        // set the field null
        familyMember.setGender(null);

        // Create the FamilyMember, which fails.
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        restFamilyMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilyMembers() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].nationalNumber").value(hasItem(DEFAULT_NATIONAL_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamilyMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(familyMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(familyMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamilyMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(familyMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(familyMemberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFamilyMember() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        // Get the familyMember
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, familyMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familyMember.getId().intValue()))
            .andExpect(jsonPath("$.nationalNumber").value(DEFAULT_NATIONAL_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER));
    }

    @Test
    @Transactional
    void getNonExistingFamilyMember() throws Exception {
        // Get the familyMember
        restFamilyMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFamilyMember() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();

        // Update the familyMember
        FamilyMember updatedFamilyMember = familyMemberRepository.findById(familyMember.getId()).get();
        // Disconnect from session so that the updates on updatedFamilyMember are not directly saved in db
        em.detach(updatedFamilyMember);
        updatedFamilyMember
            .nationalNumber(UPDATED_NATIONAL_NUMBER)
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER);
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(updatedFamilyMember);

        restFamilyMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
        FamilyMember testFamilyMember = familyMemberList.get(familyMemberList.size() - 1);
        assertThat(testFamilyMember.getNationalNumber()).isEqualTo(UPDATED_NATIONAL_NUMBER);
        assertThat(testFamilyMember.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyMember.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testFamilyMember.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void putNonExistingFamilyMember() throws Exception {
        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();
        familyMember.setId(count.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilyMember() throws Exception {
        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();
        familyMember.setId(count.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilyMember() throws Exception {
        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();
        familyMember.setId(count.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyMemberWithPatch() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();

        // Update the familyMember using partial update
        FamilyMember partialUpdatedFamilyMember = new FamilyMember();
        partialUpdatedFamilyMember.setId(familyMember.getId());

        partialUpdatedFamilyMember.nationalNumber(UPDATED_NATIONAL_NUMBER).dateOfBirth(UPDATED_DATE_OF_BIRTH).gender(UPDATED_GENDER);

        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyMember))
            )
            .andExpect(status().isOk());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
        FamilyMember testFamilyMember = familyMemberList.get(familyMemberList.size() - 1);
        assertThat(testFamilyMember.getNationalNumber()).isEqualTo(UPDATED_NATIONAL_NUMBER);
        assertThat(testFamilyMember.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilyMember.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testFamilyMember.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void fullUpdateFamilyMemberWithPatch() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();

        // Update the familyMember using partial update
        FamilyMember partialUpdatedFamilyMember = new FamilyMember();
        partialUpdatedFamilyMember.setId(familyMember.getId());

        partialUpdatedFamilyMember
            .nationalNumber(UPDATED_NATIONAL_NUMBER)
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER);

        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilyMember))
            )
            .andExpect(status().isOk());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
        FamilyMember testFamilyMember = familyMemberList.get(familyMemberList.size() - 1);
        assertThat(testFamilyMember.getNationalNumber()).isEqualTo(UPDATED_NATIONAL_NUMBER);
        assertThat(testFamilyMember.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilyMember.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testFamilyMember.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void patchNonExistingFamilyMember() throws Exception {
        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();
        familyMember.setId(count.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familyMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilyMember() throws Exception {
        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();
        familyMember.setId(count.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilyMember() throws Exception {
        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();
        familyMember.setId(count.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilyMember() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        int databaseSizeBeforeDelete = familyMemberRepository.findAll().size();

        // Delete the familyMember
        restFamilyMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, familyMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
