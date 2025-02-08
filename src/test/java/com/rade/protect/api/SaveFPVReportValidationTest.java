package com.rade.protect.api;

import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.request.FPVDroneRequest;
import com.rade.protect.model.request.FPVPilotRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Slf4j
public class SaveFPVReportValidationTest extends FPVReportRestControllerTest {

    /*
     * POST /api/v1/fpvreports
     */

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvSerialNumberCallingRestAPI(String fpvSerialNumber) throws Exception {
        givenRequestWithInvalidFpvSerialNumber(fpvSerialNumber);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvCraftNameCallingRestAPI(String fpvCraftName) throws Exception {
        givenRequestWithInvalidFpvCraftName(fpvCraftName);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidFpvModelCallingRestAPI(String fpvModel) throws Exception {
        givenRequestWithInvalidFpvModel(fpvModel);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidFpvDroneCallingRestAPI(String fpvDrone) throws Exception {
        givenRequestWithInvalidFpvDrone(fpvDrone);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidDateTimeFlightCallingRestAPI(String dateTimeFlight) throws Exception {
        givenRequestWithInvalidDateAndTime(dateTimeFlight);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvPilotNameCallingRestAPI(String fpvPilotName) throws Exception {
        givenRequestWithInvalidFpvPilotName(fpvPilotName);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvPilotLastNameCallingRestAPI(String fpvPilotLastName) throws Exception {
        givenRequestWithInvalidFpvPilotLastName(fpvPilotLastName);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidFpvPilotCallingRestAPI(String fpvPilot) throws Exception {
        givenRequestWithInvalidFpvPilot(fpvPilot);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidCoordinatesMGRSCallingRestAPI(String coordinatesMGRS) throws Exception {
        givenRequestWithInvalidCoordinatesMGRS(coordinatesMGRS);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidAdditionalInfoCallingRestAPI(String additionalInfo) throws Exception {
        givenRequestWithInvalidAdditionalInfo(additionalInfo);
        whenSaveFpvReportAPICalled();
        thenExpectNoCallToFPVReportRestServiceSave();
        thenExpectResponseHasBadRequestStatus();
    }



    private void givenRequestWithInvalidFpvSerialNumber(String fpvSerialNumber) {
        givenValidCreatedFpvReport();
        fpvReportRequest.getFpvDrone().setFpvSerialNumber(fpvSerialNumber);
    }

    private void givenRequestWithInvalidFpvCraftName(String fpvCraftName) {
        givenValidCreatedFpvReport();
        fpvReportRequest.getFpvDrone().setFpvCraftName(fpvCraftName);
    }

    private void givenRequestWithInvalidFpvModel(String fpvModel) {
        givenValidCreatedFpvReport();
        if (fpvModel == null || fpvModel.isBlank()) {
            fpvReportRequest.getFpvDrone().setFpvModel(null);
        } else {
            fpvReportRequest.getFpvDrone().setFpvModel(Enum.valueOf(FPVDrone.FPVModel.class, fpvModel));
        }
    }

    private void givenRequestWithInvalidFpvDrone(String fpvDrone) {
        givenValidCreatedFpvReport();
        if (fpvDrone == null) {
            fpvReportRequest.setFpvDrone(null);
        } else {
            fpvReportRequest.setFpvDrone(new FPVDroneRequest());
        }

    }

    private void givenRequestWithInvalidDateAndTime(String dateTimeFlight) {
        givenValidCreatedFpvReport();
        if (dateTimeFlight == null || dateTimeFlight.isBlank()) {
            fpvReportRequest.setDateTimeFlight(null);
        } else {
            fpvReportRequest.setDateTimeFlight(LocalDateTime.parse(dateTimeFlight));
        }
    }

    private void givenRequestWithInvalidFpvPilotName(String fpvPilotName) {
        givenValidCreatedFpvReport();
        fpvReportRequest.getFpvPilot().setName(fpvPilotName);
    }

    private void givenRequestWithInvalidFpvPilotLastName(String fpvPilotLastName) {
        givenValidCreatedFpvReport();
        fpvReportRequest.getFpvPilot().setLastName(fpvPilotLastName);
    }

    private void givenRequestWithInvalidFpvPilot(String fpvPilot) {
        givenValidCreatedFpvReport();
        if (fpvPilot == null) {
            fpvReportRequest.setFpvPilot(null);
        } else {
            fpvReportRequest.setFpvPilot(new FPVPilotRequest());
        }
    }

    private void givenRequestWithInvalidCoordinatesMGRS(String coordinatesMGRS) {
        givenValidCreatedFpvReport();
        fpvReportRequest.setCoordinatesMGRS(coordinatesMGRS);
    }

    private void givenRequestWithInvalidAdditionalInfo(String additionalInfo) {
        givenValidCreatedFpvReport();
        fpvReportRequest.setAdditionalInfo(additionalInfo);
    }

    public static class StringParams {

        static Stream<String> blankStrings() {
            return Stream.of(null, "", "  ");
        }
    }

}
