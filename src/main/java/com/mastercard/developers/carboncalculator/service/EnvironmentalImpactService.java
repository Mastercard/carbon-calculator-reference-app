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

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.EnvironmentalImpactApi;
import org.openapitools.client.model.AggregateSearchCriteria;
import org.openapitools.client.model.AggregateTransactionFootprints;
import org.openapitools.client.model.TransactionData;
import org.openapitools.client.model.TransactionFootprintData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mastercard.developers.carboncalculator.util.JSON.deserializeErrors;

@Service
public class EnvironmentalImpactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentalImpactService.class);

    private final EnvironmentalImpactApi environmentalImpactApi;

    @Autowired
    public EnvironmentalImpactService(ApiClient apiClient) {
        LOGGER.info("Initializing Calculate Transaction Footprint API");
        environmentalImpactApi = new EnvironmentalImpactApi(apiClient);
    }

    public List<TransactionFootprintData> calculateFootprints(List<TransactionData> mcTransactions) throws ApiException {

        LOGGER.info("Calling Calculate Transaction Footprint API");

        List<TransactionFootprintData> footprints = environmentalImpactApi.footprintsByTransactionData(mcTransactions);

        LOGGER.info("Calculate Transaction Footprint API call successful, returning Transaction Footprints.");

        return footprints;

    }


    public AggregateTransactionFootprints getPaymentCardAggregateTransactions(String clientId,
        AggregateSearchCriteria aggregateSearchCriteria, String channel, String origMcApiClientId) throws ApiException {

        LOGGER.info("Calculating new aggregate API carbon score for paymentCardIds {}",
                aggregateSearchCriteria.getPaymentCardIds());

            AggregateTransactionFootprints aggregateTransactionFootprintList = environmentalImpactApi.getPaymentCardAggregateTransaction(clientId,
                    aggregateSearchCriteria, channel, origMcApiClientId);
            LOGGER.info("Returning aggregate carbon score.");

            return aggregateTransactionFootprintList;
    }
}
