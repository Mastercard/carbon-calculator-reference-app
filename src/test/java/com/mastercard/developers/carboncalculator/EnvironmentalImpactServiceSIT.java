/*
 *  Copyright (c) 2021 Mastercard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mastercard.developers.carboncalculator;

import com.mastercard.developers.carboncalculator.service.EnvironmentalImpactService;
import com.mastercard.developers.carboncalculator.service.MockData;
import com.mastercard.developers.carboncalculator.service.SupportedParametersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class EnvironmentalImpactServiceSIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentalImpactServiceSIT.class);

    @Autowired
    private EnvironmentalImpactService environmentalImpactService;

    @Autowired
    private SupportedParametersService supportedParametersService;


    private static final String X_OPENAPI_CLIENTID = "x-openapi-clientid";
    private static final String CLIENTID = "cNU2Re-v0oKw95zjfs7G60yICaTtQtyEt-vKZrnjd34ea14e";
    private static final String CHANNEL = "CC";
  
    private static final String AGGREGATE_API_CALL_FAILED_WITH_ERROR_MSG = "Aggregate API call failed with error msg {}";

    /**
     * Use case 1. Calculate Transaction Footprints
     */
    @Test
    @DisplayName("Calculate transaction footprints")
    void calculateFootprints() {

        List<TransactionFootprintData> mcTransactionFootprints;
        try {
            mcTransactionFootprints = environmentalImpactService.calculateFootprints(
                    mockTransactions());

            if (mcTransactionFootprints != null) {
                assertNotNull(mcTransactionFootprints);
                assertFalse(mcTransactionFootprints.isEmpty());
                LOGGER.info("{}", mcTransactionFootprints);
            } else {
                LOGGER.info("Calculate Transaction Footprint API call failed.");
                Assertions.fail("Calculate Transaction Footprint API call failed.");
            }

        } catch (ApiException e) {
            LOGGER.info("Calculate Transaction Footprint API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    /**
     * Use case 1. Calculate Carbon Scores
     */
    @Test
    @DisplayName("Calculate carbon scores")
    void calculateCarbonScoreFootprints() {

        CarbonScoreDetails carbonScoreDetails;
        try {
            carbonScoreDetails = environmentalImpactService.calculateCarbonScoreFootprints(mockCarbonScoreData()
                    , X_OPENAPI_CLIENTID, CHANNEL, CLIENTID);

            if (carbonScoreDetails != null) {
                assertNotNull(carbonScoreDetails);
                assertNotNull(carbonScoreDetails.getTransactionFootprints());
                assertFalse(carbonScoreDetails.getTransactionFootprints().isEmpty());
                LOGGER.info("Calculate Carbon Score Success : {}", carbonScoreDetails);
            } else {
                LOGGER.info("Calculate Carbon Score Footprint API call failed.");
                Assertions.fail("Calculate Carbon Score Footprint API call failed.");
            }

        } catch (ApiException e) {
            LOGGER.info("Calculate Carbon Score Footprint API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    /**
     * Use case 2. Supported Currencies
     */
    @Test
    @DisplayName("GetSupportedCurrencies")
    void getSupportedCurrencies() {
        List<Currency> supportedCurrencies;
        try {
            supportedCurrencies = supportedParametersService.getSupportedCurrencies();
            assertNotNull(supportedCurrencies);
            assertFalse(supportedCurrencies.isEmpty());
        } catch (ApiException e) {
            LOGGER.info("Get Supported Currencies API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    /**
     * Use case 3. Supported Merchant Categories
     */
    @Test
    @DisplayName("GetSupportedMerchantCategories")
    void getSupportedMerchantCategories() {
        List<MerchantCategory> supportedMerchantCategories;
        try {
            supportedMerchantCategories = supportedParametersService.getSupportedMerchantCategories();

            assertNotNull(supportedMerchantCategories);
            assertFalse(supportedMerchantCategories.isEmpty());
        } catch (ApiException e) {
            LOGGER.info("Get Supported Merchant Categories API call failed with error msg {}", e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }

    /**
     * Use case 7. View Aggregate Transaction Carbon Footprints
     */
    @Test
    @DisplayName("Fetch the aggregate carbon score for the transactions")
    void aggregateTransactionFootprints() {

        try {
            AggregateTransactionFootprints aggregateTransactionFootprints = environmentalImpactService.getPaymentCardAggregateTransactions(
                    X_OPENAPI_CLIENTID, mockAggregateSearchCriteria("3b40b264-6ae6-4dd2-aa79-a984c81d138"), CHANNEL, CLIENTID);

            assertNotNull(aggregateTransactionFootprints);

            LOGGER.info("{}", aggregateTransactionFootprints);
        } catch (ApiException    e) {
            LOGGER.error(AGGREGATE_API_CALL_FAILED_WITH_ERROR_MSG, e.getResponseBody());
            Assertions.fail(e.getMessage());
        	}
    }

    @Test
    @DisplayName("Add profile to payment card")
    void addProfileToPaymentCard() {

        try {
            PaymentCardProfile paymentCardProfile = environmentalImpactService.addProfileToPaymentCard("89eff262-840d-4e47-9ab3-0eb4de390815", MockData.getMockPaymentCardProfilesRequest());


            assertNotNull(paymentCardProfile);

            LOGGER.info("{}", paymentCardProfile);
        } catch (ApiException e) {
            LOGGER.info(e.getResponseBody());
            Assertions.fail(e.getMessage());
        }

    }


    private static List<TransactionData> mockTransactions() {
        List<TransactionData> mcTransactions = new ArrayList<>();
        mcTransactions.add((TransactionData) new TransactionData().transactionId("TX-1")
                .mcc("3000").amount(
                        new Amount().currencyCode("USD").value(new BigDecimal(150))));
        return mcTransactions;
    }

    /**
     * Test with different Aggregate type, supported values are as follows:
     * 1=weekly
     * 2=monthly
     * 3=monthly category wise
     */
    private static AggregateSearchCriteria mockAggregateSearchCriteria(String paymentCardId) {

        List<String> paymentCardIds = Collections.singletonList(paymentCardId);
        return new AggregateSearchCriteria().paymentCardIds(paymentCardIds).aggregateType(2);
    }

    private static ScoreRequestDetails mockCarbonScoreData() {
        ScoreRequestDetails scoreRequestDetails = new ScoreRequestDetails();
        TransactionDetails transactionDetails  = new TransactionDetails();
        transactionDetails.mcc("3000").id("DVsJNvdSMX").amount(
                new Amount().currencyCode("USD").value(new BigDecimal(150)));
        scoreRequestDetails.addTransactionsItem(transactionDetails);
        return scoreRequestDetails;
    }

}