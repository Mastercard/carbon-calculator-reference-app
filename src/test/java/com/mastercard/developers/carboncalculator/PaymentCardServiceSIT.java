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

import com.mastercard.developers.carboncalculator.service.AddCardService;
import com.mastercard.developers.carboncalculator.service.PaymentCardService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.PaymentCard;
import org.openapitools.client.model.PaymentCardEnrolment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.mastercard.developers.carboncalculator.usecases.helper.PanGenerator.generateFPAN;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentCardServiceSIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardServiceSIT.class);
    private static final String ADD_CARD_API_CALL_FAILED_WITH_ERROR_MSG = "Add Card API call failed with error msg {}";
    private static final String DELETE_CARDS_API_CALL_FAILED_WITH_ERROR_MSG = "Delete Card API call failed with error msg {}";
    private static final String X_OPENAPI_CLIENTID = "x-openapi-clientid";
    private static final String CLIENTID = "cNU2Re-v0oKw95zjfs7G60yICaTtQtyEt-vKZrnjd34ea14e";
    private static final String CHANNEL = "CC";

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
     * Use case 4. Enrol FPAN(To be deprecated)
     */
    @Test
    @DisplayName("Register a new Payment Card")
    @Order(1)
    void registerPaymentCard() {

        var paymentCard = new PaymentCard().fpan(generateFPAN(bin)).cardBaseCurrency(cardBaseCurrency);

        try {

            var paymentCardReference = addCardService.registerPaymentCard(paymentCard);

            assertNotNull(paymentCardReference);
            assertNotNull(paymentCardReference.getPaymentCardId());
            assertNotNull(paymentCardReference.getLast4fpan());

            setPaymentCardId(paymentCardReference.getPaymentCardId());


        } catch (ApiException e) {
            LOGGER.info(ADD_CARD_API_CALL_FAILED_WITH_ERROR_MSG, e.getMessage());
            Assertions.fail(e.getMessage());
        }


    }

    /**
     * Use case 5. View Historical Carbon Impact
     */
    @Test
    @DisplayName("Fetch the historical transaction footprint data")
    @Order(3)
    void historicalTransactionFootprints() {

        try {
            var historicalTransactionFootprints = paymentCardService.getPaymentCardTransactionHistory(
                    paymentCardId, "2020-09-19", "2020-10-01", 0, 50);

            assertNotNull(historicalTransactionFootprints);

            LOGGER.info("{}", historicalTransactionFootprints);
        } catch (ApiException e) {
            LOGGER.info(ADD_CARD_API_CALL_FAILED_WITH_ERROR_MSG, e.getResponseBody());
            Assertions.fail(e.getMessage());
        }


    }

    private static void setPaymentCardId(String paymentCardId) {
        PaymentCardServiceSIT.paymentCardId = paymentCardId;
    }



    /**
     * Use case 4. Delete Payment Cards
     */
    @Test
    @DisplayName("Delete Registered payment cards")
    @Order(4)
    void deletePaymentCards() {

        final List<String> cardIds = of(PaymentCardServiceSIT.paymentCardId);

        try {
            PaymentCardService paymentCardService1 =  Mockito.spy(paymentCardService);
            paymentCardService1.deletePaymentCards(cardIds);
            verify(paymentCardService1,times(1)).deletePaymentCards(cardIds);

        } catch (ApiException e) {
            LOGGER.info(DELETE_CARDS_API_CALL_FAILED_WITH_ERROR_MSG, e.getResponseBody());
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * Use case 4. Delete Payment Cards
     */
    @Test
    @DisplayName("Delete Registered payment card")
    @Order(5)
    void deletePaymentCard() {

        try {
            PaymentCardService paymentCardService1 =  Mockito.spy(paymentCardService);
            paymentCardService1.deletePaymentCard(PaymentCardServiceSIT.paymentCardId, X_OPENAPI_CLIENTID, CHANNEL, CLIENTID);
            verify(paymentCardService1,times(1)).deletePaymentCard(PaymentCardServiceSIT.paymentCardId, X_OPENAPI_CLIENTID, CHANNEL, CLIENTID);

        } catch (ApiException exception) {
            LOGGER.info(DELETE_CARDS_API_CALL_FAILED_WITH_ERROR_MSG, exception.getResponseBody());
            Assertions.fail(exception.getMessage());
        }
    }


    /**
     * Use case 11. Enrol bulk FPAN
     */
    @Test
    @DisplayName("Enroll bulk payment cards")
    void enrollBulkPaymentCardsServiceProvider() {

        PaymentCard paymentCard1 = new PaymentCard().fpan(generateFPAN(bin)).cardBaseCurrency(cardBaseCurrency);
        PaymentCard paymentCard2 = new PaymentCard().fpan(generateFPAN(bin)).cardBaseCurrency(cardBaseCurrency);

        List<PaymentCard> paymentCards = new ArrayList<>();
        paymentCards.add(paymentCard1);
        paymentCards.add(paymentCard2);

        try {
            List<PaymentCardEnrolment> paymentCardEnrolmentList = addCardService.registerBatchPaymentCardsServiceProvider(paymentCards);

            LOGGER.info("Enrolled payment cards are {}", paymentCardEnrolmentList);

            assertNotNull(paymentCardEnrolmentList);

            paymentCardEnrolmentList
                    .stream()
                    .forEach(paymentCardEnrolment -> {
                        assertNotNull(paymentCardEnrolment.getPaymentCardId());
                        assertNotNull(paymentCardEnrolment.getLast4fpan());
                    }
            );


        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }


}