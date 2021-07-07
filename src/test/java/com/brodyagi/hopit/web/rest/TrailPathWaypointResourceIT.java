package com.brodyagi.hopit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brodyagi.hopit.IntegrationTest;
import com.brodyagi.hopit.domain.TrailPathWaypoint;
import com.brodyagi.hopit.repository.TrailPathWaypointRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TrailPathWaypointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrailPathWaypointResourceIT {

    private static final Long DEFAULT_WAYPOINT_LAT = 1L;
    private static final Long UPDATED_WAYPOINT_LAT = 2L;

    private static final Long DEFAULT_WAYPOINT_LONG = 1L;
    private static final Long UPDATED_WAYPOINT_LONG = 2L;

    private static final String ENTITY_API_URL = "/api/trail-path-waypoints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrailPathWaypointRepository trailPathWaypointRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrailPathWaypointMockMvc;

    private TrailPathWaypoint trailPathWaypoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrailPathWaypoint createEntity(EntityManager em) {
        TrailPathWaypoint trailPathWaypoint = new TrailPathWaypoint().waypointLat(DEFAULT_WAYPOINT_LAT).waypointLong(DEFAULT_WAYPOINT_LONG);
        return trailPathWaypoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrailPathWaypoint createUpdatedEntity(EntityManager em) {
        TrailPathWaypoint trailPathWaypoint = new TrailPathWaypoint().waypointLat(UPDATED_WAYPOINT_LAT).waypointLong(UPDATED_WAYPOINT_LONG);
        return trailPathWaypoint;
    }

    @BeforeEach
    public void initTest() {
        trailPathWaypoint = createEntity(em);
    }

    @Test
    @Transactional
    void createTrailPathWaypoint() throws Exception {
        int databaseSizeBeforeCreate = trailPathWaypointRepository.findAll().size();
        // Create the TrailPathWaypoint
        restTrailPathWaypointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isCreated());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeCreate + 1);
        TrailPathWaypoint testTrailPathWaypoint = trailPathWaypointList.get(trailPathWaypointList.size() - 1);
        assertThat(testTrailPathWaypoint.getWaypointLat()).isEqualTo(DEFAULT_WAYPOINT_LAT);
        assertThat(testTrailPathWaypoint.getWaypointLong()).isEqualTo(DEFAULT_WAYPOINT_LONG);
    }

    @Test
    @Transactional
    void createTrailPathWaypointWithExistingId() throws Exception {
        // Create the TrailPathWaypoint with an existing ID
        trailPathWaypoint.setId(1L);

        int databaseSizeBeforeCreate = trailPathWaypointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrailPathWaypointMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrailPathWaypoints() throws Exception {
        // Initialize the database
        trailPathWaypointRepository.saveAndFlush(trailPathWaypoint);

        // Get all the trailPathWaypointList
        restTrailPathWaypointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trailPathWaypoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].waypointLat").value(hasItem(DEFAULT_WAYPOINT_LAT.intValue())))
            .andExpect(jsonPath("$.[*].waypointLong").value(hasItem(DEFAULT_WAYPOINT_LONG.intValue())));
    }

    @Test
    @Transactional
    void getTrailPathWaypoint() throws Exception {
        // Initialize the database
        trailPathWaypointRepository.saveAndFlush(trailPathWaypoint);

        // Get the trailPathWaypoint
        restTrailPathWaypointMockMvc
            .perform(get(ENTITY_API_URL_ID, trailPathWaypoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trailPathWaypoint.getId().intValue()))
            .andExpect(jsonPath("$.waypointLat").value(DEFAULT_WAYPOINT_LAT.intValue()))
            .andExpect(jsonPath("$.waypointLong").value(DEFAULT_WAYPOINT_LONG.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTrailPathWaypoint() throws Exception {
        // Get the trailPathWaypoint
        restTrailPathWaypointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrailPathWaypoint() throws Exception {
        // Initialize the database
        trailPathWaypointRepository.saveAndFlush(trailPathWaypoint);

        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();

        // Update the trailPathWaypoint
        TrailPathWaypoint updatedTrailPathWaypoint = trailPathWaypointRepository.findById(trailPathWaypoint.getId()).get();
        // Disconnect from session so that the updates on updatedTrailPathWaypoint are not directly saved in db
        em.detach(updatedTrailPathWaypoint);
        updatedTrailPathWaypoint.waypointLat(UPDATED_WAYPOINT_LAT).waypointLong(UPDATED_WAYPOINT_LONG);

        restTrailPathWaypointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrailPathWaypoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrailPathWaypoint))
            )
            .andExpect(status().isOk());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
        TrailPathWaypoint testTrailPathWaypoint = trailPathWaypointList.get(trailPathWaypointList.size() - 1);
        assertThat(testTrailPathWaypoint.getWaypointLat()).isEqualTo(UPDATED_WAYPOINT_LAT);
        assertThat(testTrailPathWaypoint.getWaypointLong()).isEqualTo(UPDATED_WAYPOINT_LONG);
    }

    @Test
    @Transactional
    void putNonExistingTrailPathWaypoint() throws Exception {
        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();
        trailPathWaypoint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrailPathWaypointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trailPathWaypoint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrailPathWaypoint() throws Exception {
        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();
        trailPathWaypoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathWaypointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrailPathWaypoint() throws Exception {
        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();
        trailPathWaypoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathWaypointMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrailPathWaypointWithPatch() throws Exception {
        // Initialize the database
        trailPathWaypointRepository.saveAndFlush(trailPathWaypoint);

        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();

        // Update the trailPathWaypoint using partial update
        TrailPathWaypoint partialUpdatedTrailPathWaypoint = new TrailPathWaypoint();
        partialUpdatedTrailPathWaypoint.setId(trailPathWaypoint.getId());

        partialUpdatedTrailPathWaypoint.waypointLat(UPDATED_WAYPOINT_LAT);

        restTrailPathWaypointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrailPathWaypoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrailPathWaypoint))
            )
            .andExpect(status().isOk());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
        TrailPathWaypoint testTrailPathWaypoint = trailPathWaypointList.get(trailPathWaypointList.size() - 1);
        assertThat(testTrailPathWaypoint.getWaypointLat()).isEqualTo(UPDATED_WAYPOINT_LAT);
        assertThat(testTrailPathWaypoint.getWaypointLong()).isEqualTo(DEFAULT_WAYPOINT_LONG);
    }

    @Test
    @Transactional
    void fullUpdateTrailPathWaypointWithPatch() throws Exception {
        // Initialize the database
        trailPathWaypointRepository.saveAndFlush(trailPathWaypoint);

        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();

        // Update the trailPathWaypoint using partial update
        TrailPathWaypoint partialUpdatedTrailPathWaypoint = new TrailPathWaypoint();
        partialUpdatedTrailPathWaypoint.setId(trailPathWaypoint.getId());

        partialUpdatedTrailPathWaypoint.waypointLat(UPDATED_WAYPOINT_LAT).waypointLong(UPDATED_WAYPOINT_LONG);

        restTrailPathWaypointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrailPathWaypoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrailPathWaypoint))
            )
            .andExpect(status().isOk());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
        TrailPathWaypoint testTrailPathWaypoint = trailPathWaypointList.get(trailPathWaypointList.size() - 1);
        assertThat(testTrailPathWaypoint.getWaypointLat()).isEqualTo(UPDATED_WAYPOINT_LAT);
        assertThat(testTrailPathWaypoint.getWaypointLong()).isEqualTo(UPDATED_WAYPOINT_LONG);
    }

    @Test
    @Transactional
    void patchNonExistingTrailPathWaypoint() throws Exception {
        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();
        trailPathWaypoint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrailPathWaypointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trailPathWaypoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrailPathWaypoint() throws Exception {
        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();
        trailPathWaypoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathWaypointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrailPathWaypoint() throws Exception {
        int databaseSizeBeforeUpdate = trailPathWaypointRepository.findAll().size();
        trailPathWaypoint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathWaypointMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trailPathWaypoint))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrailPathWaypoint in the database
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrailPathWaypoint() throws Exception {
        // Initialize the database
        trailPathWaypointRepository.saveAndFlush(trailPathWaypoint);

        int databaseSizeBeforeDelete = trailPathWaypointRepository.findAll().size();

        // Delete the trailPathWaypoint
        restTrailPathWaypointMockMvc
            .perform(delete(ENTITY_API_URL_ID, trailPathWaypoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrailPathWaypoint> trailPathWaypointList = trailPathWaypointRepository.findAll();
        assertThat(trailPathWaypointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
