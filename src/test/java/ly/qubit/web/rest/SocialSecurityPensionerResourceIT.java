package ly.qubit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ly.qubit.IntegrationTest;
import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.repository.SocialSecurityPensionerRepository;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import ly.qubit.service.mapper.SocialSecurityPensionerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SocialSecurityPensionerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocialSecurityPensionerResourceIT {

    private static final String DEFAULT_NATIONAL_NUMBER = "AAAAAAAAAAAA";
    private static final String UPDATED_NATIONAL_NUMBER = "BBBBBBBBBBBB";

    private static final String DEFAULT_PENSION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PENSION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/social-security-pensioners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialSecurityPensionerRepository socialSecurityPensionerRepository;

    @Autowired
    private SocialSecurityPensionerMapper socialSecurityPensionerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialSecurityPensionerMockMvc;

    private SocialSecurityPensioner socialSecurityPensioner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialSecurityPensioner createEntity(EntityManager em) {
        SocialSecurityPensioner socialSecurityPensioner = new SocialSecurityPensioner()
            .nationalNumber(DEFAULT_NATIONAL_NUMBER)
            .pensionNumber(DEFAULT_PENSION_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .address(DEFAULT_ADDRESS);
        return socialSecurityPensioner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialSecurityPensioner createUpdatedEntity(EntityManager em) {
        SocialSecurityPensioner socialSecurityPensioner = new SocialSecurityPensioner()
            .nationalNumber(UPDATED_NATIONAL_NUMBER)
            .pensionNumber(UPDATED_PENSION_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .address(UPDATED_ADDRESS);
        return socialSecurityPensioner;
    }

    @BeforeEach
    public void initTest() {
        socialSecurityPensioner = createEntity(em);
    }

    @Test
    @Transactional
    void createSocialSecurityPensioner() throws Exception {
        int databaseSizeBeforeCreate = socialSecurityPensionerRepository.findAll().size();
        // Create the SocialSecurityPensioner
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);
        restSocialSecurityPensionerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeCreate + 1);
        SocialSecurityPensioner testSocialSecurityPensioner = socialSecurityPensionerList.get(socialSecurityPensionerList.size() - 1);
        assertThat(testSocialSecurityPensioner.getNationalNumber()).isEqualTo(DEFAULT_NATIONAL_NUMBER);
        assertThat(testSocialSecurityPensioner.getPensionNumber()).isEqualTo(DEFAULT_PENSION_NUMBER);
        assertThat(testSocialSecurityPensioner.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testSocialSecurityPensioner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createSocialSecurityPensionerWithExistingId() throws Exception {
        // Create the SocialSecurityPensioner with an existing ID
        socialSecurityPensioner.setId(1L);
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        int databaseSizeBeforeCreate = socialSecurityPensionerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialSecurityPensionerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNationalNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialSecurityPensionerRepository.findAll().size();
        // set the field null
        socialSecurityPensioner.setNationalNumber(null);

        // Create the SocialSecurityPensioner, which fails.
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        restSocialSecurityPensionerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPensionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialSecurityPensionerRepository.findAll().size();
        // set the field null
        socialSecurityPensioner.setPensionNumber(null);

        // Create the SocialSecurityPensioner, which fails.
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        restSocialSecurityPensionerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialSecurityPensionerRepository.findAll().size();
        // set the field null
        socialSecurityPensioner.setDateOfBirth(null);

        // Create the SocialSecurityPensioner, which fails.
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        restSocialSecurityPensionerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialSecurityPensionerRepository.findAll().size();
        // set the field null
        socialSecurityPensioner.setAddress(null);

        // Create the SocialSecurityPensioner, which fails.
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        restSocialSecurityPensionerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialSecurityPensioners() throws Exception {
        // Initialize the database
        socialSecurityPensionerRepository.saveAndFlush(socialSecurityPensioner);

        // Get all the socialSecurityPensionerList
        restSocialSecurityPensionerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialSecurityPensioner.getId().intValue())))
            .andExpect(jsonPath("$.[*].nationalNumber").value(hasItem(DEFAULT_NATIONAL_NUMBER)))
            .andExpect(jsonPath("$.[*].pensionNumber").value(hasItem(DEFAULT_PENSION_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getSocialSecurityPensioner() throws Exception {
        // Initialize the database
        socialSecurityPensionerRepository.saveAndFlush(socialSecurityPensioner);

        // Get the socialSecurityPensioner
        restSocialSecurityPensionerMockMvc
            .perform(get(ENTITY_API_URL_ID, socialSecurityPensioner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialSecurityPensioner.getId().intValue()))
            .andExpect(jsonPath("$.nationalNumber").value(DEFAULT_NATIONAL_NUMBER))
            .andExpect(jsonPath("$.pensionNumber").value(DEFAULT_PENSION_NUMBER))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingSocialSecurityPensioner() throws Exception {
        // Get the socialSecurityPensioner
        restSocialSecurityPensionerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSocialSecurityPensioner() throws Exception {
        // Initialize the database
        socialSecurityPensionerRepository.saveAndFlush(socialSecurityPensioner);

        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();

        // Update the socialSecurityPensioner
        SocialSecurityPensioner updatedSocialSecurityPensioner = socialSecurityPensionerRepository
            .findById(socialSecurityPensioner.getId())
            .get();
        // Disconnect from session so that the updates on updatedSocialSecurityPensioner are not directly saved in db
        em.detach(updatedSocialSecurityPensioner);
        updatedSocialSecurityPensioner
            .nationalNumber(UPDATED_NATIONAL_NUMBER)
            .pensionNumber(UPDATED_PENSION_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .address(UPDATED_ADDRESS);
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(updatedSocialSecurityPensioner);

        restSocialSecurityPensionerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialSecurityPensionerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isOk());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
        SocialSecurityPensioner testSocialSecurityPensioner = socialSecurityPensionerList.get(socialSecurityPensionerList.size() - 1);
        assertThat(testSocialSecurityPensioner.getNationalNumber()).isEqualTo(UPDATED_NATIONAL_NUMBER);
        assertThat(testSocialSecurityPensioner.getPensionNumber()).isEqualTo(UPDATED_PENSION_NUMBER);
        assertThat(testSocialSecurityPensioner.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testSocialSecurityPensioner.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingSocialSecurityPensioner() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();
        socialSecurityPensioner.setId(count.incrementAndGet());

        // Create the SocialSecurityPensioner
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialSecurityPensionerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialSecurityPensionerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialSecurityPensioner() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();
        socialSecurityPensioner.setId(count.incrementAndGet());

        // Create the SocialSecurityPensioner
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityPensionerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialSecurityPensioner() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();
        socialSecurityPensioner.setId(count.incrementAndGet());

        // Create the SocialSecurityPensioner
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityPensionerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialSecurityPensionerWithPatch() throws Exception {
        // Initialize the database
        socialSecurityPensionerRepository.saveAndFlush(socialSecurityPensioner);

        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();

        // Update the socialSecurityPensioner using partial update
        SocialSecurityPensioner partialUpdatedSocialSecurityPensioner = new SocialSecurityPensioner();
        partialUpdatedSocialSecurityPensioner.setId(socialSecurityPensioner.getId());

        partialUpdatedSocialSecurityPensioner
            .nationalNumber(UPDATED_NATIONAL_NUMBER)
            .pensionNumber(UPDATED_PENSION_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH);

        restSocialSecurityPensionerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialSecurityPensioner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialSecurityPensioner))
            )
            .andExpect(status().isOk());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
        SocialSecurityPensioner testSocialSecurityPensioner = socialSecurityPensionerList.get(socialSecurityPensionerList.size() - 1);
        assertThat(testSocialSecurityPensioner.getNationalNumber()).isEqualTo(UPDATED_NATIONAL_NUMBER);
        assertThat(testSocialSecurityPensioner.getPensionNumber()).isEqualTo(UPDATED_PENSION_NUMBER);
        assertThat(testSocialSecurityPensioner.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testSocialSecurityPensioner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateSocialSecurityPensionerWithPatch() throws Exception {
        // Initialize the database
        socialSecurityPensionerRepository.saveAndFlush(socialSecurityPensioner);

        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();

        // Update the socialSecurityPensioner using partial update
        SocialSecurityPensioner partialUpdatedSocialSecurityPensioner = new SocialSecurityPensioner();
        partialUpdatedSocialSecurityPensioner.setId(socialSecurityPensioner.getId());

        partialUpdatedSocialSecurityPensioner
            .nationalNumber(UPDATED_NATIONAL_NUMBER)
            .pensionNumber(UPDATED_PENSION_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .address(UPDATED_ADDRESS);

        restSocialSecurityPensionerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialSecurityPensioner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialSecurityPensioner))
            )
            .andExpect(status().isOk());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
        SocialSecurityPensioner testSocialSecurityPensioner = socialSecurityPensionerList.get(socialSecurityPensionerList.size() - 1);
        assertThat(testSocialSecurityPensioner.getNationalNumber()).isEqualTo(UPDATED_NATIONAL_NUMBER);
        assertThat(testSocialSecurityPensioner.getPensionNumber()).isEqualTo(UPDATED_PENSION_NUMBER);
        assertThat(testSocialSecurityPensioner.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testSocialSecurityPensioner.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingSocialSecurityPensioner() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();
        socialSecurityPensioner.setId(count.incrementAndGet());

        // Create the SocialSecurityPensioner
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialSecurityPensionerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialSecurityPensionerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialSecurityPensioner() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();
        socialSecurityPensioner.setId(count.incrementAndGet());

        // Create the SocialSecurityPensioner
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityPensionerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialSecurityPensioner() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityPensionerRepository.findAll().size();
        socialSecurityPensioner.setId(count.incrementAndGet());

        // Create the SocialSecurityPensioner
        SocialSecurityPensionerDTO socialSecurityPensionerDTO = socialSecurityPensionerMapper.toDto(socialSecurityPensioner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityPensionerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurityPensionerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialSecurityPensioner in the database
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialSecurityPensioner() throws Exception {
        // Initialize the database
        socialSecurityPensionerRepository.saveAndFlush(socialSecurityPensioner);

        int databaseSizeBeforeDelete = socialSecurityPensionerRepository.findAll().size();

        // Delete the socialSecurityPensioner
        restSocialSecurityPensionerMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialSecurityPensioner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialSecurityPensioner> socialSecurityPensionerList = socialSecurityPensionerRepository.findAll();
        assertThat(socialSecurityPensionerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
