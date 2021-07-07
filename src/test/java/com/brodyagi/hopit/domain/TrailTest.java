package com.brodyagi.hopit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brodyagi.hopit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trail.class);
        Trail trail1 = new Trail();
        trail1.setId(1L);
        Trail trail2 = new Trail();
        trail2.setId(trail1.getId());
        assertThat(trail1).isEqualTo(trail2);
        trail2.setId(2L);
        assertThat(trail1).isNotEqualTo(trail2);
        trail1.setId(null);
        assertThat(trail1).isNotEqualTo(trail2);
    }
}
