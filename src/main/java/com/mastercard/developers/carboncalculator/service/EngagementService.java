package com.mastercard.developers.carboncalculator.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.EngagementServicesApi;
import org.openapitools.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EngagementService {


    private static final Logger LOGGER = LoggerFactory.getLogger(EngagementService.class);

    private final EngagementServicesApi engagementServicesApi;

    @Autowired
    public EngagementService(ApiClient apiClient) {
        LOGGER.info("Initializing Doconomy Engagement APIs");
        engagementServicesApi = new EngagementServicesApi(apiClient);
    }

    public Surveys getSurveys() throws ApiException {
        LOGGER.info("Calling Get Surveys API");
        Surveys surveys = engagementServicesApi.getSurvey();
        LOGGER.info("Returning Doconomy Engagement Surveys.");
        return surveys;
    }

    public Profile updateUserProfile(Profiles profilesRequest) throws ApiException {
        LOGGER.info("Updating Doconomy Engagement Profile API");
        Profile profileResponse = engagementServicesApi.updateUserProfile(profilesRequest);
        LOGGER.info("Returning updated Doconomy Engagement Profile");
        return profileResponse;
    }


    public InsightsData updateUserInsights(InsightsRequestPayload insightsRequestPayload, String branding, Boolean heading, Integer pageSize, Integer page, String version, String language) throws ApiException {
        LOGGER.info("Updating Doconomy Engagement Insights API");
        InsightsData insightsData = engagementServicesApi.updateUserInsights(insightsRequestPayload, branding, heading, pageSize, page, version, language);
        LOGGER.info("Returning updated Doconomy Engagement Insights");
        return insightsData;
    }


    public InsightsResponseById getInsightsById(String id, InsightsByIdRequestPayload insightsByIdRequestPayload, String branding, String language, String version) throws ApiException {
        LOGGER.info("Calling Doconomy Engagement Insights by Id API");
        InsightsResponseById insightsResponseById = engagementServicesApi.getInsightsById(id, insightsByIdRequestPayload, branding, language, version);
        LOGGER.info("Returning Doconomy Engagement Insights by Id");
        return insightsResponseById;
    }

    public Benchmark getBenchmarksForCountry(String country, String period) throws ApiException {
        LOGGER.info("Calling Doconomy Engagement Benchmarks API");
        Benchmark benchmark = engagementServicesApi.getBenchmarksForCountry(country, period);
        LOGGER.info("Returning Doconomy Engagement Benchmarks");
        return benchmark;
    }

}
