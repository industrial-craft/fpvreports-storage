package com.rade.protect.api.integrationtests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.shaded.gson.Gson;
import com.rade.protect.api.AppError;
import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.request.*;
import com.rade.protect.model.response.*;
import com.rade.protect.service.impl.FPVDroneServiceImpl;
import com.rade.protect.service.impl.FPVPilotAuthServiceImpl;
import com.rade.protect.service.impl.FPVPilotServiceImpl;
import com.rade.protect.service.impl.FPVReportServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FpvReportRestControllerIT {

    protected final static String FPVREPORTS_API_PATH = "/api/v1/fpvreports";

    protected final static String FPV_AUTH_API_PATH = "/api/v1/auth";

    protected final static String SIGN_UP_API_PATH = "signup";

    protected final static String SIGN_IN_API_PATH = "signin";

    protected static ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected FPVReportServiceImpl fpvReportServiceImpl;

    @Autowired
    protected FPVDroneServiceImpl fpvDroneServiceImpl;

    @Autowired
    protected FPVPilotServiceImpl fpvPilotServiceImpl;

    @Autowired
    protected FPVPilotAuthServiceImpl fpvPilotAuthServiceImpl;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected FPVPilotRepository fpvPilotRepository;

    @Autowired
    protected FPVReportRepository fpvReportRepository;

    @Value("${security.jwt.secret-key}")
    protected String secretKey;

    @Value("${security.jwt.expiration-time}")
    protected long jwtExpiration;

    protected MockHttpServletResponse response;

    protected AppError appError;

    protected FPVPilotSignUpRequest fpvPilotSignUpRequest;

    protected FPVPilotSignInRequest fpvPilotSignInRequest;

    protected FPVReportCreateRequest fpvReportCreateRequest;

    protected FPVReportUpdateRequest fpvReportUpdateRequest;

    protected FPVPilotSignInResponse fpvPilotSignInResponse;

    protected FPVReportResponse fpvReportResponse;

    protected FPVReportIds fpvReportIds;

    protected String jwtToken;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setUp() throws Exception {

        givenValidFpvPilotSignUpRequest();
        whenFpvPilotIsSignUp();
        thenExpectResponseHasOkStatus();

        givenValidFpvPilotSignInRequest();
        whenFpvPilotIsSignIn();
        thenExpectResponseHasOkStatus();

        extractJwtToken();

        givenValidFpvReportRequest();
        whenCreateFpvReport();
        thenExpectResponseHasCreatedStatus();
        andThenExpectCorrectCreatedFpvReportResponseData();
    }

    protected void givenValidFpvPilotSignUpRequest() {
        fpvPilotSignUpRequest = FPVPilotSignUpRequest
                .builder()
                .username("fpvPilotUsername")
                .password("fpvPilotPassword")
                .firstname("Dave")
                .lastname("Clark")
                .build();
    }

    protected void givenValidFpvPilotSignInRequest() {
        fpvPilotSignInRequest = FPVPilotSignInRequest.builder()
                .username("fpvPilotUsername")
                .password("fpvPilotPassword")
                .build();
    }

    protected void givenValidFpvReportRequest() {
        FPVDroneRequest fpvDroneRequest = FPVDroneRequest.builder()
                .fpvSerialNumber("SN743")
                .fpvCraftName("Trainspotting")
                .fpvModel(FPVDrone.FPVModel.KAMIKAZE)
                .build();

        FPVPilotRequest fpvPilot = FPVPilotRequest.builder()
                .firstname("Dave")
                .lastname("Clark")
                .build();

        fpvReportCreateRequest = FPVReportCreateRequest.builder()
                .fpvDrone(fpvDroneRequest)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(fpvPilot)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();
    }

    protected void givenAnotherValidFpvReportRequest() {
        FPVDroneRequest anotherFpvDroneRequest = FPVDroneRequest.builder()
                .fpvSerialNumber("SN7564")
                .fpvCraftName("Kolibri")
                .fpvModel(FPVDrone.FPVModel.PPO)
                .build();

        FPVPilotRequest anotherFpvPilotRequest = FPVPilotRequest.builder()
                .firstname("Austin")
                .lastname("Powers")
                .build();

        fpvReportCreateRequest = FPVReportCreateRequest.builder()
                .fpvDrone(anotherFpvDroneRequest)
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 20, 0))
                .fpvPilot(anotherFpvPilotRequest)
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 12621 83048")
                .additionalInfo("h: 249")
                .build();
    }

    protected void givenUpdatedFpvReportRequest() {
        fpvReportUpdateRequest = FPVReportUpdateRequest.builder()
                .fpvDrone(FPVDroneRequest.builder()
                        .fpvSerialNumber("SN7564")
                        .fpvCraftName("Kolibri")
                        .fpvModel(FPVDrone.FPVModel.BOMBER)
                        .build())
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 20, 0))
                .isLostFPVDueToREB(true)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 12621 83048")
                .additionalInfo("h: 245")
                .build();
    }

    protected void givenFpvReportsByIds(List<Long> ids) {
        fpvReportIds = new FPVReportIds();
        fpvReportIds.setFpvReportIds(ids);
    }

    protected void givenRequestWithInvalidFpvSerialNumber(String fpvSerialNumber) {
        givenValidFpvReportRequest();
        fpvReportCreateRequest.getFpvDrone().setFpvSerialNumber(fpvSerialNumber);
    }

    protected void givenRequestWithInvalidFpvCraftName(String fpvCraftName) {
        givenValidFpvReportRequest();
        fpvReportCreateRequest.getFpvDrone().setFpvCraftName(fpvCraftName);
    }

    protected void givenRequestWithInvalidFpvModel(String fpvModel) {
        givenValidFpvReportRequest();
        if (fpvModel == null || fpvModel.isBlank()) {
            fpvReportCreateRequest.getFpvDrone().setFpvModel(null);
        } else {
            fpvReportCreateRequest.getFpvDrone().setFpvModel(Enum.valueOf(FPVDrone.FPVModel.class, fpvModel));
        }
    }

    protected void givenRequestWithInvalidFpvDrone(String fpvDrone) {
        givenValidFpvReportRequest();
        if (fpvDrone == null) {
            fpvReportCreateRequest.setFpvDrone(null);
        } else {
            fpvReportCreateRequest.setFpvDrone(new FPVDroneRequest());
        }
    }

    protected void givenRequestWithInvalidDateAndTime(String dateTimeFlight) {
        givenValidFpvReportRequest();
        if (dateTimeFlight == null || dateTimeFlight.isBlank()) {
            fpvReportCreateRequest.setDateTimeFlight(null);
        } else {
            fpvReportCreateRequest.setDateTimeFlight(LocalDateTime.parse(dateTimeFlight));
        }
    }

    protected void givenRequestWithInvalidFpvPilotName(String fpvPilotName) {
        givenValidFpvReportRequest();
        fpvReportCreateRequest.getFpvPilot().setFirstname(fpvPilotName);
    }

    protected void givenRequestWithInvalidFpvPilotLastName(String fpvPilotLastName) {
        givenValidFpvReportRequest();
        fpvReportCreateRequest.getFpvPilot().setLastname(fpvPilotLastName);
    }

    protected void givenRequestWithInvalidFpvPilot(String fpvPilot) {
        givenValidFpvReportRequest();
        if (fpvPilot == null) {
            fpvReportCreateRequest.setFpvPilot(null);
        } else {
            fpvReportCreateRequest.setFpvPilot(new FPVPilotRequest());
        }
    }

    protected void givenRequestWithInvalidCoordinatesMGRS(String coordinatesMGRS) {
        givenValidFpvReportRequest();
        fpvReportCreateRequest.setCoordinatesMGRS(coordinatesMGRS);
    }

    protected void givenRequestWithInvalidAdditionalInfo(String additionalInfo) {
        givenValidFpvReportRequest();
        fpvReportCreateRequest.setAdditionalInfo(additionalInfo);
    }

    protected void whenFpvPilotIsSignUp() throws Exception {
        response = mockMvc.perform(post(FPV_AUTH_API_PATH + '/' + SIGN_UP_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvPilotSignUpRequest)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenFpvPilotIsSignIn() throws Exception {
        response = mockMvc.perform(post(FPV_AUTH_API_PATH + '/' + SIGN_IN_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvPilotSignInRequest)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenCreateFpvReport() throws Exception {
        response = mockMvc.perform(post(FPVREPORTS_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvReportCreateRequest)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenUpdatedFpvReport(Long id) throws Exception {
        response = mockMvc.perform(put(FPVREPORTS_API_PATH + '/' + id)
                        .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fpvReportUpdateRequest)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenFindFpvReportById(Long id) throws Exception {
        response = mockMvc.perform(get(FPVREPORTS_API_PATH + '/' + id)
                        .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenFindAllFpvReports() throws Exception {
        response = mockMvc.perform(get(FPVREPORTS_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenDeleteFpvReportById(Long id) throws Exception {
        response = mockMvc.perform(delete(FPVREPORTS_API_PATH + '/' + id)
                        .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    protected void whenDeleteAllByIdsByIdsAPICalled() throws Exception {
        response = mockMvc.perform(delete(FPVREPORTS_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", jwtToken))
                        .content(mapper.writeValueAsString(fpvReportIds)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    private void extractJwtToken() throws Exception {
        fpvPilotSignInResponse = mapper.readValue(response.getContentAsString(), FPVPilotSignInResponse.class);
        jwtToken = fpvPilotSignInResponse.getToken();
    }

    protected void andThenExpectCorrectCreatedFpvReportResponseData() throws UnsupportedEncodingException, JsonProcessingException {
        fpvReportResponse = mapper.readValue(response.getContentAsString(), FPVReportResponse.class);
        assertNotNull(fpvReportResponse);
        assertEquals("SN743", fpvReportResponse.getFpvDrone().getFpvSerialNumber());
        assertEquals("h: 243", fpvReportResponse.getAdditionalInfo());
    }

    protected void andThenExpectCorrectUpdatedFpvReportResponseData() throws UnsupportedEncodingException, JsonProcessingException {
        fpvReportResponse = mapper.readValue(response.getContentAsString(), FPVReportResponse.class);
        assertNotNull(fpvReportResponse);
        assertEquals("SN7564", fpvReportResponse.getFpvDrone().getFpvSerialNumber());
        assertEquals("h: 245", fpvReportResponse.getAdditionalInfo());
    }

    protected void andThenExpectCorrectMessageIfFpvReportIsDeletedByIdInResponse(Long id) throws UnsupportedEncodingException, JsonProcessingException {
        appError = mapper.readValue(response.getContentAsString(), AppError.class);
        assertNotNull(appError);
        assertEquals(200, appError.getStatusCode());
        assertEquals(format("FPVReport with ID: %d is successfully deleted!", id), appError.getMessage());
    }

    protected void andThenExpectCorrectMessageIfFpvReportsAreDeletedByIdsInResponse(List<Long> ids) throws UnsupportedEncodingException, JsonProcessingException {
        appError = mapper.readValue(response.getContentAsString(), AppError.class);
        assertNotNull(appError);
        assertEquals(200, appError.getStatusCode());
        assertEquals(format("FPVReports with IDs: %s are successfully deleted!", ids), appError.getMessage());
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

    protected void thenExpectResponseWithMessageAndStatusCode(int actualResponseStatusCode, String actualResponseMessage) throws UnsupportedEncodingException {
        String responseBody = response.getContentAsString();
        AppError appError = new Gson().fromJson(responseBody, AppError.class);
        assertEquals(appError.getStatusCode(), actualResponseStatusCode);
        assertEquals(appError.getMessage(), actualResponseMessage);
    }

    protected void thenExpectResponseHasOkStatus() {
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    protected void thenExpectResponseHasCreatedStatus() {
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    protected void thenExpectResponseHasBadRequestStatus() {
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    protected void thenExpectResponseNotFoundStatus() {
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
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

}