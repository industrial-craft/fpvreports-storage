package com.rade.protect.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.rade.protect.model.request.FPVDrone;
import com.rade.protect.model.request.FPVPilot;
import com.rade.protect.model.request.FPVReport;
import com.rade.protect.model.request.FPVReportIds;
import com.rade.protect.model.response.FPVReportResponse;
import com.rade.protect.service.FPVReportRestService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class FPVReportRestControllerTest {

    protected final static String FPVREPORTS_API_PATH = "/api/v1/fpvreports";

    private static ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private FPVReportRestService fpvReportRestService;

    protected MockHttpServletResponse response;

    protected FPVReport fpvReport;

    protected FPVReport fpvReport1;

    protected FPVReportResponse fpvReportResponse;

    protected FPVReportResponse fpvReportResponse1;

    protected FPVReportIds fpvReportIds;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /*
     * GIVEN Methods
     */

    protected void givenValidCreatedFpvReport() {
        FPVDrone fpvDrone = FPVDrone.builder()
                .fpvSerialNumber("SN743")
                .fpvCraftName("Trainspotting")
                .fpvModel(FPVDrone.FPVModel.KAMIKAZE)
                .build();
        FPVPilot fpvPilot = FPVPilot.builder()
                .name("Mark")
                .lastName("Renton")
                .build();

        fpvReport = FPVReport.builder()
                .fpvReportId(1L)
                .fpvDrone(fpvDrone)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();

        fpvReportResponse = FPVReportResponse.builder()
                .fpvReportId(fpvReport.getFpvReportId())
                .fpvDrone(fpvDrone)
                .dateTimeFlight(fpvReport.getDateTimeFlight())
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(fpvReport.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport.getCoordinatesMGRS())
                .additionalInfo(fpvReport.getAdditionalInfo())
                .build();
    }

    protected void givenValidCreatedFpvReports() {
        fpvReport = getFpvReport();
        fpvReport1 = getFpvReport1();

        doReturn(fpvReport).when(fpvReportRestService).save(any(FPVReport.class));
        doReturn(fpvReport1).when(fpvReportRestService).save(any(FPVReport.class));
    }

    protected FPVReport getFpvReport() {
        FPVDrone fpvDrone = FPVDrone.builder()
                .fpvSerialNumber("SN743")
                .fpvCraftName("Trainspotting")
                .fpvModel(FPVDrone.FPVModel.KAMIKAZE)
                .build();

        FPVPilot fpvPilot = FPVPilot.builder()
                .name("Mark")
                .lastName("Renton")
                .build();

        return FPVReport.builder()
                .fpvReportId(1L)
                .fpvDrone(fpvDrone)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();
    }

    protected FPVReport getFpvReport1() {
        FPVDrone fpvDrone1 = FPVDrone.builder()
                .fpvSerialNumber("SN7564")
                .fpvCraftName("Kolibri")
                .fpvModel(FPVDrone.FPVModel.BOMBER)
                .build();

        FPVPilot fpvPilot1 = FPVPilot.builder()
                .name("Austin")
                .lastName("Powers")
                .build();

        return FPVReport.builder()
                .fpvReportId(2L)
                .fpvDrone(fpvDrone1)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 20, 0))
                .fpvPilot(fpvPilot1)
                .isLostFPVDueToREB(true)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 12621 83048")
                .additionalInfo("h: 245")
                .build();
    }

    protected void givenFPVReportRestServiceFindFpvReportById(Long id) {
        doReturn(Optional.of(fpvReport)).when(fpvReportRestService).findById(id);
    }

    protected void givenFPVReportRestServiceFindByIdReturnsNotFound(Long id) {
        doReturn(Optional.empty()).when(fpvReportRestService).findById(id);
    }

    protected void givenFPVReportRestServiceCreateFPVReport() {
        doReturn(fpvReport).when(fpvReportRestService).save(any(FPVReport.class));
    }

    protected void givenFPVReportRestServiceGetAllFPVReportsReturnsListOfFPVReports() {
        fpvReportResponse = FPVReportResponse.builder()
                .fpvReportId(fpvReport.getFpvReportId())
                .fpvDrone(fpvReport.getFpvDrone())
                .dateTimeFlight(fpvReport.getDateTimeFlight())
                .fpvPilot(fpvReport.getFpvPilot())
                .isLostFPVDueToREB(fpvReport.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport.getCoordinatesMGRS())
                .additionalInfo(fpvReport.getAdditionalInfo())
                .build();

        fpvReportResponse1 = FPVReportResponse.builder()
                .fpvReportId(fpvReport1.getFpvReportId())
                .fpvDrone(fpvReport1.getFpvDrone())
                .dateTimeFlight(fpvReport1.getDateTimeFlight())
                .fpvPilot(fpvReport1.getFpvPilot())
                .isLostFPVDueToREB(fpvReport1.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport1.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport1.getCoordinatesMGRS())
                .additionalInfo(fpvReport1.getAdditionalInfo())
                .build();

        doReturn(List.of(fpvReport, fpvReport1)).when(fpvReportRestService).findAll();
    }

    protected void givenFPVReportRestServiceDeleteFpvReportById(Long id) {
        doNothing().when(fpvReportRestService).deleteById(id);
    }

    protected void givenFPVReportRestServiceUpdateFpvReport(Long id) {
        fpvReport1 = getFpvReport1();

        fpvReportResponse1 = FPVReportResponse.builder()
                .fpvReportId(fpvReport1.getFpvReportId())
                .fpvDrone(fpvReport1.getFpvDrone())
                .dateTimeFlight(fpvReport1.getDateTimeFlight())
                .fpvPilot(fpvReport1.getFpvPilot())
                .isLostFPVDueToREB(fpvReport1.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport1.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport1.getCoordinatesMGRS())
                .additionalInfo(fpvReport1.getAdditionalInfo())
                .build();

        when(fpvReportRestService.updateFPVReport(eq(id), any(FPVReport.class)))
                .thenReturn(fpvReport1);
    }

    protected void givenFpvReportRestServiceDeleteFpvReportsByIds(List<Long> ids) {
        fpvReportIds = new FPVReportIds();
        fpvReportIds.setFpvReportIds(ids);
        doNothing().when(fpvReportRestService).deleteAllByIds(ids);
    }

    protected void givenFPVReportRestServiceFindAllByIdsReturnsNotFound() {
        when(fpvReportRestService.findAll()).thenReturn(Collections.emptyList());
    }

    /*
     * WHEN Methods
     */

    protected void whenFindOneFpvReport(Long id) throws Exception {
        response = mockMvc.perform(get(FPVREPORTS_API_PATH + '/' + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenFindAllFpvReports() throws Exception {
        response = mockMvc.perform(get(FPVREPORTS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenCreateFpvReportAPICalled() throws Exception {
        response = mockMvc.perform(post(FPVREPORTS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvReport)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenUpdatedFpvReportAPICalled(Long id) throws Exception {
        response = mockMvc.perform(put(FPVREPORTS_API_PATH + '/' + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvReport1)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenDeleteFpvReportAPICalled(Long id) throws Exception {
        response = mockMvc.perform(delete(FPVREPORTS_API_PATH + '/' + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenDeleteFpvReportsByIdsAPICalled() throws Exception {
        response = mockMvc.perform(delete(FPVREPORTS_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvReportIds)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    /*
     * THEN Methods
     */

    protected void thenExpectResponseHasOkStatus() {
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    protected void thenExpectResponseHasBadRequestStatus() {
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    protected void thenExpectResponseHasCreatedStatus() {
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    protected void thenExpectResponseNotFoundStatus() {
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    protected void thenExpectResponseWithFPVReports() throws UnsupportedEncodingException {
        List<FPVReportResponse> actualFindAllFindFpvReportsResponse = stringJsonToList(response.getContentAsString(), FPVReportResponse.class);
        assertNotNull(actualFindAllFindFpvReportsResponse);
        assertEquals(2, actualFindAllFindFpvReportsResponse.size());

        assertEquals(1L, actualFindAllFindFpvReportsResponse.get(0).getFpvReportId());
        assertEquals("Trainspotting", actualFindAllFindFpvReportsResponse.get(0).getFpvDrone().getFpvCraftName());

        assertEquals(2L, actualFindAllFindFpvReportsResponse.get(1).getFpvReportId());
        assertEquals("Kolibri", actualFindAllFindFpvReportsResponse.get(1).getFpvDrone().getFpvCraftName());
    }

    protected void thenExpectResponseWithOneCreatedFpvReport() throws UnsupportedEncodingException {
        FPVReportResponse actualFindOneFPVReportResponse = stringJsonToObject(response.getContentAsString(), FPVReportResponse.class);
        assertNotNull(actualFindOneFPVReportResponse);
        assertEquals(fpvReportResponse, actualFindOneFPVReportResponse);
    }

    protected void thenExpectResponseWithOneUpdatedFpvReport() throws UnsupportedEncodingException {
        FPVReportResponse actualFindOneFPVReportResponse = stringJsonToObject(response.getContentAsString(), FPVReportResponse.class);
        assertNotNull(actualFindOneFPVReportResponse);
        assertEquals(fpvReportResponse1, actualFindOneFPVReportResponse);
    }

    protected void thenExpectResponseWithMessageAndStatusCode(int actualResponseStatusCode, String actualResponseMessage) throws UnsupportedEncodingException {
        String responseBody = response.getContentAsString();
        AppError appError = new Gson().fromJson(responseBody, AppError.class);
        assertEquals(appError.getStatusCode(), actualResponseStatusCode);
        assertEquals(appError.getMessage(), actualResponseMessage);
    }

    protected void thenExpectNoCallToFPVReportRestServiceCreateFPVReport() {
        verify(fpvReportRestService, times(0)).save(any(FPVReport.class));
    }

    protected void thenExpectFpvReportServiceFindAllFpvReportsCalledOnce() {
        verify(fpvReportRestService, times(1)).findAll();
    }

    protected void thenExpectFpvReportServiceCreateFpvReportCalledOnce() {
        verify(fpvReportRestService, times(1)).save(any(FPVReport.class));
    }

    protected void thenExpectFpvReportServiceDeleteFpvReportCalledOnce(Long id) {
        verify(fpvReportRestService, times(1)).deleteById(id);
    }

    protected void thenExpectFpvReportServiceDeleteFpvReportsByIdsCalledOnce(List<Long> ids) {
        verify(fpvReportRestService, times(1)).deleteAllByIds(ids);
    }

    @SneakyThrows
    protected <T> List<T> stringJsonToList(String json, Class<T> clazz) {
        return mapper.readValue(json, new TypeReference<>() {
            @Override
            public Type getType() {
                return mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            }
        });
    }

    @SneakyThrows
    protected <T> T stringJsonToObject(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }

    @SneakyThrows
    protected String objectToJsonString(Object object) {
        return mapper.writeValueAsString(object);
    }
}