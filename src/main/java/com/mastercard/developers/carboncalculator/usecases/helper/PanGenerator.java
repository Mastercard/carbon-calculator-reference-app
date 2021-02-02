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
package com.mastercard.developers.carboncalculator.usecases.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

/**
 * Random FPAN generator.
 */
public class PanGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanGenerator.class);
    private static final SecureRandom random = new SecureRandom();

    private PanGenerator() {
    }

    /**
     * @param bin Bank identification number
     * @return Randomly generated FPAN
     */
    public static String generateFPAN(String bin) {

        int length = 16; //Length of FPAN
        int randomNumberLength = length - (bin.length() + 1);

        StringBuilder builder = new StringBuilder(bin);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }

        int checkDigit = getCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    private static int getCheckDigit(String number) {

        int sum = 0;
        for (int i = 0; i < number.length(); i++) {

            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        int mod = sum % 10;
        return ((mod == 0) ? 0 : 10 - mod);
    }

    public static void main(String[] args) {
        String fpan = generateFPAN("5425");
        LOGGER.info("{}", fpan);
    }

}