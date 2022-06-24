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

import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.carboncalculator.configuration.ApiConfiguration;
import com.mastercard.developers.carboncalculator.exception.ServiceException;
import com.mastercard.developers.carboncalculator.util.EncryptionHelper;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PaymentCardApi;
import org.openapitools.client.model.PaymentCard;
import org.openapitools.client.model.PaymentCardEnrolment;
import org.openapitools.client.model.PaymentCardReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mastercard.developers.carboncalculator.util.JSON.deserializeErrors;

@Service
public class AddCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddCardService.class);

    private PaymentCardApi paymentCardApi;

    @Autowired
    public AddCardService(ApiConfiguration apiConfiguration) throws ServiceException {
        LOGGER.info("Initializing Add Card Service");
        paymentCardApi = new PaymentCardApi(setup(apiConfiguration));

    }

    private ApiClient setup(ApiConfiguration apiConfiguration) throws ServiceException {
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(
                new OkHttpFieldLevelEncryptionInterceptor(
                        EncryptionHelper.encryptionConfig(apiConfiguration.getEncryptionKeyFile()))).addInterceptor(
                new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey()))
                .build();

        return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
    }


    public PaymentCardReference registerPaymentCard(PaymentCard paymentCard) throws ServiceException {

        try {
            LOGGER.info("Calling Add Card API");

            PaymentCardReference paymentCardInfo = paymentCardApi.registerPaymentCard(paymentCard);

            LOGGER.info("Add Card API call successful, payment card with id {} and fpan suffix {} added successfully .",
                    paymentCardInfo.getPaymentCardId(),
                    paymentCardInfo.getLast4fpan());

            return paymentCardInfo;
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

    }

    public List<PaymentCardEnrolment> registerBatchPaymentCards(List<PaymentCard> paymentCard) throws ServiceException {

        try {
            LOGGER.info("Calling Register Batch Payment Cards");
            return paymentCardApi.batchRegisterPaymentCards(paymentCard);
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

    }


}
