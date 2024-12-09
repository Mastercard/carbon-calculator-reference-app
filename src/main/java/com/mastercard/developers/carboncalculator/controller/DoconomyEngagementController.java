package com.mastercard.developers.carboncalculator.controller;


import com.mastercard.developers.carboncalculator.service.EngagementService;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mastercard.developers.carboncalculator.util.EncryptionHelper.getErrorObjectResponseEntity;

/**
 * This controller class exposes the following endpoints
 * 1. /surveys
 * 2. /insights
 * 3. /profiles
 * 4. /insights by id
 * 5. /benchmarks
 * 6. /personas
 * 7. /comparisons
 * <p>
 * Issuer can consume these endpoints directly through their web or mobile application or add their implementation on top of this.
 */

@RestController
@RequestMapping("/demo")
public class DoconomyEngagementController {

    private final EngagementService engagementService;

    public DoconomyEngagementController(EngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @GetMapping("/surveys")
    public ResponseEntity<Object> getSurvey() {
        Surveys surveys = null;
        try {
            surveys = engagementService.getSurveys();
        } catch (ApiException exception) {
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(surveys);
    }

    @PutMapping("/profiles")
    public ResponseEntity<Object> updateUserProfile(@RequestBody Profiles profilesRequest) {
        Profile profile = null;
        try {
            profile = engagementService.updateUserProfile(profilesRequest);
        } catch (ApiException exception) {
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/insights")
    public ResponseEntity<Object> updateUserInsights(@RequestBody InsightsRequestPayload insightsRequestPayload, @RequestParam(value = "branding", required = false) String branding, @RequestParam(value = "heading", required = false) Boolean heading, @RequestParam(value = "page_size", required = false) String pageSize, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "version", required = false) String version, @RequestParam(value = "language", required = false) String language) {
        InsightsData insightsData = null;

        try {
            insightsData = engagementService.updateUserInsights(insightsRequestPayload, branding, heading, pageSize, page, version, language);
        } catch (ApiException exception) {
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(insightsData);
    }

    @PutMapping("/insights/{id}")
    public ResponseEntity<Object> getInsightsById(@PathVariable("id") String id, @RequestBody InsightsByIdRequestPayload insightsByIdRequestPayload, @RequestParam(value = "branding", required = false) String branding, @RequestParam(value = "language", required = false) String language, @RequestParam(value = "version", required = false) String version) {
        InsightsResponseById insightsResponseById = null;
        try {
            insightsResponseById = engagementService.getInsightsById(id, insightsByIdRequestPayload, branding, language, version);
        } catch (ApiException exception) {
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(insightsResponseById);

    }

    @GetMapping("/benchmarks")
    public ResponseEntity<Object> getBenchmarksForCountry(@RequestParam(value = "country") String country, @RequestParam(value = "period", required = false) String period) {
        Benchmark benchmark = null;
        try {
            benchmark = engagementService.getBenchmarksForCountry(country, period);
        } catch (ApiException exception) {
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(benchmark);

    }
    @GetMapping("/personas")
    public ResponseEntity<Object> getPersonas(@RequestParam(value = "branding", required = false) String branding, @RequestParam(value = "language", required = false) String language, @RequestParam(value = "page_size", required = false) String pageSize, @RequestParam(value = "page", required = false) String page) {
        Personas personas = null;
        try {
            personas = engagementService.getPersonas(branding, language, pageSize, page);
        } catch (ApiException exception) {
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(personas);

    }
    @GetMapping("/comparisons")
    public ResponseEntity<Object> getComparisons(@RequestParam(value = "branding", required = false) String branding, @RequestParam(value = "language", required = false) String language, @RequestParam(value = "version", required = false) String version, @RequestParam(value = "main_category", required = false) String mainCategory, @RequestParam(value = "spending_area_id", required = false) String spendingAreaId, @RequestParam(value = "tonne", required = false) String tonne) {
        Comparison comparison = null;
        try {
            comparison = engagementService.getComparisons(branding, language, version, mainCategory, spendingAreaId, tonne);
        } catch (ApiException exception) {
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(comparison);

    }


}
