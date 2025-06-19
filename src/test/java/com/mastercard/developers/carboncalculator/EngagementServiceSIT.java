package com.mastercard.developers.carboncalculator;


import com.mastercard.developers.carboncalculator.service.EngagementService;
import com.mastercard.developers.carboncalculator.service.MockData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EngagementServiceSIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngagementServiceSIT.class);
    @Autowired
    private EngagementService engagementService;

    @Qualifier("apiClientEngagement")
    private ApiClient apiClientEngagement;

    @Test
    @DisplayName("GetSurveys")
    void getSurveysTest() {
        Surveys surveys;
        try {
            surveys = engagementService.getSurvey();
            assertNotNull(surveys);
        } catch (ApiException e) {
            LOGGER.info("Get Surveys API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("User Profile")
    void userProfileTest() {
        try {
            Profiles profiles;
            profiles = MockData.getMockProfilesRequest();
            Profile profile = engagementService.userProfile(profiles);

            LOGGER.info("{}", profile);

            assertNotNull(profile);

        } catch (ApiException e) {
            LOGGER.info("Update User Profile API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("User Insights")
    void userInsightsTest() {

        try {

            InsightsRequestPayload insightsRequestPayload = new InsightsRequestPayload();
            insightsRequestPayload.setMainCategory("shopping");
            insightsRequestPayload.subCategory("clothes");
            insightsRequestPayload.setMain("shopping");
            insightsRequestPayload.setDocc("60101");
            insightsRequestPayload.setSpendingAreaId("10");
            var insightsData = engagementService.userInsights(insightsRequestPayload);
            LOGGER.info("{}", insightsData);

            assertNotNull(insightsData);

        } catch (ApiException e) {
            LOGGER.info("Update User Insights API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }


    @Test
    @DisplayName("Get Insights by id")
    void getInsightsByIdTest() {

        try {

            var insightsResponseById = engagementService.getInsightsById("T101","","en");

            assertNotNull(insightsResponseById);

        } catch (ApiException e) {
            LOGGER.info("Get User Insights by Id API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("GetBenchmarks")
    void getBenchmarksTest() {
        Benchmark benchmark;
        try {
            benchmark = engagementService.getBenchmarksForCountry("US", null);
            assertNotNull(benchmark);
        } catch (ApiException e) {
            LOGGER.info("Get Benchmarks API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }
    @Test
    @DisplayName("Get Personas")
    void getPersonasTest() {
        Personas personas;
        try {
            personas = engagementService.getPersonas("", "en");
            assertNotNull(personas);
        } catch (ApiException e) {
            LOGGER.info("Get Personas API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Get Comparisons")
    void getComparisonsTest() {
        Comparison comparison;
        try {
            comparison = engagementService.getComparisons("", "en", null, null, BigDecimal.valueOf(1));
            assertNotNull(comparison);
        } catch (ApiException e) {
            LOGGER.info("Get Comparisons API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }
    }


}
