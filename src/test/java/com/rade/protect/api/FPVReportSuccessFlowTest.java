package com.rade.protect.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
public class FPVReportSuccessFlowTest extends FPVReportRestControllerTest {

    @BeforeEach
    void setUp() throws Exception {
        givenValidCreatedFpvReport();
        givenFPVReportRestServiceCreateFPVReport();
        whenCreateFpvReportAPICalled();
        thenExpectResponseHasCreatedStatus();
    }

    /*
     * GET /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @ValueSource(longs = {1})
    public void shouldReturnTheOneFpvReportRestAPI(Long id) throws Exception {
        givenFPVReportRestServiceFindFpvReportById(id);
        whenFindOneFpvReport(id);
        thenExpectResponseHasOkStatus();
        thenExpectResponseWithOneCreatedFpvReport();
    }

    /*
     * POST /api/v1/fpvreports
     */

    @Test
    public void shouldCreateFpvReportCallingRestAPI() throws Exception {
        thenExpectFpvReportServiceCreateFpvReportCalledOnce();
        thenExpectResponseWithOneCreatedFpvReport();
    }

    /*
     * PUT /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @ValueSource(longs = {1L})
    public void shouldUpdateFpvReportCallingRestAPI(Long id) throws Exception {
        givenFPVReportRestServiceUpdateFpvReport(id);
        whenUpdatedFpvReportAPICalled(id);
        thenExpectResponseHasOkStatus();
        thenExpectResponseWithOneUpdatedFpvReport();
    }

    /*
     * DELETE /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @ValueSource(longs = {1L})
    public void shouldDeleteFpvReportCallingRestAPI(Long id) throws Exception {
        givenFPVReportRestServiceDeleteFpvReportById(id);
        givenFPVReportRestServiceFindFpvReportById(id);
        whenDeleteFpvReportAPICalled(id);
        thenExpectResponseHasOkStatus();
        thenExpectFpvReportServiceDeleteFpvReportCalledOnce(id);

        givenFPVReportRestServiceFindByIdReturnsNotFound(id);
        whenFindOneFpvReport(id);
        thenExpectResponseNotFoundStatus();
    }

}
