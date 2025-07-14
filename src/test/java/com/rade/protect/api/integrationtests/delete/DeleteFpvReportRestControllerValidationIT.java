package com.rade.protect.api.integrationtests.delete;

import com.rade.protect.api.integrationtests.FpvReportRestControllerIT;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

public class DeleteFpvReportRestControllerValidationIT extends FpvReportRestControllerIT {

    /*
     * DELETE /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @ValueSource(longs = {2L})
    public void shouldNotDeleteFpvReportWhenIdIsInvalid(Long id) throws Exception {
        whenDeleteFpvReportById(id);
        thenExpectResponseNotFoundStatus();
    }

    @ParameterizedTest
    @MethodSource("fpvReportIdsAndMessages")
    public void shouldNotDeleteFpvReportsByIdsWhenIdsAreInvalid(List<Long> ids, String actualResponseMessage, int actualResponseStatusCode) throws Exception {
        givenFpvReportsByIds(ids);
        whenDeleteAllByIdsByIdsAPICalled();
        thenExpectResponseNotFoundStatus();
        thenExpectResponseWithMessageAndStatusCode(actualResponseStatusCode, actualResponseMessage);
    }

    private static Stream<Arguments> fpvReportIdsAndMessages() {
        return Stream.of(
                Arguments.of(List.of(2L, 3L), "FPVReports not found for IDs: [2, 3]", 404)
        );
    }

}
