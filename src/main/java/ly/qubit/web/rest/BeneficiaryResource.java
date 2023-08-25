package ly.qubit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ly.qubit.domain.BeneficiaryId;
import ly.qubit.repository.BeneficiaryRepository;
import ly.qubit.service.BeneficiaryService;
import ly.qubit.service.dto.BeneficiaryDto_Empd;
import ly.qubit.service.dto.BeneficiaryIdDto;
import ly.qubit.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ly.qubit.domain.Beneficiary}.
 */
@RestController
@RequestMapping("/api")
public class BeneficiaryResource {

    private final Logger log = LoggerFactory.getLogger(BeneficiaryResource.class);

    private static final String ENTITY_NAME = "beneficiary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeneficiaryService beneficiaryService;

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryResource(BeneficiaryService beneficiaryService, BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryService = beneficiaryService;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    /**
     * {@code POST  /beneficiaries} : Create a new beneficiary.
     *
     * @param beneficiaryDTO the beneficiaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beneficiaryDTO, or with status {@code 400 (Bad Request)} if the beneficiary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beneficiaries")
    public ResponseEntity<BeneficiaryDto_Empd> createBeneficiary(@Valid @RequestBody BeneficiaryDto_Empd beneficiaryDTO)
        throws URISyntaxException {
        log.debug("REST request to save Beneficiary : {}", beneficiaryDTO);
        if (beneficiaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new beneficiary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeneficiaryDto_Empd result = beneficiaryService.save(beneficiaryDTO);
        return ResponseEntity
            .created(new URI("/api/beneficiaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beneficiaries/:id} : Updates an existing beneficiary.
     *
     * @param fid the id of the beneficiaryDTO.FM to save.
     * @param aid the id of the beneficiaryDTO.AD to save.
     * @param beneficiaryDTO the beneficiaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiaryDTO,
     * or with status {@code 400 (Bad Request)} if the beneficiaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beneficiaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beneficiaries/{fid}/{aid}")
    public ResponseEntity<BeneficiaryDto_Empd> updateBeneficiary(
        @PathVariable(value = "fid", required = false) final Long fid,
        @PathVariable(value = "aid", required = false) final Long aid,
        @Valid @RequestBody BeneficiaryDto_Empd beneficiaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Beneficiary : {}, {}, {}", aid, fid, beneficiaryDTO);

        BeneficiaryId id = new BeneficiaryId(fid, aid);

        if (beneficiaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BeneficiaryDto_Empd result = beneficiaryService.update(beneficiaryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beneficiaries/:id} : Partial updates given fields of an existing beneficiary, field will ignore if it is null
     *
     * @param fid the id of the beneficiaryDTO.FM to save.
     * @param aid the id of the beneficiaryDTO.AD to save.
     * @param beneficiaryDTO the beneficiaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiaryDTO,
     * or with status {@code 400 (Bad Request)} if the beneficiaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the beneficiaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the beneficiaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beneficiaries/{fid}/{aid}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BeneficiaryDto_Empd> partialUpdateBeneficiary(
        @PathVariable(value = "aid", required = false) final Long fid,
        @PathVariable(value = "fid", required = false) final Long aid,
        @NotNull @RequestBody BeneficiaryDto_Empd beneficiaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Beneficiary partially : {} and {} {} ", fid, aid, beneficiaryDTO);

        BeneficiaryId id = new BeneficiaryId(fid, aid);

        if (beneficiaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BeneficiaryDto_Empd> result = beneficiaryService.partialUpdate(beneficiaryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiaryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /beneficiaries} : get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beneficiaries in body.
     */
    @GetMapping("/beneficiaries")
    public ResponseEntity<List<BeneficiaryDto_Empd>> getAllBeneficiaries(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Beneficiaries");
        Page<BeneficiaryDto_Empd> page;
        if (eagerload) {
            page = beneficiaryService.findAllWithEagerRelationships(pageable);
        } else {
            page = beneficiaryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beneficiaries/:id} : get the "id" beneficiary.
     *
     * @param fid the first pat of composite id of the beneficiaryDTO to retrieve.
     * @param aid the second pat of composite id of the beneficiaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beneficiaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beneficiaries/{fid}/{aid}")
    public ResponseEntity<BeneficiaryDto_Empd> getBeneficiary(@PathVariable Long fid, @PathVariable Long aid) {
        log.debug("REST request to get Beneficiary : {} and {}", fid, aid);
        Optional<BeneficiaryDto_Empd> beneficiaryDTO = beneficiaryService.findOne(new BeneficiaryIdDto(fid, aid));
        return ResponseUtil.wrapOrNotFound(beneficiaryDTO);
    }

    /**
     * {@code DELETE  /beneficiaries/:id} : delete the "id" beneficiary.
     *
     * @param fid the id of the family beneficiaryDTO to delete.
     * @param aid the id of the annual beneficiaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beneficiaries/{fid}/{aid}}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long fid, @PathVariable Long aid) {
        log.debug("REST request to delete Beneficiary : {} and {}", fid, aid);
        beneficiaryService.delete(new BeneficiaryIdDto(fid, aid));
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, aid.toString() + " " + fid.toString()))
            .build();
    }
}
