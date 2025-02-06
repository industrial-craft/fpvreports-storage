package com.rade.protect.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class SaveFPVReportValidationTest extends FPVReportRestControllerTest {

    /*
     * POST /api/v1/fpvreports
     */

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithNullSerialNumberCallingRestAPI(String fpvSerialNumber) throws Exception {
        givenRequestWithInvalidSerialNumber(fpvSerialNumber);
        whenCreateFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceCreateFPVReport();
        thenExpectResponseHasBadRequestStatus();
    }

    private void givenRequestWithInvalidSerialNumber(String fpvSerialNumber) {
        givenValidCreatedFpvReport();
        fpvReport.getFpvDrone().setFpvSerialNumber(fpvSerialNumber);
    }

}
