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
import com.mastercard.developers.carboncalculator.service.ServiceProviderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.ServiceProvider;
import org.openapitools.client.model.ServiceProviderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ContextConfiguration
class ServiceProviderServiceSIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProviderServiceSIT.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    /**
     * Use case 5. Get Service Provider
     */
    @Test
    @DisplayName("Get Service Provider")
    void getServiceProvider() {

        try {
            var serviceProviderInfo = serviceProviderService.getServiceProvider();

            LOGGER.info("{}", serviceProviderInfo);

            assertNotNull(serviceProviderInfo);
            assertNotNull(serviceProviderInfo.getClientId());

        } catch (ServiceException e) {
            LOGGER.info("Get Service Provider API call failed with error msg {}", e.getServiceErrors());
            Assertions.fail(e.getMessage());
        }

    }


    /**
     * Use case 6. Update Service Provider
     */
    @Test
    @DisplayName("Update a Service Provider")
    void updateServiceProviderTest() {

        try {
            ServiceProviderConfig serviceProviderConfig = new ServiceProviderConfig();
            serviceProviderConfig.setCustomerName("New Customer Name");
            serviceProviderConfig.setCallbackUrl("https://www.abcbank.com/payment-cards/notify-transaction-footprints");
            serviceProviderConfig.setSupportedAccountRange("534403");

            ServiceProvider serviceProviderInfo = serviceProviderService.updateServiceProvider(serviceProviderConfig);

            LOGGER.info("{}", serviceProviderInfo);

            assertNotNull(serviceProviderInfo);
            assertNotNull(serviceProviderInfo.getClientId());

        } catch (ServiceException e) {
            LOGGER.info("Update Service Provider API call failed with error msg {}", e.getServiceErrors());
            Assertions.fail(e.getMessage());
        }

    }


}