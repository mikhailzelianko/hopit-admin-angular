package com.brodyagi.hopit.web.rest;

import com.brodyagi.hopit.domain.Trail;
import com.brodyagi.hopit.repository.TrailRepository;
import com.brodyagi.hopit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.brodyagi.hopit.domain.Trail}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrailResource {

    private final Logger log = LoggerFactory.getLogger(TrailResource.class);

    private static final String ENTITY_NAME = "trail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrailRepository trailRepository;

    public TrailResource(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    /**
     * {@code POST  /trails} : Create a new trail.
     *
     * @param trail the trail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trail, or with status {@code 400 (Bad Request)} if the trail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trails")
    public ResponseEntity<Trail> createTrail(@Valid @RequestBody Trail trail) throws URISyntaxException {
        log.debug("REST request to save Trail : {}", trail);
        if (trail.getId() != null) {
            throw new BadRequestAlertException("A new trail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trail result = trailRepository.save(trail);
        return ResponseEntity
            .created(new URI("/api/trails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trails/:id} : Updates an existing trail.
     *
     * @param id the id of the trail to save.
     * @param trail the trail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trail,
     * or with status {@code 400 (Bad Request)} if the trail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trails/{id}")
    public ResponseEntity<Trail> updateTrail(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Trail trail)
        throws URISyntaxException {
        log.debug("REST request to update Trail : {}, {}", id, trail);
        if (trail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Trail result = trailRepository.save(trail);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trail.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trails/:id} : Partial updates given fields of an existing trail, field will ignore if it is null
     *
     * @param id the id of the trail to save.
     * @param trail the trail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trail,
     * or with status {@code 400 (Bad Request)} if the trail is not valid,
     * or with status {@code 404 (Not Found)} if the trail is not found,
     * or with status {@code 500 (Internal Server Error)} if the trail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trails/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Trail> partialUpdateTrail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Trail trail
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trail partially : {}, {}", id, trail);
        if (trail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trail> result = trailRepository
            .findById(trail.getId())
            .map(
                existingTrail -> {
                    if (trail.getTitle() != null) {
                        existingTrail.setTitle(trail.getTitle());
                    }
                    if (trail.getDescription() != null) {
                        existingTrail.setDescription(trail.getDescription());
                    }
                    if (trail.getShortDescription() != null) {
                        existingTrail.setShortDescription(trail.getShortDescription());
                    }
                    if (trail.getSpecialRules() != null) {
                        existingTrail.setSpecialRules(trail.getSpecialRules());
                    }
                    if (trail.getType() != null) {
                        existingTrail.setType(trail.getType());
                    }
                    if (trail.getPrice() != null) {
                        existingTrail.setPrice(trail.getPrice());
                    }
                    if (trail.getEnterLat() != null) {
                        existingTrail.setEnterLat(trail.getEnterLat());
                    }
                    if (trail.getEnterLong() != null) {
                        existingTrail.setEnterLong(trail.getEnterLong());
                    }
                    if (trail.getFlagUnavailable() != null) {
                        existingTrail.setFlagUnavailable(trail.getFlagUnavailable());
                    }
                    if (trail.getFlagWater() != null) {
                        existingTrail.setFlagWater(trail.getFlagWater());
                    }
                    if (trail.getFlagBirdwatching() != null) {
                        existingTrail.setFlagBirdwatching(trail.getFlagBirdwatching());
                    }
                    if (trail.getFlagReligious() != null) {
                        existingTrail.setFlagReligious(trail.getFlagReligious());
                    }
                    if (trail.getFlagFishing() != null) {
                        existingTrail.setFlagFishing(trail.getFlagFishing());
                    }
                    if (trail.getFlagParking() != null) {
                        existingTrail.setFlagParking(trail.getFlagParking());
                    }
                    if (trail.getFlagWC() != null) {
                        existingTrail.setFlagWC(trail.getFlagWC());
                    }
                    if (trail.getFlagCamping() != null) {
                        existingTrail.setFlagCamping(trail.getFlagCamping());
                    }
                    if (trail.getFlagPicnic() != null) {
                        existingTrail.setFlagPicnic(trail.getFlagPicnic());
                    }
                    if (trail.getFlagStreetfood() != null) {
                        existingTrail.setFlagStreetfood(trail.getFlagStreetfood());
                    }
                    if (trail.getSource() != null) {
                        existingTrail.setSource(trail.getSource());
                    }
                    if (trail.getAdminComment() != null) {
                        existingTrail.setAdminComment(trail.getAdminComment());
                    }

                    return existingTrail;
                }
            )
            .map(trailRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trail.getId().toString())
        );
    }

    /**
     * {@code GET  /trails} : get all the trails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trails in body.
     */
    @GetMapping("/trails")
    public List<Trail> getAllTrails() {
        log.debug("REST request to get all Trails");
        return trailRepository.findAll();
    }

    /**
     * {@code GET  /trails/:id} : get the "id" trail.
     *
     * @param id the id of the trail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trails/{id}")
    public ResponseEntity<Trail> getTrail(@PathVariable Long id) {
        log.debug("REST request to get Trail : {}", id);
        Optional<Trail> trail = trailRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trail);
    }

    /**
     * {@code DELETE  /trails/:id} : delete the "id" trail.
     *
     * @param id the id of the trail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trails/{id}")
    public ResponseEntity<Void> deleteTrail(@PathVariable Long id) {
        log.debug("REST request to delete Trail : {}", id);
        trailRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
