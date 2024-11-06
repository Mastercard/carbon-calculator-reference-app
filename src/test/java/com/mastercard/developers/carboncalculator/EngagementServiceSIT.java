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
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class EngagementServiceSIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngagementServiceSIT.class);
    @Autowired
    private EngagementService engagementService;

    @Autowired
    private ApiClient apiClient;


    @Test
    @DisplayName("GetSurveys")
    void getSurveysTest() {
        Surveys surveys;
        try {
            surveys = engagementService.getSurveys();
            assertNotNull(surveys);
        } catch (ApiException e) {
            LOGGER.info("Get Surveys API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("Update User Profile")
    void updateUserProfileTest() {

        try {
            Profiles profiles;
            profiles = MockData.getMockProfilesRequest();
            Profile profile = engagementService.updateUserProfile(profiles);

            LOGGER.info("{}", profile);

            assertNotNull(profile);

        } catch (ApiException e) {
            LOGGER.info("Update User Profile API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("Update User Insights")
    void updateUserInsightsTest() {

        try {
            InsightsRequestPayload insightsRequestPayload = new InsightsRequestPayload();
            InsightsRequestPayloadInsightsPayload insightsRequestPayloadInsightsPayload = new InsightsRequestPayloadInsightsPayload();
            insightsRequestPayloadInsightsPayload.setMainCategory("shopping");
            insightsRequestPayloadInsightsPayload.subCategory("clothes");
            insightsRequestPayloadInsightsPayload.setMain("shopping");
            insightsRequestPayloadInsightsPayload.setDocc("");
            insightsRequestPayloadInsightsPayload.setSpendingAreaId("10");
            insightsRequestPayload.setInsightsPayload(insightsRequestPayloadInsightsPayload);


            var insightsData = engagementService.updateUserInsights(insightsRequestPayload, any(), false, 20, 2, "1.1", "en");

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
            InsightsByIdRequestPayload insightsByIdRequestPayload;

            insightsByIdRequestPayload =  MockData.getMockInsightsByIdRequest();

            var insightsResponseById = engagementService.getInsightsById("T101",insightsByIdRequestPayload,"","en","1.1");
            LOGGER.info("{}", insightsResponseById);

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

}
