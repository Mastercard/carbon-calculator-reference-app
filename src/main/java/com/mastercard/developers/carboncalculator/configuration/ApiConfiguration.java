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
package com.mastercard.developers.carboncalculator.configuration;

import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import java.security.PrivateKey;

/**
 * Api Client Setup
 */

@Configuration
public class ApiConfiguration {

    @Value("${mastercard.api.authentication.consumer-key}")
    private String consumerKey;

    @Value("${mastercard.api.authentication.keystore-alias}")
    private String keyAlias;

    @Value("${mastercard.api.authentication.keystore-password}")
    private char[] userPasscode;

    @Value("${mastercard.api.authentication.key-file}")
    private Resource p12File;

    @Value("${mastercard.api.environment.base-path}")
    private String basePath;

    @Value("${mastercard.api.environment.base-path-engagement}")
    private String basePathEngagement;


    @Value("${mastercard.api.encryption.key-file}")
    private Resource encryptionKeyFile;

    @Value("${mastercard.api.encryption.fingerprint}")
    private String encryptionFingerprint;

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getBasePath() {
        return basePath;
    }

    public Resource getEncryptionKeyFile() {
        return encryptionKeyFile;
    }

    @Bean
    @Primary
    public ApiClient setupApiClient() {
        var apiClient = new ApiClient();

        apiClient.setBasePath(basePath);
        apiClient.setHttpClient(
                apiClient.getHttpClient()
                        .newBuilder()
                        .addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, getSigningKey()))
                        .build()
        );
        apiClient.setDebugging(false);

        return apiClient;
    }
    @Bean
    @Qualifier("apiClientEngagement")
    public ApiClient setupApiClientEngagement() {
        var apiClient = new ApiClient();

        apiClient.setBasePath(basePathEngagement);
        apiClient.setHttpClient(
                apiClient.getHttpClient()
                        .newBuilder()
                        .addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, getSigningKey()))
                        .build()
        );
        apiClient.setDebugging(false);

        return apiClient;
    }

    public PrivateKey getSigningKey() {
        try {
            return AuthenticationUtils.loadSigningKey(
                    p12File.getFile().getAbsolutePath(),
                    keyAlias,
                    String.valueOf(userPasscode));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}
