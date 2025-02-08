package com.rade.protect.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

@Slf4j
public class SaveFPVReportValidationTest extends FPVReportRestControllerTest {

    /*
     * POST /api/v1/fpvreports
     */

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithNullSerialNumberCallingRestAPI(String fpvSerialNumber) throws Exception {
        givenRequestWithInvalidSerialNumber(fpvSerialNumber);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    private void givenRequestWithInvalidSerialNumber(String fpvSerialNumber) {
        givenValidCreatedFpvReport();
        fpvReportRequest.getFpvDrone().setFpvSerialNumber(fpvSerialNumber);
    }

}
