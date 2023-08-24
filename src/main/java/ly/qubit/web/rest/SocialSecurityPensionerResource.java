package ly.qubit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ly.qubit.repository.SocialSecurityPensionerRepository;
import ly.qubit.service.SocialSecurityPensionerService;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import ly.qubit.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ly.qubit.domain.SocialSecurityPensioner}.
 */
@RestController
@RequestMapping("/api")
public class SocialSecurityPensionerResource {

    private final Logger log = LoggerFactory.getLogger(SocialSecurityPensionerResource.class);

    private static final String ENTITY_NAME = "socialSecurityPensioner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialSecurityPensionerService socialSecurityPensionerService;

    private final SocialSecurityPensionerRepository socialSecurityPensionerRepository;

    public SocialSecurityPensionerResource(
        SocialSecurityPensionerService socialSecurityPensionerService,
        SocialSecurityPensionerRepository socialSecurityPensionerRepository
    ) {
        this.socialSecurityPensionerService = socialSecurityPensionerService;
        this.socialSecurityPensionerRepository = socialSecurityPensionerRepository;
    }

    /**
     * {@code POST  /social-security-pensioners} : Create a new socialSecurityPensioner.
     *
     * @param socialSecurityPensionerDTO the socialSecurityPensionerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialSecurityPensionerDTO, or with status {@code 400 (Bad Request)} if the socialSecurityPensioner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/social-security-pensioners")
    public ResponseEntity<SocialSecurityPensionerDTO> createSocialSecurityPensioner(
        @Valid @RequestBody SocialSecurityPensionerDTO socialSecurityPensionerDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SocialSecurityPensioner : {}", socialSecurityPensionerDTO);
        if (socialSecurityPensionerDTO.getId() != null) {
            throw new BadRequestAlertException("A new socialSecurityPensioner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialSecurityPensionerDTO result = socialSecurityPensionerService.save(socialSecurityPensionerDTO);
        return ResponseEntity
            .created(new URI("/api/social-security-pensioners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-security-pensioners/:id} : Updates an existing socialSecurityPensioner.
     *
     * @param id the id of the socialSecurityPensionerDTO to save.
     * @param socialSecurityPensionerDTO the socialSecurityPensionerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialSecurityPensionerDTO,
     * or with status {@code 400 (Bad Request)} if the socialSecurityPensionerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialSecurityPensionerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/social-security-pensioners/{id}")
    public ResponseEntity<SocialSecurityPensionerDTO> updateSocialSecurityPensioner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialSecurityPensionerDTO socialSecurityPensionerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SocialSecurityPensioner : {}, {}", id, socialSecurityPensionerDTO);
        if (socialSecurityPensionerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialSecurityPensionerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialSecurityPensionerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SocialSecurityPensionerDTO result = socialSecurityPensionerService.update(socialSecurityPensionerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialSecurityPensionerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /social-security-pensioners/:id} : Partial updates given fields of an existing socialSecurityPensioner, field will ignore if it is null
     *
     * @param id the id of the socialSecurityPensionerDTO to save.
     * @param socialSecurityPensionerDTO the socialSecurityPensionerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialSecurityPensionerDTO,
     * or with status {@code 400 (Bad Request)} if the socialSecurityPensionerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the socialSecurityPensionerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialSecurityPensionerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/social-security-pensioners/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialSecurityPensionerDTO> partialUpdateSocialSecurityPensioner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialSecurityPensionerDTO socialSecurityPensionerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialSecurityPensioner partially : {}, {}", id, socialSecurityPensionerDTO);
        if (socialSecurityPensionerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialSecurityPensionerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialSecurityPensionerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialSecurityPensionerDTO> result = socialSecurityPensionerService.partialUpdate(socialSecurityPensionerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialSecurityPensionerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /social-security-pensioners} : get all the socialSecurityPensioners.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialSecurityPensioners in body.
     */
    @GetMapping("/social-security-pensioners")
    public ResponseEntity<List<SocialSecurityPensionerDTO>> getAllSocialSecurityPensioners(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SocialSecurityPensioners");
        Page<SocialSecurityPensionerDTO> page = socialSecurityPensionerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /social-security-pensioners/:id} : get the "id" socialSecurityPensioner.
     *
     * @param id the id of the socialSecurityPensionerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialSecurityPensionerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/social-security-pensioners/{id}")
    public ResponseEntity<SocialSecurityPensionerDTO> getSocialSecurityPensioner(@PathVariable Long id) {
        log.debug("REST request to get SocialSecurityPensioner : {}", id);
        Optional<SocialSecurityPensionerDTO> socialSecurityPensionerDTO = socialSecurityPensionerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialSecurityPensionerDTO);
    }

    /**
     * {@code DELETE  /social-security-pensioners/:id} : delete the "id" socialSecurityPensioner.
     *
     * @param id the id of the socialSecurityPensionerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/social-security-pensioners/{id}")
    public ResponseEntity<Void> deleteSocialSecurityPensioner(@PathVariable Long id) {
        log.debug("REST request to delete SocialSecurityPensioner : {}", id);
        socialSecurityPensionerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
