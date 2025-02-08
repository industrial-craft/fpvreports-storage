package com.rade.protect.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class FPVReportsSuccessFlowTest extends FPVReportRestControllerTest {

    /*
     * GET /api/v1/fpvreports
     */

    @Test
    public void shouldReturnTheListOfFpvReportsRestAPI() throws Exception {
        givenValidCreatedFpvReports();
        givenFPVReportServiceImplSave();
        givenFPVReportRestServiceGetAllFPVReportsReturnsListOfFPVReports();
        whenFindAllFpvReports();
        thenExpectResponseHasOkStatus();
        thenExpectFpvReportServiceFindAllFpvReportsCalledOnce();
        thenExpectResponseWithFPVReports();
    }

    /*
     * DELETE /api/v1/fpvreports
     */

    @ParameterizedTest
    @MethodSource("fpvReportIdsAndMessages")
    public void shouldDeleteFpvReportsByIdsCallingRestAPI(List<Long> ids, String actualResponseMessage, int actualResponseStatusCode) throws Exception {
        givenValidCreatedFpvReports();
        givenFPVReportServiceImplSave();
        givenFPVReportRestServiceGetAllFPVReportsReturnsListOfFPVReports();

        givenFpvReportRestServiceDeleteAllByIdsByIds(ids);
        whenDeleteAllByIdsByIdsAPICalled();
        thenExpectResponseHasOkStatus();
        thenExpectFpvReportServiceDeleteAllByIdsByIdsCalledOnce(ids);

        givenFPVReportRestServiceFindAllByIdsReturnsNotFound();
        whenFindAllFpvReports();
        thenExpectResponseHasOkStatus();
        thenExpectResponseWithMessageAndStatusCode(actualResponseStatusCode, actualResponseMessage);
    }

    private static Stream<Arguments> fpvReportIdsAndMessages() {
        return Stream.of(
                Arguments.of(List.of(1L, 2L), "No FPVReports are available!", 200)
        );
    }

}
