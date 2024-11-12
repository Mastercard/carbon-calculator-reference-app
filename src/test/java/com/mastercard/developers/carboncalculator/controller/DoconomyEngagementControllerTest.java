package com.mastercard.developers.carboncalculator.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.developers.carboncalculator.configuration.ApiConfiguration;
import com.mastercard.developers.carboncalculator.service.*;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.Benchmark;
import org.openapitools.client.model.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import static com.mastercard.developers.carboncalculator.service.MockData.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DoconomyEngagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiConfiguration apiConfiguration;
    @MockBean
    private EngagementService engagementService;

    @MockBean
    private Profiles profilesRequest;

    @MockBean
    private EnvironmentalImpactService environmentalImpactService;

    @MockBean
    private SupportedParametersService supportedParametersService;
    @MockBean
    private PaymentCardService paymentCardService;
    @MockBean
    private ServiceProviderService serviceProviderService;
    @MockBean
    private AddCardService addCardService;


    @Autowired
    private ObjectMapper objectMapper;

    ApiException apiException = new ApiException(400, "Bad Request");

    @Test
    void getSurveys() throws Exception {

        when(engagementService.getSurveys()).thenReturn(
                surveys());

        MvcResult mvcResult = this.mockMvc
                .perform(get("/demo/surveys"))
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);

    }

    @Test
    void getSurveysException() throws Exception {

        when(engagementService.getSurveys()).thenThrow(apiException);

        MvcResult mvcResult = this.mockMvc
                .perform(get("/demo/surveys"))
                .andExpect(status().isBadRequest()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);

    }

    @Test
    void updateUserProfile() throws Exception {
        String profilesJson = objectMapper.writeValueAsString(getMockProfilesRequest());

        when(engagementService.updateUserProfile(getMockProfilesRequest())).thenReturn(getMockProfilesResponse());

        MvcResult mvcResult = mockMvc.perform(put("/demo/profiles").contentType(MediaType.APPLICATION_JSON).content(profilesJson))
                .andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void updateUserProfileException() throws Exception {
        String profilesJson = objectMapper.writeValueAsString(getMockProfilesRequest());

        when(engagementService.updateUserProfile(getMockProfilesRequest())).thenThrow(apiException);

        MvcResult mvcResult = mockMvc.perform(put("/demo/profiles").contentType(MediaType.APPLICATION_JSON).content(profilesJson))
                .andExpect(status().isBadRequest()).andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void updateUserInsights() throws Exception {

        String insightsJson = objectMapper.writeValueAsString(getMockInsightsRequest());
        when(engagementService.updateUserInsights(getMockInsightsRequest(), "branding", false, 20, 2, "1.1", "en")).thenReturn((getMockInsightsResponse()));

        MvcResult mvcResult = mockMvc.perform(put("/demo/insights").contentType("application/json").content(insightsJson)).andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void updateUserInsightsException() throws Exception {

        String insightsJson = objectMapper.writeValueAsString(getMockInsightsRequest());
        when(engagementService.updateUserInsights(getMockInsightsRequest(), "branding", false, 20, 2, "1.1", "en")).thenThrow(apiException);

        MvcResult mvcResult = mockMvc.perform(put("/demo/insights").contentType("application/json").content(insightsJson)
                .params(prepareRequestParams())).andExpect(status().isBadRequest()).andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testInsightsByIdSuccessResponse() throws Exception {
        String insightsJson = objectMapper.writeValueAsString(getMockInsightsByIdRequest());

        // Mocking the service response
        when(engagementService.getInsightsById("T101", getMockInsightsByIdRequest(), "branding", "en", "1.1")).thenReturn(getMockInsightsByIdResponse());

        MvcResult mvcResult = mockMvc.perform(put("/demo/insights/{id}", "T101").contentType("application/json").content(insightsJson)).andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void insightsByIdException() throws Exception {

        String insightsJson = objectMapper.writeValueAsString(getMockInsightsByIdRequest());
        when(engagementService.getInsightsById("T101", getMockInsightsByIdRequest(), "branding", "en", "1.1")).thenThrow(apiException);
        MvcResult mvcResult = mockMvc.perform(put("/demo/insights/{id}", "T101").contentType("application/json").content(insightsJson).params(prepareRequestParams())).andExpect(status().isBadRequest()).andReturn();
        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getBenchmarksForCountry_Success() throws Exception {
        String country = "USA";
        String period = "2023";
        Benchmark mockBenchmark = new Benchmark();
        when(engagementService.getBenchmarksForCountry(country, period)).thenReturn(mockBenchmark);

        mockMvc.perform(get("/demo/benchmarks").param("country", country).param("period", period).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void getBenchmarksForCountry_ApiException() throws Exception {
        String country = "USA";
        String period = "2023";
        when(engagementService.getBenchmarksForCountry(country, period)).thenThrow(apiException);

        MvcResult mvcResult = mockMvc.perform(get("/demo/benchmarks").param("country", country).param("period", period).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
        assertNotNull(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getBenchmarksForCountry_NoPeriod() throws Exception {
        String country = "USA";
        Benchmark mockBenchmark = new Benchmark();
        when(engagementService.getBenchmarksForCountry(country, null)).thenReturn(mockBenchmark);

        mockMvc.perform(get("/demo/benchmarks").param("country", country).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    private LinkedMultiValueMap<String, String> prepareRequestParams() {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("branding", "branding");
        requestParams.add("heading", "false");
        requestParams.add("page_size", "20");
        requestParams.add("page", "2");
        requestParams.add("version", "1.1");
        requestParams.add("language", "en");
        return requestParams;
    }
}
