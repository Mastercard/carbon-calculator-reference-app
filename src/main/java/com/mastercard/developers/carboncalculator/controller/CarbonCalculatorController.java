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

import com.mastercard.developers.carboncalculator.exception.ServiceException;
import com.mastercard.developers.carboncalculator.service.*;
import org.openapitools.client.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller class exposes the following endpoints
 * 1. /transaction-footprints
 * 2. /supported-currencies
 * 3. /supported-mccs
 * 4. /add-card
 * 5. /aggregate-transaction-footprints
 * 6. /historical-transaction-footprints
 * 7. /service-provider
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

    public CarbonCalculatorController(EnvironmentalImpactService environmentalImpactService, SupportedParametersService supportedParametersService, PaymentCardService paymentCardService, ServiceProviderService serviceProviderService, AddCardService addCardService) {
        this.environmentalImpactService = environmentalImpactService;
        this.supportedParametersService = supportedParametersService;
        this.paymentCardService = paymentCardService;
        this.serviceProviderService = serviceProviderService;
        this.addCardService = addCardService;
    }

    @PostMapping("/transaction-footprints")
    public ResponseEntity<List<TransactionFootprint>> calculateFootprints(@RequestBody List<Transaction> mcTransactions) throws ServiceException {
        return ResponseEntity.ok(environmentalImpactService.calculateFootprints(mcTransactions));

    }

    @GetMapping("/supported-currencies")
    public ResponseEntity<List<Currency>> getSupportedCurrencies() throws ServiceException {
        return ResponseEntity.ok(supportedParametersService.getSupportedCurrencies());
    }

    @GetMapping("/supported-mccs")
    public ResponseEntity<List<MerchantCategory>> getSupportedMerchantCategories() throws ServiceException {
        return ResponseEntity.ok(supportedParametersService.getSupportedMerchantCategories());
    }

    @PostMapping("/payment-cards")
    public ResponseEntity<PaymentCardReference> registerPaymentCard(@RequestBody PaymentCard paymentCard) throws ServiceException {
        return ResponseEntity.ok(addCardService.registerPaymentCard(paymentCard));
    }

    @PostMapping("/aggregate-transaction-footprints")
    public ResponseEntity<List<AggregateTransactionFootprint>> getPaymentCardAggregateTransactions(@RequestBody AggregateSearchCriteria aggregateSearchCriteria) throws ServiceException {
        return ResponseEntity.ok(paymentCardService.getPaymentCardAggregateTransactions(aggregateSearchCriteria));
    }

    @GetMapping("/historical/{paymentcard_id}/transaction-footprints")
    public ResponseEntity<HistoricalTransactionFootprints> getPaymentCardTransactionHistory(@PathVariable("paymentcard_id") String paymentCardId, @RequestParam(value = "from_date") String fromDate, @RequestParam(value = "from_date") String toDate, @RequestParam(value = "offset") int offset, @RequestParam(value = "limit", required = false, defaultValue = "50") int limit) throws ServiceException {
        return ResponseEntity.ok(
                paymentCardService.getPaymentCardTransactionHistory(paymentCardId, fromDate, toDate, offset, limit));
    }

    @GetMapping("/service-providers")
    public ResponseEntity<ServiceProvider> getServiceProvider() throws ServiceException {
        return ResponseEntity.ok(serviceProviderService.getServiceProvider());
    }


}
