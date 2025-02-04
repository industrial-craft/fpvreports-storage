package com.rade.protect.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rade.protect.model.request.FPVDrone;
import com.rade.protect.model.request.FPVPilot;
import com.rade.protect.model.request.FPVReport;
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
import java.time.LocalDateTime;
import java.util.List;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
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

    protected FPVReportResponse fpvReportResponse;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /*
     * GIVEN Methods
     */

    protected void givenFPVReportRestServiceGetAllFPVReportsReturnsListOfFPVReports() {
        FPVDrone fpvDrone = FPVDrone.builder()
                .fpvSerialNumber("SN743")
                .fpvCraftName("Trainspotting")
                .fpvModel(FPVDrone.FPVModel.KAMIKAZE)
                .build();
        FPVPilot fpvPilot = FPVPilot.builder()
                .name("Mark")
                .lastName("Renton")
                .build();

        fpvReportResponse = FPVReportResponse.builder()
                .fpvReportId(1L)
                .fpvDrone(fpvDrone)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();

        doReturn(List.of(fpvReportResponse)).when(fpvReportRestService).findAll();
    }

    protected void givenValidCreateFpvReport() {
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
                .fpvDrone(fpvDrone)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();

        fpvReportResponse = FPVReportResponse.builder()
                .fpvReportId(1L)
                .fpvDrone(fpvDrone)
                .dateTimeFlight(fpvReport.getDateTimeFlight())
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(fpvReport.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport.getCoordinatesMGRS())
                .additionalInfo(fpvReport.getAdditionalInfo())
                .build();

        doReturn(fpvReport).when(fpvReportRestService).save(any(FPVReport.class));
    }

    /*
     * WHEN Methods
     */

    protected void whenFindOneFpvReport(Long id) throws Exception {
        response = mockMvc.perform(get(FPVREPORTS_API_PATH + '/' + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
    }

    protected void whenFindAllFpvReports() throws Exception {
        response = mockMvc.perform(get(FPVREPORTS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
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

        verify(fpvReportRestService, times(1)).save(any(FPVReport.class));
    }

    protected void whenUpdatedFpvReportAPICalled(Long id, FPVReport updatedFpvReport) throws Exception {
        response = mockMvc.perform(put(FPVREPORTS_API_PATH + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJsonString(updatedFpvReport)))
                .andReturn()
                .getResponse();
    }

    protected void whenDeleteFpvReportAPICalled(Long id) throws Exception {
        response = mockMvc.perform(delete(FPVREPORTS_API_PATH + id)
                        .contentType(MediaType.APPLICATION_JSON))
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

    protected void thenExpectResponseHasCreatedStatus() throws UnsupportedEncodingException {
        log.info("Response body: {}", response.getContentAsString());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    protected void thenExpectResponseWithFPVReports() throws UnsupportedEncodingException {
        List<FPVReportResponse> findAllFindFpvReports = stringJsonToList(response.getContentAsString(), FPVReportResponse.class);
        assertEquals(1, findAllFindFpvReports.size());
        assertTrue(findAllFindFpvReports.contains(fpvReportResponse));
    }

    protected void thenExpectResponseWithOneFPVReport() throws UnsupportedEncodingException {
        FPVReportResponse actualFindOneFPVReportResponse = stringJsonToObject(response.getContentAsString(), FPVReportResponse.class);
        assertNotNull(actualFindOneFPVReportResponse);
        assertEquals(fpvReportResponse, actualFindOneFPVReportResponse);
    }

    protected void thenExpectFpvReportServiceFindAllFpvReportsCalledOnce() {
        verify(fpvReportRestService).findAll();
    }

    protected void thenExpectFpvReportServiceCreateFpvReportCalledOnce() {
        verify(fpvReportRestService).save(any(FPVReport.class));
    }

    protected void thenExpectNoCallToFpvReportServiceCreateFpvReportCalledOnce() {
        verify(fpvReportRestService, times(0)).save(any(FPVReport.class));
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