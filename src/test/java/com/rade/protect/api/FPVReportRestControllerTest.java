package com.rade.protect.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.FPVReport;
import com.rade.protect.model.request.*;
import com.rade.protect.model.response.FPVDroneResponse;
import com.rade.protect.model.response.FPVPilotResponse;
import com.rade.protect.model.response.FPVReportResponse;
import com.rade.protect.service.impl.FPVDroneServiceImpl;
import com.rade.protect.service.impl.FPVPilotServiceImpl;
import com.rade.protect.service.impl.FPVReportServiceImpl;
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
    private FPVReportServiceImpl fpvReportServiceImpl;

    @MockBean
    private FPVDroneServiceImpl fpvDroneService;

    @MockBean
    private FPVPilotServiceImpl fpvPilotService;

    protected MockHttpServletResponse response;

    protected FPVReport fpvReport;

    protected FPVReportRequest fpvReportRequest;

    protected FPVReportRequest fpvReportRequest1;

    protected FPVReportResponse fpvReportResponse;

    protected FPVReportResponse fpvReportResponse1;

    protected FPVDroneResponse fpvDroneResponse;

    protected FPVDroneResponse fpvDroneResponse1;

    protected FPVPilotResponse fpvPilotResponse;

    protected FPVPilotResponse fpvPilotResponse1;

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
        FPVDroneRequest fpvDroneRequest = FPVDroneRequest.builder()
                .fpvSerialNumber("SN743")
                .fpvCraftName("Trainspotting")
                .fpvModel(FPVDrone.FPVModel.KAMIKAZE)
                .build();

        FPVPilotRequest fpvPilot = FPVPilotRequest.builder()
                .name("Mark")
                .lastName("Renton")
                .build();

        fpvReportRequest = FPVReportRequest.builder()
                .fpvDrone(fpvDroneRequest)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();

        fpvDroneResponse = FPVDroneResponse.builder()
                .fpvSerialNumber(fpvReportRequest.getFpvDrone().getFpvSerialNumber())
                .fpvCraftName(fpvReportRequest.getFpvDrone().getFpvCraftName())
                .fpvModel(fpvReportRequest.getFpvDrone().getFpvModel())
                .build();

        fpvPilotResponse = FPVPilotResponse.builder()
                .name(fpvReportRequest.getFpvPilot().getName())
                .lastName(fpvReportRequest.getFpvPilot().getLastName())
                .build();

        fpvReportResponse = FPVReportResponse.builder()
                .fpvReportId(1L)
                .fpvDrone(fpvDroneResponse)
                .dateTimeFlight(fpvReportRequest.getDateTimeFlight())
                .fpvPilot(fpvPilotResponse)
                .isLostFPVDueToREB(fpvReportRequest.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReportRequest.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReportRequest.getCoordinatesMGRS())
                .additionalInfo(fpvReportRequest.getAdditionalInfo())
                .build();
    }

    protected void givenValidCreatedFpvReports() {
        fpvReportRequest = getFpvReportRequest();
        fpvReportRequest1 = getFpvReportRequest1();

        doReturn(fpvReportResponse).when(fpvReportServiceImpl).save(any(FPVReportRequest.class));
        doReturn(fpvReportResponse1).when(fpvReportServiceImpl).save(any(FPVReportRequest.class));
    }

    protected FPVReportRequest getFpvReportRequest() {
        FPVDroneRequest fpvDroneRequest = FPVDroneRequest.builder()
                .fpvSerialNumber("SN743")
                .fpvCraftName("Trainspotting")
                .fpvModel(FPVDrone.FPVModel.KAMIKAZE)
                .build();

        FPVPilotRequest fpvPilotRequest = FPVPilotRequest.builder()
                .name("Mark")
                .lastName("Renton")
                .build();

        return FPVReportRequest.builder()
                .fpvDrone(fpvDroneRequest)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(fpvPilotRequest)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();
    }

    protected FPVReportRequest getFpvReportRequest1() {
        FPVDroneRequest fpvDroneRequest1 = FPVDroneRequest.builder()
                .fpvSerialNumber("SN7564")
                .fpvCraftName("Kolibri")
                .fpvModel(FPVDrone.FPVModel.BOMBER)
                .build();

        FPVPilotRequest fpvPilotRequest1 = FPVPilotRequest.builder()
                .name("Austin")
                .lastName("Powers")
                .build();

        return FPVReportRequest.builder()
                .fpvDrone(fpvDroneRequest1)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 20, 0))
                .fpvPilot(fpvPilotRequest1)
                .isLostFPVDueToREB(true)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 12621 83048")
                .additionalInfo("h: 245")
                .build();
    }

    protected void givenFPVReportRestServiceFindFpvReportById(Long id) {
        doReturn(Optional.of(fpvReportResponse)).when(fpvReportServiceImpl).findById(id);
    }

    protected void givenFPVReportRestServiceFindByIdReturnsNotFound(Long id) {
        doReturn(Optional.empty()).when(fpvReportServiceImpl).findById(id);
    }

    protected void givenFPVReportServiceImplSave() {
        doReturn(fpvReportResponse).when(fpvReportServiceImpl).save(any(FPVReportRequest.class));
    }

    protected void givenFPVReportRestServiceGetAllFPVReportsReturnsListOfFPVReports() {

        fpvDroneResponse = FPVDroneResponse.builder()
                .fpvSerialNumber(fpvReportRequest.getFpvDrone().getFpvSerialNumber())
                .fpvCraftName(fpvReportRequest.getFpvDrone().getFpvCraftName())
                .fpvModel(fpvReportRequest.getFpvDrone().getFpvModel())
                .build();

        fpvPilotResponse = FPVPilotResponse.builder()
                .name(fpvReportRequest.getFpvPilot().getName())
                .lastName(fpvReportRequest.getFpvPilot().getLastName())
                .build();

        fpvDroneResponse1 = FPVDroneResponse.builder()
                .fpvSerialNumber(fpvReportRequest1.getFpvDrone().getFpvSerialNumber())
                .fpvCraftName(fpvReportRequest1.getFpvDrone().getFpvCraftName())
                .fpvModel(fpvReportRequest1.getFpvDrone().getFpvModel())
                .build();

        fpvPilotResponse1 = FPVPilotResponse.builder()
                .name(fpvReportRequest1.getFpvPilot().getName())
                .lastName(fpvReportRequest1.getFpvPilot().getLastName())
                .build();

        fpvReportResponse = FPVReportResponse.builder()
                .fpvReportId(1L)
                .fpvDrone(fpvDroneResponse)
                .dateTimeFlight(fpvReportRequest.getDateTimeFlight())
                .fpvPilot(fpvPilotResponse)
                .isLostFPVDueToREB(fpvReportRequest.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReportRequest.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReportRequest.getCoordinatesMGRS())
                .additionalInfo(fpvReportRequest.getAdditionalInfo())
                .build();

        fpvReportResponse1 = FPVReportResponse.builder()
                .fpvReportId(2L)
                .fpvDrone(fpvDroneResponse1)
                .dateTimeFlight(fpvReportRequest1.getDateTimeFlight())
                .fpvPilot(fpvPilotResponse1)
                .isLostFPVDueToREB(fpvReportRequest1.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReportRequest1.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReportRequest1.getCoordinatesMGRS())
                .additionalInfo(fpvReportRequest1.getAdditionalInfo())
                .build();

        doReturn(List.of(fpvReportResponse, fpvReportResponse1)).when(fpvReportServiceImpl).findAll();
    }

    protected void givenFPVReportRestServiceDeleteByIdById(Long id) {
        doNothing().when(fpvReportServiceImpl).deleteById(id);
    }

    protected void givenFPVReportRestServiceUpdate(Long id) {
        fpvReportRequest1 = getFpvReportRequest1();

        fpvDroneResponse1 = FPVDroneResponse.builder()
                .fpvSerialNumber(fpvReportRequest1.getFpvDrone().getFpvSerialNumber())
                .fpvCraftName(fpvReportRequest1.getFpvDrone().getFpvCraftName())
                .fpvModel(fpvReportRequest1.getFpvDrone().getFpvModel())
                .build();

        fpvPilotResponse1 = FPVPilotResponse.builder()
                .name(fpvReportRequest1.getFpvPilot().getName())
                .lastName(fpvReportRequest1.getFpvPilot().getLastName())
                .build();

        fpvReportResponse1 = FPVReportResponse.builder()
                .fpvReportId(2L)
                .fpvDrone(fpvDroneResponse1)
                .dateTimeFlight(fpvReportRequest1.getDateTimeFlight())
                .fpvPilot(fpvPilotResponse1)
                .isLostFPVDueToREB(fpvReportRequest1.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReportRequest1.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReportRequest1.getCoordinatesMGRS())
                .additionalInfo(fpvReportRequest1.getAdditionalInfo())
                .build();

        when(fpvReportServiceImpl.update(eq(id), any(FPVReportRequest.class))).thenReturn(fpvReportResponse1);
    }

    protected void givenFpvReportRestServiceDeleteAllByIdsByIds(List<Long> ids) {
        fpvReportIds = new FPVReportIds();
        fpvReportIds.setFpvReportIds(ids);
        doNothing().when(fpvReportServiceImpl).deleteAllByIds(ids);
    }

    protected void givenFPVReportRestServiceFindAllByIdsReturnsNotFound() {
        when(fpvReportServiceImpl.findAll()).thenReturn(Collections.emptyList());
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

    protected void whenSaveFpvReportAPICalled() throws Exception {
        response = mockMvc.perform(post(FPVREPORTS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvReportRequest)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenUpdatedFpvReportAPICalled(Long id) throws Exception {
        response = mockMvc.perform(put(FPVREPORTS_API_PATH + '/' + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvReportRequest1)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenDeleteByIdAPICalled(Long id) throws Exception {
        response = mockMvc.perform(delete(FPVREPORTS_API_PATH + '/' + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenDeleteAllByIdsByIdsAPICalled() throws Exception {
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

    protected void thenExpectResponseWithOneSavedFpvReport() throws UnsupportedEncodingException {
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

    protected void thenExpectNoCallToFPVReportRestServiceSave() {
        verify(fpvReportServiceImpl, times(0)).save(any(FPVReportRequest.class));
    }

    protected void thenExpectFpvReportServiceFindAllFpvReportsCalledOnce() {
        verify(fpvReportServiceImpl, times(1)).findAll();
    }

    protected void thenExpectFpvReportServiceSaveCalledOnce() {
        verify(fpvReportServiceImpl, times(1)).save(any(FPVReportRequest.class));
    }

    protected void thenExpectFpvReportServiceDeleteByIdCalledOnce(Long id) {
        verify(fpvReportServiceImpl, times(1)).deleteById(id);
    }

    protected void thenExpectFpvReportServiceDeleteAllByIdsByIdsCalledOnce(List<Long> ids) {
        verify(fpvReportServiceImpl, times(1)).deleteAllByIds(ids);
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