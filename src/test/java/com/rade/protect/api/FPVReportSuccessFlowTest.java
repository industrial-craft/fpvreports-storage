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
        givenFPVReportServiceImplSave();
        whenSaveFpvReportAPICalled();
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
        thenExpectResponseWithOneSavedFpvReport();
    }

    /*
     * POST /api/v1/fpvreports
     */

    @Test
    public void shouldCreateFpvReportCallingRestAPI() throws Exception {
        thenExpectFpvReportServiceSaveCalledOnce();
        thenExpectResponseWithOneSavedFpvReport();
    }

    /*
     * PUT /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @ValueSource(longs = {1L})
    public void shouldUpdateFpvReportCallingRestAPI(Long id) throws Exception {
        givenFPVReportRestServiceUpdate(id);
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
        givenFPVReportRestServiceDeleteByIdById(id);
        givenFPVReportRestServiceFindFpvReportById(id);
        whenDeleteByIdAPICalled(id);
        thenExpectResponseHasOkStatus();
        thenExpectFpvReportServiceDeleteByIdCalledOnce(id);

        givenFPVReportRestServiceFindByIdReturnsNotFound(id);
        whenFindOneFpvReport(id);
        thenExpectResponseNotFoundStatus();
    }

}
