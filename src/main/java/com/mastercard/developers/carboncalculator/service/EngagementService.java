package com.mastercard.developers.carboncalculator.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.EngagementServicesApi;
import org.openapitools.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class EngagementService {


    private static final Logger LOGGER = LoggerFactory.getLogger(EngagementService.class);

    private final EngagementServicesApi engagementServicesApi;

    @Autowired
    public EngagementService( @Qualifier("apiClientEngagement")ApiClient apiClient) {        LOGGER.info("Initializing Doconomy Engagement APIs");
        engagementServicesApi = new EngagementServicesApi(apiClient);
    }

    public Surveys getSurvey() throws ApiException {
        LOGGER.info("Calling Get Surveys API");
        Surveys surveys = engagementServicesApi.getSurvey();
        LOGGER.info("Returning Doconomy Engagement Surveys.");
        return surveys;
    }

    public Profile userProfile(Profiles profilesRequest) throws ApiException {
        LOGGER.info("Updating Doconomy Engagement Profile API");
        Profile profileResponse = engagementServicesApi.userProfile(profilesRequest);
        LOGGER.info("Returning updated Doconomy Engagement Profile");
        return profileResponse;
    }


    public InsightsData userInsights(InsightsRequestPayload insightsRequestPayload) throws ApiException {
        LOGGER.info("Updating Doconomy Engagement Insights API");
        InsightsData insightsData = engagementServicesApi.userInsights(insightsRequestPayload);
        LOGGER.info("Returning updated Doconomy Engagement Insights");
        return insightsData;
    }


    public InsightsResponseById getInsightsById(String id, String branding, String language) throws ApiException {
        LOGGER.info("Calling Doconomy Engagement Insights by Id API");
        InsightsResponseById insightsResponseById = engagementServicesApi.getInsightsById(id, branding, language);
        LOGGER.info("Returning Doconomy Engagement Insights by Id");
        return insightsResponseById;
    }

    public Benchmark getBenchmarksForCountry(String country, String period) throws ApiException {
        LOGGER.info("Calling Doconomy Engagement Benchmarks API");
        Benchmark benchmark = engagementServicesApi.getBenchmarksForCountry(country, period);
        LOGGER.info("Returning Doconomy Engagement Benchmarks");
        return benchmark;
    }
    public Personas getPersonas(String branding, String language) throws ApiException
    {
        LOGGER.info("Calling Doconomy Engagement Personas API");
        Personas personas = engagementServicesApi.getPersonas(branding, language);
        LOGGER.info("Returning Doconomy Engagement Personas");
        return personas;
    }


    public Comparison getComparisons(String branding, String  language, String  mainCategory, String  spendingAreaId, BigDecimal tonne) throws ApiException
    {
        LOGGER.info("Calling Doconomy Engagement Comparisons API");
        Comparison comparison = engagementServicesApi.getComparisons(branding, language, mainCategory, spendingAreaId, tonne);
        LOGGER.info("Returning Doconomy Engagement Comparisons");
        return comparison;
    }

}
