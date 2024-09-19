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
import org.openapitools.client.api.PaymentCardApi;
import org.openapitools.client.model.AggregateSearchCriteria;
import org.openapitools.client.model.AggregateTransactionFootprints;
import org.openapitools.client.model.HistoricalTransactionFootprints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardService.class);

    private final PaymentCardApi paymentCardApi;


    @Autowired
    public PaymentCardService(ApiClient client) {
        LOGGER.info("Initializing Payment Card API");
        paymentCardApi = new PaymentCardApi(client);
    }


    public AggregateTransactionFootprints getPaymentCardAggregateTransactions(AggregateSearchCriteria aggregateSearchCriteria) throws ApiException {

        LOGGER.info("Calculating aggregate carbon score for paymentCardIds {}",
                aggregateSearchCriteria.getPaymentCardIds());

        AggregateTransactionFootprints aggregateTransactionFootprintList = paymentCardApi.getPaymentCardAggregateTransactions(
                aggregateSearchCriteria);
        LOGGER.info("Returning aggregate carbon score.");
        return aggregateTransactionFootprintList;
    }

    public HistoricalTransactionFootprints getPaymentCardTransactionHistory(String paymentCardId, String fromDate, String toDate, int offset, int limit) throws ApiException {
            LOGGER.info("Fetching historical transaction footprint data for paymentCardId {}",
                    paymentCardId);
            HistoricalTransactionFootprints historicalTransactionFootprintList = paymentCardApi.getPaymentCardTransactionHistory(
                    paymentCardId, fromDate, toDate, offset, limit);
            LOGGER.info("Returning historical transaction footprint data.");
            return historicalTransactionFootprintList;
    }

    public void deletePaymentCards(List<String> paymentCards) throws ApiException {
        LOGGER.info("Deleting payment card/s");
        paymentCardApi.paymentCardDeletions(paymentCards);
        LOGGER.info("Deleting payment cards completed");
    }

    public void deletePaymentCard(String paymentCard, String clientId, String channel, String origMcApiClientId) throws ApiException {
        LOGGER.info("Deleting payment card");

        paymentCardApi.paymentCardDeletion(paymentCard, clientId, channel, origMcApiClientId);
        LOGGER.info("Deleting payment cardId completed");
    }

}
