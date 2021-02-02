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
package com.mastercard.developers.carboncalculator.usecases;

import com.mastercard.developers.carboncalculator.exception.ServiceException;
import com.mastercard.developers.carboncalculator.service.AddCardService;
import com.mastercard.developers.carboncalculator.service.PaymentCardService;
import org.junit.jupiter.api.*;
import org.openapitools.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static com.mastercard.developers.carboncalculator.usecases.helper.PanGenerator.generateFPAN;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentCardServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardServiceTest.class);
    private static final String ADD_CARD_API_CALL_FAILED_WITH_ERROR_MSG = "Add Card API call failed with error msg {}";

    @Autowired
    private PaymentCardService paymentCardService;

    @Autowired
    private AddCardService addCardService;

    @Value("${test.data.bin}")
    private String bin;

    @Value("${test.data.card-base-currency}")
    String cardBaseCurrency;

    private static String paymentCardId;

    /**
     * Use case 4. Enrol FPAN
     */
    @Test
    @DisplayName("Register a new Payment Card")
    @Order(1)
    void registerPaymentCard() {

        PaymentCard paymentCard = new PaymentCard().fpan(generateFPAN(bin)).cardBaseCurrency(cardBaseCurrency);

        try {

            PaymentCardReference paymentCardReference = addCardService.registerPaymentCard(paymentCard);

            assertNotNull(paymentCardReference);
            assertNotNull(paymentCardReference.getPaymentCardId());
            assertNotNull(paymentCardReference.getLast4fpan());

            setPaymentCardId(paymentCardReference.getPaymentCardId());


        } catch (ServiceException e) {
            LOGGER.info(ADD_CARD_API_CALL_FAILED_WITH_ERROR_MSG, e.getServiceErrors());
            Assertions.fail(e.getMessage());
        }


    }

    /**
     * Use case 5. View Historical Carbon Impact
     */
    @Test
    @DisplayName("Fetch the aggregate carbon score for the transactions")
    @Order(2)
    void aggregateTransactionFootprints() {

        try {
            List<AggregateTransactionFootprint> aggregateTransactionFootprints = paymentCardService.getPaymentCardAggregateTransactions(
                    mockAggregateSearchCriteria(paymentCardId));

            assertNotNull(aggregateTransactionFootprints);

            LOGGER.info("{}", aggregateTransactionFootprints);
        } catch (ServiceException e) {
            LOGGER.info(ADD_CARD_API_CALL_FAILED_WITH_ERROR_MSG, e.getServiceErrors());
            Assertions.fail(e.getMessage());
        }


    }

    /**
     * Use case 6. View Aggregate Carbon Impact
     */
    @Test
    @DisplayName("Fetch the historical transaction footprint data")
    @Order(3)
    void historicalTransactionFootprints() {

        try {
            HistoricalTransactionFootprints historicalTransactionFootprints = paymentCardService.getPaymentCardTransactionHistory(
                    paymentCardId, "2020-09-19", "2020-10-01", 0, 50);

            assertNotNull(historicalTransactionFootprints);

            LOGGER.info("{}", historicalTransactionFootprints);
        } catch (ServiceException e) {
            LOGGER.info(ADD_CARD_API_CALL_FAILED_WITH_ERROR_MSG, e.getServiceErrors());
            Assertions.fail(e.getMessage());
        }


    }

    /**
     * Test with different Aggregate type, supported values are as follows:
     * 0=daily
     * 1=weekly
     * 2=monthly
     * 3=yearly
     */
    private static AggregateSearchCriteria mockAggregateSearchCriteria(String paymentCardId) {

        List<String> paymentCardIds = Collections.singletonList(paymentCardId);
        return new AggregateSearchCriteria().paymentCardIds(paymentCardIds).aggregateType(0);
    }

    private static void setPaymentCardId(String paymentCardId) {
        PaymentCardServiceTest.paymentCardId = paymentCardId;
    }
}