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
    private PaymentCardService paymentCardService;

    @Mock
    private ApiClient apiClient;


    @BeforeEach
    void setUp() throws Exception {
        when(apiClient.buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(), anyMap(), anyMap(),
                                 any(), any())).thenReturn(mock(Call.class));
    }

    @Test
    void calculateFootprints() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), transactionFootprints()));

        List<TransactionFootprint> transactionFootprints = environmentalImpactService.calculateFootprints(
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
    void deleteCards() throws Exception {
        when(apiClient.execute(any(Call.class))).thenReturn(
                new ApiResponse<>(201, new HashMap<>(),"SUCCESS"));
        final List<String> cardIds = of("9d84e28e-2f5e-4843-87dc-ee0cdf2381d9");

        paymentCardService.deletePaymentCards(cardIds);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

    }
}