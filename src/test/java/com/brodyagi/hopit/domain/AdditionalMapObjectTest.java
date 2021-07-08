package com.brodyagi.hopit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brodyagi.hopit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdditionalMapObjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdditionalMapObject.class);
        AdditionalMapObject additionalMapObject1 = new AdditionalMapObject();
        additionalMapObject1.setId(1L);
        AdditionalMapObject additionalMapObject2 = new AdditionalMapObject();
        additionalMapObject2.setId(additionalMapObject1.getId());
        assertThat(additionalMapObject1).isEqualTo(additionalMapObject2);
        additionalMapObject2.setId(2L);
        assertThat(additionalMapObject1).isNotEqualTo(additionalMapObject2);
        additionalMapObject1.setId(null);
        assertThat(additionalMapObject1).isNotEqualTo(additionalMapObject2);
    }
}
