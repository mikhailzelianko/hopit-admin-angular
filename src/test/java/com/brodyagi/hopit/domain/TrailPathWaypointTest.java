package com.brodyagi.hopit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brodyagi.hopit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrailPathWaypointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrailPathWaypoint.class);
        TrailPathWaypoint trailPathWaypoint1 = new TrailPathWaypoint();
        trailPathWaypoint1.setId(1L);
        TrailPathWaypoint trailPathWaypoint2 = new TrailPathWaypoint();
        trailPathWaypoint2.setId(trailPathWaypoint1.getId());
        assertThat(trailPathWaypoint1).isEqualTo(trailPathWaypoint2);
        trailPathWaypoint2.setId(2L);
        assertThat(trailPathWaypoint1).isNotEqualTo(trailPathWaypoint2);
        trailPathWaypoint1.setId(null);
        assertThat(trailPathWaypoint1).isNotEqualTo(trailPathWaypoint2);
    }
}
