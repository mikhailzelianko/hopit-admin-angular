package com.brodyagi.hopit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brodyagi.hopit.IntegrationTest;
import com.brodyagi.hopit.domain.TrailPath;
import com.brodyagi.hopit.repository.TrailPathRepository;
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
 * Integration tests for the {@link TrailPathResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrailPathResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trail-paths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrailPathRepository trailPathRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrailPathMockMvc;

    private TrailPath trailPath;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrailPath createEntity(EntityManager em) {
        TrailPath trailPath = new TrailPath().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return trailPath;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrailPath createUpdatedEntity(EntityManager em) {
        TrailPath trailPath = new TrailPath().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return trailPath;
    }

    @BeforeEach
    public void initTest() {
        trailPath = createEntity(em);
    }

    @Test
    @Transactional
    void createTrailPath() throws Exception {
        int databaseSizeBeforeCreate = trailPathRepository.findAll().size();
        // Create the TrailPath
        restTrailPathMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trailPath)))
            .andExpect(status().isCreated());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeCreate + 1);
        TrailPath testTrailPath = trailPathList.get(trailPathList.size() - 1);
        assertThat(testTrailPath.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTrailPath.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTrailPathWithExistingId() throws Exception {
        // Create the TrailPath with an existing ID
        trailPath.setId(1L);

        int databaseSizeBeforeCreate = trailPathRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrailPathMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trailPath)))
            .andExpect(status().isBadRequest());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailPathRepository.findAll().size();
        // set the field null
        trailPath.setTitle(null);

        // Create the TrailPath, which fails.

        restTrailPathMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trailPath)))
            .andExpect(status().isBadRequest());

        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrailPaths() throws Exception {
        // Initialize the database
        trailPathRepository.saveAndFlush(trailPath);

        // Get all the trailPathList
        restTrailPathMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trailPath.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTrailPath() throws Exception {
        // Initialize the database
        trailPathRepository.saveAndFlush(trailPath);

        // Get the trailPath
        restTrailPathMockMvc
            .perform(get(ENTITY_API_URL_ID, trailPath.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trailPath.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingTrailPath() throws Exception {
        // Get the trailPath
        restTrailPathMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrailPath() throws Exception {
        // Initialize the database
        trailPathRepository.saveAndFlush(trailPath);

        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();

        // Update the trailPath
        TrailPath updatedTrailPath = trailPathRepository.findById(trailPath.getId()).get();
        // Disconnect from session so that the updates on updatedTrailPath are not directly saved in db
        em.detach(updatedTrailPath);
        updatedTrailPath.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restTrailPathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrailPath.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrailPath))
            )
            .andExpect(status().isOk());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
        TrailPath testTrailPath = trailPathList.get(trailPathList.size() - 1);
        assertThat(testTrailPath.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTrailPath.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTrailPath() throws Exception {
        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();
        trailPath.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrailPathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trailPath.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trailPath))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrailPath() throws Exception {
        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();
        trailPath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trailPath))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrailPath() throws Exception {
        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();
        trailPath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trailPath)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrailPathWithPatch() throws Exception {
        // Initialize the database
        trailPathRepository.saveAndFlush(trailPath);

        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();

        // Update the trailPath using partial update
        TrailPath partialUpdatedTrailPath = new TrailPath();
        partialUpdatedTrailPath.setId(trailPath.getId());

        restTrailPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrailPath.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrailPath))
            )
            .andExpect(status().isOk());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
        TrailPath testTrailPath = trailPathList.get(trailPathList.size() - 1);
        assertThat(testTrailPath.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTrailPath.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTrailPathWithPatch() throws Exception {
        // Initialize the database
        trailPathRepository.saveAndFlush(trailPath);

        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();

        // Update the trailPath using partial update
        TrailPath partialUpdatedTrailPath = new TrailPath();
        partialUpdatedTrailPath.setId(trailPath.getId());

        partialUpdatedTrailPath.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restTrailPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrailPath.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrailPath))
            )
            .andExpect(status().isOk());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
        TrailPath testTrailPath = trailPathList.get(trailPathList.size() - 1);
        assertThat(testTrailPath.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTrailPath.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTrailPath() throws Exception {
        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();
        trailPath.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrailPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trailPath.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trailPath))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrailPath() throws Exception {
        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();
        trailPath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trailPath))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrailPath() throws Exception {
        int databaseSizeBeforeUpdate = trailPathRepository.findAll().size();
        trailPath.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailPathMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trailPath))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrailPath in the database
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrailPath() throws Exception {
        // Initialize the database
        trailPathRepository.saveAndFlush(trailPath);

        int databaseSizeBeforeDelete = trailPathRepository.findAll().size();

        // Delete the trailPath
        restTrailPathMockMvc
            .perform(delete(ENTITY_API_URL_ID, trailPath.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrailPath> trailPathList = trailPathRepository.findAll();
        assertThat(trailPathList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
