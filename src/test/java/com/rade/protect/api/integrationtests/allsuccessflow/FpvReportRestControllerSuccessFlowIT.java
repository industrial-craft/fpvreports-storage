package com.rade.protect.api.integrationtests.allsuccessflow;

import com.rade.protect.api.integrationtests.FpvReportRestControllerIT;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class FpvReportRestControllerSuccessFlowIT extends FpvReportRestControllerIT {

    @ParameterizedTest
    @ValueSource(longs = 1L)
    public void shouldUpdateFpvReport(Long id) throws Exception {
        givenUpdatedFpvReportRequest();
        whenUpdatedFpvReport(id);
        thenExpectResponseHasOkStatus();
        andThenExpectCorrectUpdatedFpvReportResponseData();
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    public void shouldFindFpvReportWhenIdExists(Long id) throws Exception {
        whenFindFpvReportById(id);
        thenExpectResponseHasOkStatus();
        andThenExpectCorrectCreatedFpvReportResponseData();
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    public void shouldDeleteFpvReportWhenIdExists(Long id) throws Exception {
        whenDeleteFpvReportById(id);
        thenExpectResponseHasOkStatus();
        andThenExpectCorrectMessageIfFpvReportIsDeletedByIdInResponse(id);
    }

    @Test
    public void shouldFindAllFpvReportByIds() throws Exception {
        givenAnotherValidFpvReportRequest();
        whenCreateFpvReport();
        thenExpectResponseHasCreatedStatus();
        whenFindAllFpvReports();
        thenExpectResponseHasOkStatus();
        thenExpectResponseWithFPVReports();
    }

    @ParameterizedTest
    @MethodSource("fpvReportIdsAndMessages")
    public void shouldDeleteFpvReportsByIds(List<Long> ids, String actualResponseMessage, int actualResponseStatusCode) throws Exception {
        givenAnotherValidFpvReportRequest();
        whenCreateFpvReport();
        thenExpectResponseHasCreatedStatus();
        givenFpvReportsByIds(ids);
        whenDeleteAllByIdsByIdsAPICalled();
        thenExpectResponseHasOkStatus();
        andThenExpectCorrectMessageIfFpvReportsAreDeletedByIdsInResponse(ids);
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
