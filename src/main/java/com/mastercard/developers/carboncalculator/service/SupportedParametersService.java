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
import org.openapitools.client.api.SupportedParametersApi;
import org.openapitools.client.model.Currency;
import org.openapitools.client.model.MerchantCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mastercard.developers.carboncalculator.util.JSON.deserializeErrors;

@Service
public class SupportedParametersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupportedParametersService.class);

    private final SupportedParametersApi supportedParametersApi;

    @Autowired
    public SupportedParametersService(ApiClient client) {
        LOGGER.info("Initializing Supported Parameters API");
        supportedParametersApi = new SupportedParametersApi(client);
    }

    public List<Currency> getSupportedCurrencies() throws ServiceException {
        LOGGER.info("Calling Get Supported Currencies API");

        try {
            List<Currency> mcSupportedCurrencies = supportedParametersApi.getSupportedCurrencies();

            LOGGER.info("Returning list of supported currencies.");

            return mcSupportedCurrencies;
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }


    }

    public List<MerchantCategory> getSupportedMerchantCategories() throws ServiceException {
        LOGGER.info("Calling Get Supported Merchant Categories API");

        try {
            List<MerchantCategory> merchantCategories = supportedParametersApi.getSupportedMerchantCategories();

            LOGGER.info("Returning list of supported MCCs.");

            return merchantCategories;
        } catch (ApiException e) {
            throw new ServiceException(e.getResponseBody());
        }
    }

}
