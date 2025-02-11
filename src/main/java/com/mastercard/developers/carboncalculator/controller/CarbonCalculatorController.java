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
package com.mastercard.developers.carboncalculator.controller;

import com.mastercard.developers.carboncalculator.service.AddCardService;
import com.mastercard.developers.carboncalculator.service.EnvironmentalImpactService;
import com.mastercard.developers.carboncalculator.service.SupportedParametersService;
import com.mastercard.developers.carboncalculator.service.PaymentCardService;
import com.mastercard.developers.carboncalculator.service.ServiceProviderService;
import org.openapitools.client.ApiException;

import org.openapitools.client.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mastercard.developers.carboncalculator.util.EncryptionHelper.getErrorObjectResponseEntity;


/**
 * This controller class exposes the following endpoints
 * 1. /transaction-footprints
 * 2. /supported-currencies
 * 3. /supported-mccs
 * 4. /add-card
 * 5. /aggregate-transaction-footprints
 * 6. /historical-transaction-footprints
 * 7. /service-provider
 * 8. /payment-cards/{payment_card_id}/profiles
 * <p>
 * Issuer can consume these endpoints directly through their web or mobile application or add their implementation on top of this.
 */
@RestController
@RequestMapping("/demo")
public class CarbonCalculatorController {

    private final EnvironmentalImpactService environmentalImpactService;
    private final SupportedParametersService supportedParametersService;
    private final PaymentCardService paymentCardService;
    private final ServiceProviderService serviceProviderService;
    private final AddCardService addCardService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CarbonCalculatorController.class);

    public CarbonCalculatorController(EnvironmentalImpactService environmentalImpactService, SupportedParametersService supportedParametersService, PaymentCardService paymentCardService, ServiceProviderService serviceProviderService, AddCardService addCardService) {
        this.environmentalImpactService = environmentalImpactService;
        this.supportedParametersService = supportedParametersService;
        this.paymentCardService = paymentCardService;
        this.serviceProviderService = serviceProviderService;
        this.addCardService = addCardService;
    }

    @PostMapping("/transaction-footprints")
    public ResponseEntity<Object> calculateFootprints(@RequestBody List<TransactionData> mcTransactions) {

        List<TransactionFootprintData> footprintData = null;
        try {
            footprintData = environmentalImpactService.calculateFootprints(mcTransactions);
        } catch (ApiException exception) {
            LOGGER.error("transaction-footprints apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(footprintData);

    }

    @PostMapping("/carbon-scores")
    public ResponseEntity<Object> calculateCarbonScoreFootprints(@RequestHeader("x-openapi-clientid") String clientId, @RequestBody ScoreRequestDetails scoreRequestDetails, String channel, String origMcApiClientId) {

        CarbonScoreDetails footprintData = null;
        try {
            footprintData = environmentalImpactService.calculateCarbonScoreFootprints(scoreRequestDetails, clientId, channel, origMcApiClientId);
        } catch (ApiException exception) {
            LOGGER.error("carbon-scores apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(footprintData);

    }

    @GetMapping("/supported-currencies")
    public ResponseEntity<Object> getSupportedCurrencies() {
        List<Currency> currencyList = null;
        try {
            currencyList = supportedParametersService.getSupportedCurrencies();
        } catch (ApiException exception) {
            LOGGER.error("supported-currencies apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(currencyList);
    }

    @GetMapping("/supported-mccs")
    public ResponseEntity<Object> getSupportedMerchantCategories() {
        List<MerchantCategory> merchantCategories = null;
        try {
            merchantCategories = supportedParametersService.getSupportedMerchantCategories();
        } catch (ApiException exception) {
            LOGGER.error("supported-mccs apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(merchantCategories);
    }

    @PostMapping("/payment-cards")
    public ResponseEntity<Object> registerPaymentCard(@RequestBody PaymentCard paymentCard) {
        PaymentCardReference paymentCardReference = null;
        try {
            paymentCardReference = addCardService.registerPaymentCard(paymentCard);
        } catch (ApiException exception) {
            LOGGER.error("payment-cards apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(paymentCardReference);
    }

    @PostMapping("/payment-cards/transaction-footprints/aggregates")
    public ResponseEntity<Object> getPaymentCardAggregateTransaction(@RequestHeader("x-openapi-clientid") String clientId,
                                                                     @RequestBody AggregateSearchCriteria aggregateSearchCriteria, @RequestHeader("channel") String channel, @RequestHeader("origMcApiClientId") String origMcApiClientId) {
        AggregateTransactionFootprints footprintData = null;
        try {
            footprintData = environmentalImpactService.getPaymentCardAggregateTransactions(clientId, aggregateSearchCriteria, channel, origMcApiClientId);
        } catch (ApiException exception) {
            LOGGER.error("Get transaction-footprints-aggregates apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(footprintData);
    }

    @GetMapping("/historical/{paymentcard_id}/transaction-footprints")
    public ResponseEntity<Object> getPaymentCardTransactionHistory(@PathVariable("paymentcard_id") String paymentCardId, @RequestParam(value = "from_date") String fromDate, @RequestParam(value = "to_date") String toDate, @RequestParam(value = "offset") int offset, @RequestParam(value = "limit", required = false, defaultValue = "50") int limit) {
        HistoricalTransactionFootprints transactionFootprints = null;
        try {
            transactionFootprints = paymentCardService.getPaymentCardTransactionHistory(paymentCardId, fromDate, toDate, offset, limit);
        } catch (ApiException exception) {
            LOGGER.error("Get historical-transaction-footprints apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(transactionFootprints);
    }

    @GetMapping("/service-providers")
    public ResponseEntity<Object> getServiceProvider() {
        ServiceProvider serviceProvider = null;
        try {
            serviceProvider = serviceProviderService.getServiceProvider();
        } catch (ApiException exception) {
            LOGGER.error("Get service-providers apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(serviceProvider);
    }

    @PostMapping("/payment-card-deletions")
    public ResponseEntity<Object> deletePaymentCards(@RequestBody List<String> paymentCards) {
        try {
            paymentCardService.deletePaymentCards(paymentCards);
        } catch (ApiException exception) {
            LOGGER.error("delete payment-card-deletions apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/service-providers/payment-cards/{paymentcard_id}")
    public ResponseEntity<Object> deletePaymentCard(@PathVariable("paymentcard_id") String paymentCardId,
                                                    @RequestHeader("x-openapi-clientid") String clientId, @RequestHeader("channel") String channel, @RequestHeader("origMcApiClientId") String origMcApiClientId) {
        try {
            paymentCardService.deletePaymentCard(paymentCardId, clientId, channel, origMcApiClientId);
        } catch (ApiException exception) {
            LOGGER.error("delete service-providers-payment-cards apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/service-providers")
    public ResponseEntity<Object> updateServiceProvider(@RequestBody ServiceProviderConfig serviceProviderConfig) {

        ServiceProvider serviceProvider = null;

        try {
            serviceProvider = serviceProviderService.updateServiceProvider(serviceProviderConfig);
        } catch (ApiException exception) {
            LOGGER.error("service-providers apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(serviceProvider);
    }

    @PostMapping("/payment-card-enrolments")
    public ResponseEntity<Object> addBulkPaymentCards(@RequestBody List<PaymentCard> paymentCards) throws ApiException {
        List<PaymentCardEnrolment> paymentCardEnrolments = null;
        try {
            paymentCardEnrolments = addCardService.registerBatchPaymentCards(paymentCards);
        } catch (ApiException exception) {
            LOGGER.error("payment-card-enrolments apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(paymentCardEnrolments);
    }

    @PostMapping("/service-providers/payment-cards")
    public ResponseEntity<Object> addBatchPaymentCards(@RequestBody List<PaymentCard> paymentCards) {
        List<PaymentCardEnrolment> paymentCardEnrolments = null;
        try {
            paymentCardEnrolments = addCardService.registerBatchPaymentCardsServiceProvider(paymentCards);
        } catch (ApiException exception) {
            LOGGER.error("service-providers-payment-cards apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(paymentCardEnrolments);
    }

    @PostMapping("/payment-cards/{payment_card_id}/profiles")
    public ResponseEntity<Object> addProfileToPaymentCard(@PathVariable("payment_card_id") String paymentCardId,@RequestBody CardClimateProfile cardClimateProfile) {

        PaymentCardProfile paymentCardProfile = null;
        try {
            paymentCardProfile = environmentalImpactService.addProfileToPaymentCard(paymentCardId, cardClimateProfile);
        } catch (ApiException exception) {
            LOGGER.error("transaction-footprints apiException : {}", exception.getResponseBody());
            return getErrorObjectResponseEntity(exception);
        }
        return ResponseEntity.ok(paymentCardProfile);

    }


}
