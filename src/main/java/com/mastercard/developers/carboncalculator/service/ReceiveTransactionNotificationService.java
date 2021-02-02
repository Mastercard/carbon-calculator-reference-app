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

import org.openapitools.client.model.NotifyTransactionFootprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReceiveTransactionNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveTransactionNotificationService.class);


    public String notifyTransactionFootprint(NotifyTransactionFootprint mcNotifyTransactionFootprint) {
        LOGGER.info("{}", mcNotifyTransactionFootprint);

        // Use this transaction notification to notify your user about his transaction footprint.
        return ("Transaction Footprint Notification Received Successfully.");
    }

}
