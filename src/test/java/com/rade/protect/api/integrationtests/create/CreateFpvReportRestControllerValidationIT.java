package com.rade.protect.api.integrationtests.create;

import com.rade.protect.api.integrationtests.FpvReportRestControllerIT;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

public class CreateFpvReportRestControllerValidationIT extends FpvReportRestControllerIT {

    /*
     * POST /api/v1/fpvreports
     */

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvSerialNumberCallingRestAPI(String fpvSerialNumber) throws Exception {
        givenRequestWithInvalidFpvSerialNumber(fpvSerialNumber);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvCraftNameCallingRestAPI(String fpvCraftName) throws Exception {
        givenRequestWithInvalidFpvCraftName(fpvCraftName);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidFpvModelCallingRestAPI(String fpvModel) throws Exception {
        givenRequestWithInvalidFpvModel(fpvModel);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidFpvDroneCallingRestAPI(String fpvDrone) throws Exception {
        givenRequestWithInvalidFpvDrone(fpvDrone);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidDateTimeFlightCallingRestAPI(String dateTimeFlight) throws Exception {
        givenRequestWithInvalidDateAndTime(dateTimeFlight);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvPilotNameCallingRestAPI(String fpvPilotName) throws Exception {
        givenRequestWithInvalidFpvPilotName(fpvPilotName);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidFpvPilotLastNameCallingRestAPI(String fpvPilotLastName) throws Exception {
        givenRequestWithInvalidFpvPilotLastName(fpvPilotLastName);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    public void shouldNotCreateFpvReportWithInvalidFpvPilotCallingRestAPI(String fpvPilot) throws Exception {
        givenRequestWithInvalidFpvPilot(fpvPilot);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidCoordinatesMGRSCallingRestAPI(String coordinatesMGRS) throws Exception {
        givenRequestWithInvalidCoordinatesMGRS(coordinatesMGRS);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void shouldNotCreateFpvReportWithInvalidAdditionalInfoCallingRestAPI(String additionalInfo) throws Exception {
        givenRequestWithInvalidAdditionalInfo(additionalInfo);
        whenCreateFpvReport();
        thenExpectResponseHasBadRequestStatus();
    }

}
