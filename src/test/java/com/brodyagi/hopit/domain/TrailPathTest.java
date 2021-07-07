package com.brodyagi.hopit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brodyagi.hopit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrailPathTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrailPath.class);
        TrailPath trailPath1 = new TrailPath();
        trailPath1.setId(1L);
        TrailPath trailPath2 = new TrailPath();
        trailPath2.setId(trailPath1.getId());
        assertThat(trailPath1).isEqualTo(trailPath2);
        trailPath2.setId(2L);
        assertThat(trailPath1).isNotEqualTo(trailPath2);
        trailPath1.setId(null);
        assertThat(trailPath1).isNotEqualTo(trailPath2);
    }
}
