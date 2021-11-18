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

import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.carboncalculator.configuration.ApiConfiguration;
import com.mastercard.developers.carboncalculator.exception.ServiceException;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PaymentCardApi;
import org.openapitools.client.model.AggregateSearchCriteria;
import org.openapitools.client.model.AggregateTransactionFootprint;
import org.openapitools.client.model.HistoricalTransactionFootprints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mastercard.developers.carboncalculator.util.JSON.deserializeErrors;


@Service
public class PaymentCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardService.class);

    private final PaymentCardApi paymentCardApi;


    @Autowired
    public PaymentCardService(ApiConfiguration apiConfiguration)  {
        LOGGER.info("Initializing Payment Card API");
        paymentCardApi = new PaymentCardApi(setup(apiConfiguration));
    }

    private ApiClient setup(ApiConfiguration apiConfiguration)  {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(
                        new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey()))
                .build();

        return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
    }

    public List<AggregateTransactionFootprint> getPaymentCardAggregateTransactions(AggregateSearchCriteria aggregateSearchCriteria) throws ServiceException {

        LOGGER.info("Calculating aggregate carbon score for paymentCardIds {}",
                    aggregateSearchCriteria.getPaymentCardIds());

        try {
            List<AggregateTransactionFootprint> aggregateTransactionFootprintList = paymentCardApi.getPaymentCardAggregateTransactions(
                    aggregateSearchCriteria);
            LOGGER.info("Returning aggregate carbon score.");

            return aggregateTransactionFootprintList;
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }


    }

    public HistoricalTransactionFootprints getPaymentCardTransactionHistory(String paymentCardId, String fromDate, String toDate, int offset, int limit) throws ServiceException {
        try {
            LOGGER.info("Fetching historical transaction footprint data for paymentCardId {}",
                        paymentCardId);

            HistoricalTransactionFootprints historicalTransactionFootprintList = paymentCardApi.getPaymentCardTransactionHistory(
                    paymentCardId, parseDate(fromDate), parseDate(toDate), offset, limit);

            LOGGER.info("Returning historical transaction footprint data.");

            return historicalTransactionFootprintList;
        } catch (ApiException e) {
            throw new ServiceException(e);
        }
    }

    private LocalDate parseDate(String stringDate) {
        return LocalDate.parse(stringDate);
    }

    public void deletePaymentCards(List <String> paymentCards) throws ServiceException {
        LOGGER.info("Deleting payment card/s {}", paymentCards);
        try {

            paymentCardApi.paymentCardDeletions(paymentCards);

            LOGGER.info("Deleting payment cards completed");
        } catch (ApiException e) {
            throw new ServiceException(e);
        }

    }

}
