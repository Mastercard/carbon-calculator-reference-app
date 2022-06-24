package com.mastercard.developers.carboncalculator.service;

import com.mastercard.developers.carboncalculator.exception.ServiceException;
import okhttp3.Call;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.PaymentCardApi;
import org.openapitools.client.model.Error;
import org.openapitools.client.model.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static com.mastercard.developers.carboncalculator.service.MockData.*;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    private static final String SOURCE = "Carbon-Calculator";

    @InjectMocks
    private EnvironmentalImpactService environmentalImpactService;

    @InjectMocks
    private SupportedParametersService supportedParametersService;

    @InjectMocks
    private ServiceProviderService serviceProviderService;

    @InjectMocks
    private ServiceProviderConfig serviceProviderConfig;

    @InjectMocks
    private PaymentCardService paymentCardService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private PaymentCardApi paymentCardApi;

    @BeforeEach
    void setUp() throws Exception {
        when(apiClient.buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(),
                any(), any())).thenReturn(mock(Call.class));
    }

    @Test
    void calculateFootprints() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), transactionFootprints()));

        List<TransactionFootprintData> transactionFootprints = environmentalImpactService.calculateFootprints(
                transactions());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(transactionFootprints);
    }

    @Test
    void getSupportedCurrencies() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), currencies()));
        List<Currency> currencies = supportedParametersService.getSupportedCurrencies();

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(currencies);
    }

    @Test
    void getSupportedMerchantCategories() throws Exception {

        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), merchantCategories()));

        List<MerchantCategory> merchantCategories = supportedParametersService.getSupportedMerchantCategories();

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(merchantCategories);
    }

    @Test
    void serviceProvider() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(201, new HashMap<>(), MockData.serviceProvider()));

        ServiceProvider serviceProvider = serviceProviderService.getServiceProvider();

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(serviceProvider);
    }

    @Test
    void serviceProviderErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));


        ServiceException serviceException = Assertions.assertThrows(ServiceException.class,
                () -> serviceProviderService.getServiceProvider());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(serviceException.getServiceErrors());
        List<Error> errors = serviceException.getServiceErrors().getErrors().getError();
        Assertions.assertFalse(errors.isEmpty());
        errors.forEach(error -> {
            Assertions.assertEquals(SOURCE, error.getSource());
            Assertions.assertFalse(error.getRecoverable());
        });
    }

    @Test
    void updateServiceProviderErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "INVALID_REQUEST_PARAMETER",
                        "One of the request parameters is either invalid or is missing, try again with the correct request",
                        false,
                        "supportedAccountRange must match \\\"^[\\\\d\\\\,]{1,}\\\"")));


        ServiceException serviceException = Assertions.assertThrows(ServiceException.class,
                () -> serviceProviderService.updateServiceProvider(serviceProviderConfig));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(serviceException.getServiceErrors());
        List<Error> errors = serviceException.getServiceErrors().getErrors().getError();
        Assertions.assertFalse(errors.isEmpty());
        errors.forEach(error -> {
            Assertions.assertEquals(SOURCE, error.getSource());
            Assertions.assertFalse(error.getRecoverable());
        });
    }

    @Test
    void updateProvider() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), MockData.serviceProvider()));

        ServiceProviderConfig serviceProviderConfig = new ServiceProviderConfig();
        serviceProviderConfig.setCustomerName("Customer1");

        ServiceProvider serviceProvider = serviceProviderService.updateServiceProvider(serviceProviderConfig);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(serviceProvider);
    }

    @Test
    void historical() throws Exception {
        when(apiClient.escapeString(anyString())).thenReturn("randomString");
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(201, new HashMap<>(), historicalTransactionFootprint()));

        HistoricalTransactionFootprints historicalTransactionFootprints = paymentCardService.getPaymentCardTransactionHistory(
                "testPaymentCardId", "2020-09-19", "2020-10-01", 0, 50);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(historicalTransactionFootprints);
    }

    @Test
    void aggregate() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(201, new HashMap<>(), MockData.aggregateTransactionFootprint()));

        List<AggregateTransactionFootprint> aggregateTransactionFootprints = paymentCardService.getPaymentCardAggregateTransactions(
                aggregateSearchCriteria("testPaymentCardId"));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(aggregateTransactionFootprints);
    }

    @Test
    void calculateFootprintsErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "INVALID_REQUEST_PARAMETER",
                        "One of the request parameters is invalid, try again with correct request.",
                        false,
                        "carbonIndexCalculation.transactions[0].mcc: size must be between 1 and 4")));

        ServiceException serviceException = Assertions.assertThrows(ServiceException.class,
                () -> environmentalImpactService.calculateFootprints(
                        invalidTransactionRequest()));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(serviceException.getServiceErrors());
        List<Error> errors = serviceException.getServiceErrors().getErrors().getError();
        Assertions.assertFalse(errors.isEmpty());
        errors.forEach(error -> {
            Assertions.assertEquals(SOURCE, error.getSource());
            Assertions.assertFalse(error.getRecoverable());
        });
    }

    @Test
    void paymentCardAggregateTransactionsErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "INVALID_REQUEST_PARAMETER",
                        "Payment card id - [b86fd2ba-c095-4acb-b9df-f3805655ba24,d30f6223-b15d-4663-8e6a-247475c596dd ] is/are invalid or not found. Please try again with a valid payment card ID.",
                        false,
                        "")));


        ServiceException serviceException = Assertions.assertThrows(ServiceException.class,
                () -> paymentCardService.getPaymentCardAggregateTransactions(
                        aggregateSearchCriteria("b86fd2ba-c095-4acb-b9df-f3805655ba24")));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(serviceException.getServiceErrors());
        List<Error> errors = serviceException.getServiceErrors().getErrors().getError();
        Assertions.assertFalse(errors.isEmpty());
        errors.forEach(error -> {
            Assertions.assertEquals(SOURCE, error.getSource());
            Assertions.assertFalse(error.getRecoverable());
        });
    }

    @Test
    void supportedCurrenciesErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "'UNSUPPORTED_CURRENCY'",
                        "The currency in the request is not supported, try again with a different one.",
                        false,
                        "")));

        ServiceException serviceException = Assertions.assertThrows(ServiceException.class,
                () -> supportedParametersService.getSupportedCurrencies());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(serviceException.getServiceErrors());
        List<Error> errors = serviceException.getServiceErrors().getErrors().getError();
        Assertions.assertFalse(errors.isEmpty());
        errors.forEach(error -> {
            Assertions.assertEquals(SOURCE, error.getSource());
            Assertions.assertFalse(error.getRecoverable());
        });
    }

    @Test
    void supportedMCCErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "'UNSUPPORTED_MCC'",
                        "The mcc in the request is not supported, try again with a different one.",
                        false,
                        "")));

        ServiceException serviceException = Assertions.assertThrows(ServiceException.class,
                () -> supportedParametersService.getSupportedMerchantCategories());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(serviceException.getServiceErrors());
    }

    @Test
    void deleteCards() throws Exception {
        when(apiClient.execute(any(Call.class))).thenReturn(
                new ApiResponse<>(201, new HashMap<>(),"SUCCESS"));
        final List<String> cardIds = of("9d84e28e-2f5e-4843-87dc-ee0cdf2381d9");

        paymentCardService.deletePaymentCards(cardIds);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

    }

    @Test
    void paymentCardTransactionHistoryErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "'INVALID_DATE_RANGE'",
                        "Requested date range is either invalid or exceeds three-year limits. Try again with a valid date range.",
                        false,
                        "")));
        when(apiClient.escapeString(anyString())).thenReturn("randomString");

        ServiceException serviceException = Assertions.assertThrows(ServiceException.class,
                () -> paymentCardService.getPaymentCardTransactionHistory(paymentCardReference().getPaymentCardId(), "2015-04-29","2013-06-30",0,
                        1));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(serviceException.getServiceErrors());
        List<Error> errors = serviceException.getServiceErrors().getErrors().getError();
        Assertions.assertFalse(errors.isEmpty());
        errors.forEach(error -> {
            Assertions.assertEquals(SOURCE, error.getSource());
            Assertions.assertFalse(error.getRecoverable());
        });
    }
}