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

import com.mastercard.developers.carboncalculator.service.ReceiveTransactionNotificationService;
import org.openapitools.client.model.NotifyTransactionFootprint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Service Providers need to implement this API  and register the URL with Mastercard to receive notification of transaction footprints for corresponding real-time auth transaction for a registered payment card. Service Providers can then notify the same to the payment card user.
 */
@RestController
@RequestMapping("/demo")
public class NotifyTransactionFootprintController {

    private final ReceiveTransactionNotificationService receiveTransactionNotificationService;

    public NotifyTransactionFootprintController(ReceiveTransactionNotificationService receiveTransactionNotificationService) {
        this.receiveTransactionNotificationService = receiveTransactionNotificationService;
    }

    @PostMapping("/payment-cards/notify-transaction-footprints")
    public String transactionFootPrintsNotification(@RequestBody NotifyTransactionFootprint mcNotifyTransactionFootprint) {
        return receiveTransactionNotificationService.notifyTransactionFootprint(mcNotifyTransactionFootprint);
    }
}