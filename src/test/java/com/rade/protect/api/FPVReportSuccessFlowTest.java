package com.rade.protect.api;

import com.rade.protect.model.request.FPVReport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
public class FPVReportSuccessFlowTest extends FPVReportRestControllerTest {

    /*
     * GET /api/v1/fpvreports
     */

    @Test
    public void shouldReturnTheListOfFpvReportsRestAPI() throws Exception {
        givenFPVReportRestServiceGetAllFPVReportsReturnsListOfFPVReports();
        whenFindAllFpvReports();
        thenExpectResponseHasOkStatus();
        thenExpectFpvReportServiceFindAllFpvReportsCalledOnce();
        thenExpectResponseWithFPVReports();
    }

    /*
     * GET /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @ValueSource(longs = {1})
    public void shouldReturnTheOneFpvReportRestAPI(Long id) throws Exception {
        givenValidCreateFpvReport();
        whenFindOneFpvReport(id);
        thenExpectResponseHasOkStatus();
        thenExpectResponseWithOneFPVReport();
    }

    /*
     * POST /api/v1/fpvreports
     */

    @Test
    public void shouldCreateFpvReportCallingRestAPI() throws Exception {
        givenValidCreateFpvReport();
        whenCreateFpvReportAPICalled();
        thenExpectResponseHasCreatedStatus();
        thenExpectFpvReportServiceCreateFpvReportCalledOnce();
        thenExpectResponseWithOneFPVReport();
    }
}
