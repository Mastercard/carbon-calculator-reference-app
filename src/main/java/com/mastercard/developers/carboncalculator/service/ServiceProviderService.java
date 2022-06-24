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
package com.mastercard.developers.carboncalculator.service;

import com.mastercard.developers.carboncalculator.exception.ServiceException;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ServiceProviderApi;
import org.openapitools.client.model.ServiceProvider;
import org.openapitools.client.model.ServiceProviderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.mastercard.developers.carboncalculator.util.JSON.deserializeErrors;

@Service
public class ServiceProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProviderService.class);

    private final ServiceProviderApi serviceProviderApi;

    public ServiceProviderService(ApiClient client) {
        LOGGER.info("Initializing Service Provider API");
        serviceProviderApi = new ServiceProviderApi(client);
    }

    public ServiceProvider getServiceProvider() throws ServiceException {
        LOGGER.info("Fetching service provider");

        try {
            var serviceProviderInfo = serviceProviderApi.getServiceProviderById();

            LOGGER.info("Returning service provider information");
            return serviceProviderInfo;
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }
    }


    public ServiceProvider updateServiceProvider(ServiceProviderConfig serviceProviderConfig) throws ServiceException {
        LOGGER.info("Updating service provider");
        try {
            ServiceProvider serviceProvider = serviceProviderApi.updateServiceProvider( serviceProviderConfig);
            LOGGER.info("Returning updated service provider");
            return serviceProvider;
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }
    }
}
