package ly.qubit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ly.qubit.repository.FamilyMemberRepository;
import ly.qubit.service.FamilyMemberService;
import ly.qubit.service.dto.FamilyMemberDTO;
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
 * REST controller for managing {@link ly.qubit.domain.FamilyMember}.
 */
@RestController
@RequestMapping("/api")
public class FamilyMemberResource {

    private final Logger log = LoggerFactory.getLogger(FamilyMemberResource.class);

    private static final String ENTITY_NAME = "familyMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyMemberService familyMemberService;

    private final FamilyMemberRepository familyMemberRepository;

    public FamilyMemberResource(FamilyMemberService familyMemberService, FamilyMemberRepository familyMemberRepository) {
        this.familyMemberService = familyMemberService;
        this.familyMemberRepository = familyMemberRepository;
    }

    /**
     * {@code POST  /family-members} : Create a new familyMember.
     *
     * @param familyMemberDTO the familyMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familyMemberDTO, or with status {@code 400 (Bad Request)} if the familyMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/family-members")
    public ResponseEntity<FamilyMemberDTO> createFamilyMember(@Valid @RequestBody FamilyMemberDTO familyMemberDTO)
        throws URISyntaxException {
        log.debug("REST request to save FamilyMember : {}", familyMemberDTO);
        if (familyMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new familyMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilyMemberDTO result = familyMemberService.save(familyMemberDTO);
        return ResponseEntity
            .created(new URI("/api/family-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /family-members/:id} : Updates an existing familyMember.
     *
     * @param id the id of the familyMemberDTO to save.
     * @param familyMemberDTO the familyMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyMemberDTO,
     * or with status {@code 400 (Bad Request)} if the familyMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familyMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/family-members/{id}")
    public ResponseEntity<FamilyMemberDTO> updateFamilyMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FamilyMemberDTO familyMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FamilyMember : {}, {}", id, familyMemberDTO);
        if (familyMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FamilyMemberDTO result = familyMemberService.update(familyMemberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /family-members/:id} : Partial updates given fields of an existing familyMember, field will ignore if it is null
     *
     * @param id the id of the familyMemberDTO to save.
     * @param familyMemberDTO the familyMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyMemberDTO,
     * or with status {@code 400 (Bad Request)} if the familyMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the familyMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the familyMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/family-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilyMemberDTO> partialUpdateFamilyMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FamilyMemberDTO familyMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FamilyMember partially : {}, {}", id, familyMemberDTO);
        if (familyMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilyMemberDTO> result = familyMemberService.partialUpdate(familyMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /family-members} : get all the familyMembers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familyMembers in body.
     */
    @GetMapping("/family-members")
    public ResponseEntity<List<FamilyMemberDTO>> getAllFamilyMembers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("beneficiary-is-null".equals(filter)) {
            log.debug("REST request to get all FamilyMembers where beneficiary is null");
            return new ResponseEntity<>(familyMemberService.findAllWhereBeneficiaryIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of FamilyMembers");
        Page<FamilyMemberDTO> page;
        if (eagerload) {
            page = familyMemberService.findAllWithEagerRelationships(pageable);
        } else {
            page = familyMemberService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /family-members/:id} : get the "id" familyMember.
     *
     * @param id the id of the familyMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familyMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/family-members/{id}")
    public ResponseEntity<FamilyMemberDTO> getFamilyMember(@PathVariable Long id) {
        log.debug("REST request to get FamilyMember : {}", id);
        Optional<FamilyMemberDTO> familyMemberDTO = familyMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyMemberDTO);
    }

    /**
     * {@code DELETE  /family-members/:id} : delete the "id" familyMember.
     *
     * @param id the id of the familyMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/family-members/{id}")
    public ResponseEntity<Void> deleteFamilyMember(@PathVariable Long id) {
        log.debug("REST request to delete FamilyMember : {}", id);
        familyMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
