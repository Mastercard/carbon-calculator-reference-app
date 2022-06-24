package com.mastercard.developers.carboncalculator.controller;

import com.google.gson.Gson;
import com.mastercard.developers.carboncalculator.configuration.ApiConfiguration;
import com.mastercard.developers.carboncalculator.service.*;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mastercard.developers.carboncalculator.service.MockData.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CarbonCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private EnvironmentalImpactService environmentalImpactService;

    @MockBean
    private ApiConfiguration apiConfiguration;

    @MockBean
    private SupportedParametersService supportedParametersService;

    @MockBean
    private PaymentCardService paymentCardService;

    @MockBean
    private AddCardService addCardService;

    @MockBean
    private ServiceProviderService serviceProviderApi;

    @MockBean
    private ServiceProviderConfig serviceProviderConfig;

    @Value("${test.data.bin}")
    String bin;

    @Value("${test.data.card-base-currency}")
    String cardBaseCurrency;

    @Test
    void calculateFootprints() throws Exception {

        List<TransactionData> mcTransactions = transactions();

        when(environmentalImpactService.calculateFootprints(mcTransactions)).thenReturn(
                transactionFootprints());

        MvcResult mvcResult = mockMvc.perform(post("/demo/transaction-footprints").contentType(
                MediaType.APPLICATION_JSON).content(
                gson.toJson(mcTransactions))).andExpect(
                status().isOk()).andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);

    }

    @Test
    void getSupportedCurrencies() throws Exception {

        when(supportedParametersService.getSupportedCurrencies()).thenReturn(
                currencies());

        MvcResult mvcResult = this.mockMvc
                .perform(get("/demo/supported-currencies"))
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);

    }

    @Test
    void getSupportedMerchantCategories() throws Exception {

        when(supportedParametersService.getSupportedMerchantCategories()).thenReturn(
                merchantCategories());

        MvcResult mvcResult = this.mockMvc
                .perform(get("/demo/supported-mccs"))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);

    }

    @Test
    void registerPaymentCard() throws Exception {


        when(addCardService.registerPaymentCard(any())).thenReturn(
                paymentCardReference());

        MvcResult mvcResult = this.mockMvc.perform(post("/demo/payment-cards").contentType(
                MediaType.APPLICATION_JSON).content(
                gson.toJson(paymentCard()))).andExpect(
                status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void aggregateTransactionFootprints() throws Exception {

        AggregateSearchCriteria aggregateSearchCriteria = new AggregateSearchCriteria().paymentCardIds(
                Collections.singletonList("paymentCardId")).aggregateType(0);

        when(paymentCardService.getPaymentCardAggregateTransactions(aggregateSearchCriteria)).thenReturn(
                new ArrayList<>());

        MvcResult mvcResult2 = this.mockMvc
                .perform(post("/demo/aggregate-transaction-footprints")
                        .contentType(
                                MediaType.APPLICATION_JSON).content(
                                gson.toJson(aggregateSearchCriteria))).andExpect(
                        status().isOk()).andReturn();

        String response2 = mvcResult2.getResponse().getContentAsString();
        assertNotNull(response2);
    }

    @Test
    void historicalTransactionFootprints() throws Exception {
        when(paymentCardService.getPaymentCardTransactionHistory("paymentCardId", "2020-09-19",
                "2020-10-01", 0,
                1)).thenReturn(
                new HistoricalTransactionFootprints());
        MvcResult mvcResult3 = this.mockMvc
                .perform(get("/demo/historical/{paymentcard_id}/transaction-footprints".replace("{paymentcard_id}",
                        "paymentCardId"))
                        .params(prepareRequestParams()))
                .andExpect(status().isOk()).andReturn();

        String response3 = mvcResult3.getResponse().getContentAsString();
        assertNotNull(response3);
    }

    @Test
    void getServiceProvider() throws Exception {
        when(serviceProviderApi.getServiceProvider()).thenReturn(serviceProvider());

        MvcResult mvcResult = this.mockMvc
                .perform(get("/demo/service-providers"))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void deletePaymentCards() throws Exception {
        doNothing().when(paymentCardService).deletePaymentCards(any());

        this.mockMvc.perform(post("/demo/payment-card-deletions").contentType(
                MediaType.APPLICATION_JSON).content(
                gson.toJson(listPaymentCards()))).andExpect(
                status().isAccepted()).andReturn();
    }

    @Test
    void batchRegistrationPaymentCards() throws Exception {


        when(addCardService.registerBatchPaymentCards(any())).thenReturn(
                batchPaymentEnrollment());

        MvcResult mvcResult = this.mockMvc.perform(post("/demo/payment-card-enrolments").contentType(
                MediaType.APPLICATION_JSON).content(
                gson.toJson(listPaymentCardReference())))
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void updateServiceProvider() throws Exception {
        when(serviceProviderApi.updateServiceProvider(serviceProviderConfig)).thenReturn(serviceProvider());

        MvcResult mvcResult = this.mockMvc
                .perform(get("/demo/service-providers"))
                .andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }


    private LinkedMultiValueMap<String, String> prepareRequestParams() {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("from_date", "2020-09-19");
        requestParams.add("to_date", "2020-10-01");
        requestParams.add("offset", "0");
        requestParams.add("limit", "1");
        return requestParams;
    }


}
