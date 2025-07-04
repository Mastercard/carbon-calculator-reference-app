# Carbon Calculator Reference App
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_Carbon-Calculator-Reference-App&metric=alert_status)](https://sonarcloud.io/dashboard?id=Mastercard_Carbon-Calculator-Reference-App)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_Carbon-Calculator-Reference-App&metric=coverage)](https://sonarcloud.io/dashboard?id=Mastercard_Carbon-Calculator-Reference-App)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Mastercard_Carbon-Calculator-Reference-App&metric=code_smells)](https://sonarcloud.io/dashboard?id=Mastercard_Carbon-Calculator-Reference-App)
[![](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Mastercard/carbon-calculator-reference-app/blob/master/LICENSE)
## Table of Contents
- [Overview](#overview)
- [Requirements](#requirements)
- [Frameworks/Libraries](#frameworks)
- [Integrating with OpenAPI Generator](#OpenAPI_Generator)
- [Configuration](#configuration)
- [Use-Cases](#use-cases)
- [Execute the Use-Cases](#execute-the-use-cases)
- [Service Documentation](#documentation)
- [API Reference](#api-reference)
- [Support](#support)
- [License](#license)

## Overview  <a name="overview"></a>
This is a reference application to demonstrate how Carbon Calculator APIs can be used.
To call these APIs, consumer key and .p12 file are required from your project on Mastercard Developers.

## Requirements  <a name="requirements"></a>

- Java 17
- IntelliJ IDEA (or any other IDE)

## Frameworks/Libraries <a name="frameworks"></a>
- Spring Boot
- Apache Maven
- OpenAPI Generator

## Integrating with OpenAPI Generator <a name="OpenAPI_Generator"></a>

OpenAPI Generator generates API client libraries from OpenAPI Specs. It provides generators and library templates for supporting multiple languages and frameworks.
Check [Generating and Configuring a Mastercard API Client](https://developer.mastercard.com/platform/documentation/security-and-authentication/generating-and-configuring-a-mastercard-api-client/) to know more about how to generate a simple API client for consuming APIs.


### Configuring Payload Encryption
The [Mastercard Encryption Library](https://github.com/Mastercard/client-encryption-java) provides interceptor class that you can use when configuring your API client. This [interceptor](https://github.com/Mastercard/client-encryption-java#usage-of-the-okhttpfieldlevelencryptioninterceptor-openapi-generator-4xy) will encrypt payload before sending the request.

**Encryption Config**
```
FieldLevelEncryptionConfig config = FieldLevelEncryptionConfigBuilder
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
```

See also: 
- [Securing Sensitive Data Using Payload Encryption](https://developer.mastercard.com/platform/documentation/security-and-authentication/securing-sensitive-data-using-payload-encryption/).

## Configuration <a name="configuration"></a>
1. Create your account on [Mastercard Developers](https://developer.mastercard.com/) if you don't have it already.
2. Create a new project here and add ***Carbon Calculator*** to it and click continue.
3. Download Sandbox Signing Key, a ```.p12``` file will be downloaded.
4. In the Client Encryption Keys section of the dashboard, click on the ```Actions``` dropdown and download the client encryption key, a ``.pem``` file will be downloaded. 
5. Copy the downloaded ```.p12``` and ```.pem``` files to ```src/main/resources``` folder in your code.
6. Open ```src/main/resources/application.yml``` and configure:
    - ```mastercard.api.environment.key-file ```- Path to keystore (.p12) file, just change the name as per the downloaded file in step 5. 
    - ```mastercard.api.authentication.consumer-key``` - Copy the Consumer key from "Sandbox/Production Keys" section on your project page
    - ```mastercard.api.authentication.keystore-alias``` - Alias of your key. Default key alias for sandbox is ```keyalias```.
    - ```mastercard.api.authentication.keystore-password``` -  Password of your Keystore. Default keystore password for sandbox project is ```keystorepassword```.
    - ```mastercard.api.encryption.key-file ```- Path to encryption key (.pem) file, just change the name as per the downloaded file in step 5. 
    ```mastercard.api.encryption.fingerprint ```- Fingerprint, copy the fingerprint from Client Encryption Keys section. If you have multiple client encryption keys then copy the fingerprint of the key which you want to use.
    - ```test.data.bin``` - Update this with one of your supported BINs

## Use-Cases <a name="use-cases"></a>
1. **Calculate Transaction Footprints**   
Calculates carbon emission based on payment transactions.

2. **Supported Currencies**    
Provides a list of Currencies supported by the application.

3. **Supported Merchant Categories**<br/>
Provides a list of Merchant Category Code (MCC) supported by the application.  

4. **Enroll FPAN**  
Allows a registered Service Provider to add a new Payment Card under its profile.

5. **View Historical Carbon Impact**  
Fetches historical transactions and their footprints for a registered payment card.

6. **View Aggregate Carbon Impact**  
Fetches carbon score for registered payment card/s and aggregates the same on daily, weekly, monthly and yearly basis. 

7. **Get Service Provider**  
Fetches service provider details.

8. **Update Service Provider**<br/>
Allows a registered Service Provider to update its configuration on the server. A Service Provider should mandatorily call this API first after their successful project creation on Mastercard Developers Platform before they can successfully call other APIs. 

9. **Delete FPAN** <br/>
Allows a registered Service Provider to delete one or more Payment Cards from its profile. Any data associated with a requested paymentCardId will also be deleted permanently.

10. **Bulk Enroll FPAN**  
Allows a registered Service Provider to add list of new Payment Cards under its profile

11. **Calculate Carbon Scores** <br/>
Calculates carbon footprints for various indices. It selects the most relevant index based on input data and calculates the carbon footprint accordingly. This API is applicable for **India region** only.

12. **Get Survey Questions** <br/>
The Survey API offers essential features for seamless onboarding onto the Engagement Services platform, enabling integration with diverse surveys designed to fine-tune a Climate Profile.

13. **Update Profiles** <br/>
The Profile Service calculates a climate profile based on the answers from a Survey obtained from the Surveys Service.

14. **Update Insights** <br/>
The Insights Service delivers insights and other suggestions that can inform you about what you can do in order to reduce your footprint.

15. **Get Insights by ID** <br/>
Fetches a given Insight and marks it as viewed in your profile. Issuer has to save the profile locally and send it in future communication with Engagement Services.

16. **Get Benchmarks** <br/>
Returns a benchmark for a specific country for the year 2020, monthly or yearly.

17. **Get Personas** <br/>
A persona is a type of character described based on the answers a user has given to survey questions. There is a list of personas, and one of them is assigned to the profile during profile creation.

18. **Get Comparisons** <br/>
Fetches comparisons data by comparing tonnes of Co2 with any day to day life examples.

More details can be found [here](https://stage.developer.mastercard.com/drafts/carbon-calculator/staging/documentation/use-cases/).    


## Execute the Use-Cases   <a name="execute-the-use-cases"></a>
1. Run ```mvn clean install``` from the root of the project directory.
2. There are two ways to execute the use-cases:
    1. Execute the use-cases(test cases):  
        - Go to ```src/test/java/com/mastercard/developers/carboncalculator/``` folder.  
        - Execute each test cases.
        - In ```PaymentCardServiceSIT.java```, note that a random FPAN is generated starting with your BIN while adding a new payment card and the paymentCardId of this card is used while executing other two test-cases.
    
    2. Use REST API based Client( such as [Insomnia](https://insomnia.rest/download/core/) or [Postman](https://www.postman.com/downloads/))  
        - Run ```mvn spring-boot:run``` command to run the application.  
        - Use any REST API based Client to test the functionality. Below are the APIs exposed by this application:  
            - APIs exposed on Global Region:
                - POST <Host>/demo/transaction-footprints      
                - GET <Host>/demo/supported-mccs  
                - GET <Host>/demo/supported-currencies  
                - POST <Host>/demo/payment-cards      
                - POST <Host>/demo/aggregate-transaction-footprints  
                - GET <Host>/demo/historical/{paymentcard_id}/transaction-footprints  
                - GET <Host>/demo/service-providers  
                - PUT <Host>/demo/service-providers <br>
                - POST <Host>/demo/payment-card-deletions   
                - POST <Host>/demo/service-providers/payment-cards 
                - GET <Host>/demo/surveys <br>
                - PUT <Host>/demo/profiles <br>
                - PUT <Host>/demo/insights <br>
                - PUT <Host>/demo/insights/{id} <br>
                - GET <Host>/demo/benchmarks        
                - GET <Host>/demo/personas
                - GET <Host>/demo/comparisons
          - APIs exposed on India Region:
                - POST <Host>/demo/carbon-scores
                                                                               
## Service Documentation <a name="documentation"></a>

Carbon Calculator documentation can be found [here](https://stage.developer.mastercard.com/drafts/carbon-calculator/staging/documentation/).


## API Reference <a name="api-reference"></a>
The Swagger API specification can be found [here](https://stage.developer.mastercard.com/drafts/carbon-calculator/staging/documentation/api-reference/).

## Support <a name="support"></a>
Please send an email to **apisupport@mastercard.com** with any questions or feedback you may have.


## License <a name="license"></a>
<p>Copyright 2021 Mastercard</p>
<p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at:</p>
<pre><code>   http://www.apache.org/licenses/LICENSE-2.0
</code></pre>
<p>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.</p>