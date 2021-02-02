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
package com.mastercard.developers.carboncalculator.util;

import com.mastercard.developer.encryption.FieldLevelEncryptionConfig;
import com.mastercard.developer.encryption.FieldLevelEncryptionConfigBuilder;
import com.mastercard.developer.utils.EncryptionUtils;
import com.mastercard.developers.carboncalculator.exception.ServiceException;
import org.springframework.core.io.Resource;

import java.security.cert.X509Certificate;

public class EncryptionHelper {

    private EncryptionHelper() {

    }

    /**
     * This method is use setup the encryption config to perform encryption on real data.
     * * (See Mastercard Encryption Library <a href>https://github.com/Mastercard/client-encryption-java</a>)
     * *
     *
     * @param encryptionKeyFile Resource clientPem file.
     * @return FieldLevelEncryptionConfig
     */
    public static FieldLevelEncryptionConfig encryptionConfig(Resource encryptionKeyFile)
            throws ServiceException {
        try {

            X509Certificate cert = (X509Certificate) EncryptionUtils.loadEncryptionCertificate(
                    encryptionKeyFile.getFile().getAbsolutePath());


            return FieldLevelEncryptionConfigBuilder
                    .aFieldLevelEncryptionConfig()
                    .withEncryptionCertificate(cert)
                    .withEncryptionPath("$", "$")
                    .withEncryptedValueFieldName("encryptedData")
                    .withEncryptedKeyFieldName("encryptedKey")
                    .withOaepPaddingDigestAlgorithmFieldName("oaepHashingAlgorithm")
                    .withOaepPaddingDigestAlgorithm("SHA-256")
                    .withEncryptionKeyFingerprintFieldName("publicKeyFingerprint")
                    .withIvFieldName("iv")
                    .withFieldValueEncoding(FieldLevelEncryptionConfig.FieldValueEncoding.HEX)
                    .build();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
