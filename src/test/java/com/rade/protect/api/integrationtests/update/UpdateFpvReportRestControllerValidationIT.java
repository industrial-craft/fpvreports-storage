package com.rade.protect.api.integrationtests.update;

import com.rade.protect.api.integrationtests.FpvReportRestControllerIT;
import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.request.FPVDroneRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

public class UpdateFpvReportRestControllerValidationIT extends FpvReportRestControllerIT {

    /*
     * PUT /api/v1/fpvreports/{id}
     */

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotUpdateFpvReportWithInvalidFpvSerialNumber(String fpvSerialNumber) throws Exception {
        givenUpdatedRequestWithInvalidFpvSerialNumber(fpvSerialNumber);
        whenUpdatedFpvReport(1L);
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotUpdateFpvReportWithInvalidFpvCraftName(String fpvCraftName) throws Exception {
        givenUpdatedRequestWithInvalidFpvCraftName(fpvCraftName);
        whenUpdatedFpvReport(1L);
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotUpdateFpvReportWithInvalidFpvModel(String fpvModel) throws Exception {
        givenUpdatedRequestWithInvalidFpvModel(fpvModel);
        whenUpdatedFpvReport(1L);
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotUpdateFpvReportWithInvalidFpvDrone(String fpvDrone) throws Exception {
        givenUpdatedRequestWithInvalidFpvDrone(fpvDrone);
        whenUpdatedFpvReport(1L);
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotUpdateFpvReportWithInvalidDateTimeFlight(String dateTimeFlight) throws Exception {
        givenUpdatedRequestWithInvalidDateAndTime(dateTimeFlight);
        whenUpdatedFpvReport(1L);
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidCoordinatesMGRS(String coordinatesMGRS) throws Exception {
        givenUpdatedRequestWithInvalidCoordinatesMGRS(coordinatesMGRS);
        whenUpdatedFpvReport(1L);
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidAdditionalInfo(String additionalInfo) throws Exception {
        givenUpdatedRequestWithInvalidAdditionalInfo(additionalInfo);
        whenUpdatedFpvReport(1L);
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @ValueSource(longs = {2L})
    public void shouldNotUpdateFpvReportWithInvalidId(Long id) throws Exception {
        givenUpdatedRequestWithInvalidId();
        whenUpdatedFpvReport(id);
        thenExpectResponseNotFoundStatus();
    }

    private void givenUpdatedRequestWithInvalidId() {
        givenUpdatedFpvReportRequest();
    }

    private void givenUpdatedRequestWithInvalidFpvSerialNumber(String fpvSerialNumber) {
        givenUpdatedFpvReportRequest();
        fpvReportUpdateRequest.getFpvDrone().setFpvSerialNumber(fpvSerialNumber);
    }

    private void givenUpdatedRequestWithInvalidFpvCraftName(String fpvCraftName) {
        givenUpdatedFpvReportRequest();
        fpvReportUpdateRequest.getFpvDrone().setFpvCraftName(fpvCraftName);
    }

    private void givenUpdatedRequestWithInvalidFpvModel(String fpvModel) {
        givenUpdatedFpvReportRequest();
        if (fpvModel == null || fpvModel.isBlank()) {
            fpvReportUpdateRequest.getFpvDrone().setFpvModel(null);
        } else {
            fpvReportUpdateRequest.getFpvDrone().setFpvModel(Enum.valueOf(FPVDrone.FPVModel.class, fpvModel));
        }
    }

    private void givenUpdatedRequestWithInvalidFpvDrone(String fpvDrone) {
        givenUpdatedFpvReportRequest();
        if (fpvDrone == null) {
            fpvReportUpdateRequest.setFpvDrone(null);
        } else {
            fpvReportUpdateRequest.setFpvDrone(new FPVDroneRequest());
        }
    }

    private void givenUpdatedRequestWithInvalidDateAndTime(String dateTimeFlight) {
        givenUpdatedFpvReportRequest();
        if (dateTimeFlight == null || dateTimeFlight.isBlank()) {
            fpvReportUpdateRequest.setDateTimeFlight(null);
        } else {
            fpvReportUpdateRequest.setDateTimeFlight(LocalDateTime.parse(dateTimeFlight));
        }
    }

    private void givenUpdatedRequestWithInvalidCoordinatesMGRS(String coordinatesMGRS) {
        givenUpdatedFpvReportRequest();
        fpvReportUpdateRequest.setCoordinatesMGRS(coordinatesMGRS);
    }

    private void givenUpdatedRequestWithInvalidAdditionalInfo(String additionalInfo) {
        givenUpdatedFpvReportRequest();
        fpvReportUpdateRequest.setAdditionalInfo(additionalInfo);
    }
}
