package com.rade.protect.api.integrationtests.findbyid;

import com.rade.protect.api.integrationtests.FpvReportRestControllerIT;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FindByIdFpvReportRestControllerValidationIT extends FpvReportRestControllerIT {

    /*
     * GET /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @ValueSource(longs = {2L})
    public void shouldNotFindFpvReportByIdIfIdIsInvalid(Long id) throws Exception {
        whenFindFpvReportById(id);
        thenExpectResponseNotFoundStatus();
    }
}
