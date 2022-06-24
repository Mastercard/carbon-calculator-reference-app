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

import com.mastercard.developers.carboncalculator.exception.ServiceException;
import com.mastercard.developers.carboncalculator.service.EnvironmentalImpactService;
import com.mastercard.developers.carboncalculator.service.SupportedParametersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
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

        } catch (ServiceException e) {
            LOGGER.info("Calculate Transaction Footprint API call failed with error msg {}", e.getServiceErrors());
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
        } catch (ServiceException e) {
            LOGGER.info("Get Supported Currencies API call failed with error msg {}", e.getServiceErrors());
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
        } catch (ServiceException e) {
            LOGGER.info("Get Supported Merchant Categories API call failed with error msg {}", e.getServiceErrors());
            Assertions.fail(e.getMessage());
        }

    }

    private static List<TransactionData> mockTransactions() {
        List<TransactionData> mcTransactions = new ArrayList<>();
        mcTransactions.add(new TransactionData().transactionId("TX-1")
                .mcc("3000").amount(
                        new Amount().currencyCode("USD").value(new BigDecimal(150))));
        return mcTransactions;
    }

}