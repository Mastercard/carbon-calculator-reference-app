package com.mastercard.developers.carboncalculator.service;

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
import org.openapitools.client.api.EngagementServicesApi;
import org.openapitools.client.api.PaymentCardApi;
import org.openapitools.client.model.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.mastercard.developers.carboncalculator.service.MockData.*;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @InjectMocks
    private EnvironmentalImpactService environmentalImpactService;

    @InjectMocks
    private SupportedParametersService supportedParametersService;

    @InjectMocks
    private ServiceProviderService serviceProviderService;

    @InjectMocks
    private ServiceProviderConfig serviceProviderConfig;

    @InjectMocks
    private Profiles profiles;

    @InjectMocks
    private InsightsRequestPayload insightsRequestPayload;

    @InjectMocks
    private InsightsByIdRequestPayload insightsByIdRequestPayload;

    @InjectMocks
    private PaymentCardService paymentCardService;

    @InjectMocks
    private EngagementService engagementService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private PaymentCardApi paymentCardApi;

    @Mock
    private EngagementServicesApi engagementServicesApi;

    private static final String CLIENTID = "cNU2Re-v0oKw95zjfs7G60yICaTtQtyEt-vKZrnjd34ea14e";
    private static final String ORIG_CLIENTID = "wfe232Re-v0oKw95zjfs7G60yICaTtQtyEt-vKZrnjd34ea14e";
    private static final String CHANNEL = "CC";

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
    void calculateCarbonScoreFootprints() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), carbonScoreResponse()));

        CarbonScoreDetails carbonScoreDetails = environmentalImpactService.calculateCarbonScoreFootprints(
                carbonScoreRequest(), CLIENTID, CHANNEL, ORIG_CLIENTID);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(carbonScoreDetails);
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


        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> serviceProviderService.getServiceProvider());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void updateServiceProviderErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "INVALID_REQUEST_PARAMETER",
                        "One of the request parameters is either invalid or is missing, try again with the correct request",
                        false,
                        "supportedAccountRange must match \\\"^[\\\\d\\\\,]{1,}\\\"")));


        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> serviceProviderService.updateServiceProvider(serviceProviderConfig));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void updateProvider() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), MockData.serviceProvider()));

        ServiceProviderConfig serviceProviderConfig1 = new ServiceProviderConfig();
        serviceProviderConfig1.setCustomerName("Customer1");

        ServiceProvider serviceProvider = serviceProviderService.updateServiceProvider(serviceProviderConfig1);

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

        AggregateSearchCriteria aggregateSearchCriteria= new AggregateSearchCriteria();
        aggregateSearchCriteria.setPaymentCardIds(Arrays.asList("testPaymentCardId"));

        AggregateTransactionFootprints aggregateTransactionFootprints = environmentalImpactService.getPaymentCardAggregateTransactions(CLIENTID, aggregateSearchCriteria, CHANNEL, ORIG_CLIENTID);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(aggregateTransactionFootprints);
    }

    @Test
    void aggregateFootprintsErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "INVALID_REQUEST_PARAMETER",
                        "Invalid aggregate type. Please try again with a valid value.",
                        false,
                        "")));

        AggregateSearchCriteria aggregateSearchCriteria= new AggregateSearchCriteria();
        aggregateSearchCriteria.setPaymentCardIds(Arrays.asList("testPaymentCardId"));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> environmentalImpactService.getPaymentCardAggregateTransactions(CLIENTID, aggregateSearchCriteria, CHANNEL, ORIG_CLIENTID));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void calculateFootprintsErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "INVALID_REQUEST_PARAMETER",
                        "One of the request parameters is invalid, try again with correct request.",
                        false,
                        "carbonIndexCalculation.transactions[0].mcc: size must be between 1 and 4")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> environmentalImpactService.calculateFootprints(
                        invalidTransactionRequest()));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void supportedCurrenciesErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "'UNSUPPORTED_CURRENCY'",
                        "The currency in the request is not supported, try again with a different one.",
                        false,
                        "")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> supportedParametersService.getSupportedCurrencies());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void supportedMCCErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(400, new HashMap<>(),
                getErrorResponseBody(
                        "'UNSUPPORTED_MCC'",
                        "The mcc in the request is not supported, try again with a different one.",
                        false,
                        "")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> supportedParametersService.getSupportedMerchantCategories());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void oldDeleteCards() throws Exception {
        when(apiClient.execute(any(Call.class))).thenReturn(
                new ApiResponse<>(201, new HashMap<>(),"SUCCESS"));
        final List<String> cardIds = of("9d84e28e-2f5e-4843-87dc-ee0cdf2381d9");

        paymentCardService.deletePaymentCards(cardIds);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

    }

    @Test
    void deleteCards() throws Exception {
        when(apiClient.escapeString(anyString())).thenReturn("randomString");
        when(apiClient.execute(any(Call.class))).thenReturn(
                new ApiResponse<>(201, new HashMap<>(),"SUCCESS"));

        paymentCardService.deletePaymentCard("9d84e28e-2f5e-4843-87dc-ee0cdf2381d9", CLIENTID, CHANNEL, ORIG_CLIENTID);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

    }

    @Test
    void deleteCardErrorScenario() throws Exception {

        when(apiClient.execute(any(Call.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "'INVALID_DATE_RANGE'",
                        "Requested date range is either invalid or exceeds three-year limits. Try again with a valid date range.",
                        false,
                        "")));
        when(apiClient.escapeString(anyString())).thenReturn("randomString");

        Assertions.assertThrows(ApiException.class,
                () -> paymentCardService.deletePaymentCard("9d84e28e-2f5e-4843-87dc-ee0cdf2381d9", CLIENTID, CHANNEL, ORIG_CLIENTID));

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

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> paymentCardService.getPaymentCardTransactionHistory(paymentCardReference().getPaymentCardId(), "2015-04-29","2013-06-30",0,
                        1));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void getSurveys() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), surveys()));
        Surveys surveys = engagementService.getSurveys();

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(surveys);
    }
    @Test
    void getSurveysErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "'ACCOUNT_NOT_FOUND'",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engagementService.getSurveys());

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }
    @Test
    void updateUserProfile() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), MockData.getMockProfilesResponse()));

        Profiles profiles1 = new Profiles();

        Profile profile = engagementService.updateUserProfile(profiles1);

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(profile);
    }
    @Test
    void updateUserProfileErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));


        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engagementService.updateUserProfile(profiles));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void updateUserInsights() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), MockData.getMockInsightsResponse()));
        InsightsRequestPayload insightsRequestPayload1 = new InsightsRequestPayload();

        InsightsData insightsData = engagementService.updateUserInsights(insightsRequestPayload1,"branding",false,"20","2","1.1","en");

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(insightsData);
    }
    @Test
    void updateUserInsightsErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));


        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engagementService.updateUserInsights(insightsRequestPayload,"",false,"1","mvn","",""));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void getBenchmarksForCountry() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), new Benchmark()));


        Benchmark benchmark = engagementService.getBenchmarksForCountry("USA", "2023");

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(benchmark);
    }

    @Test
    void getBenchmarksForCountryErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));


        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engagementService.getBenchmarksForCountry("ABCD", "2020"));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }
    @Test
    void insightsByIdSuccess() throws Exception {
        when(apiClient.escapeString(anyString())).thenReturn("randomString");
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), MockData.getMockInsightsByIdResponse()));


        InsightsResponseById insightsResponseById   = engagementService.getInsightsById("T101",insightsByIdRequestPayload,"branding","en","1.1");

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(insightsResponseById);
    }
    @Test
    void insightsByIdErrorScenario() throws Exception {
        when(apiClient.escapeString(anyString())).thenReturn("randomString");
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engagementService.getInsightsById("T101",insightsByIdRequestPayload,"","",""));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }
    @Test
    void getPersonas_Success() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), new Personas()));  // Adjust as necessary for Personas

        Personas personas = engagementService.getPersonas("branding", "en", "20", "1");

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(personas);
    }
    @Test
    void getPersonas_ErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engagementService.getPersonas("branding", "en", "20", "1"));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }
    @Test
    void getComparisons_Success() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), new Comparison()));

        Comparison comparison = engagementService.getComparisons("branding", "en", "1.0", "category1", "spend1", "1");

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(comparison);
    }
    @Test
    void getComparisons_ErrorScenario() throws Exception {
        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engagementService.getComparisons("branding", "en", "1.0", "category1", "spend1", "1"));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }

    @Test
    void addProfileToPaymentCard_Success() throws Exception {
        when(apiClient.escapeString(anyString())).thenReturn("randomString");

        when(apiClient.execute(any(Call.class), any(Type.class))).thenReturn(
                new ApiResponse<>(200, new HashMap<>(), new PaymentCardProfile()));

        PaymentCardProfile paymentCardProfile = environmentalImpactService.addProfileToPaymentCard("8cd77c74-ca32-42f9-83fc-85ca2dfabe96", MockData.getMockPaymentCardProfilesRequest());
        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        assertNotNull(paymentCardProfile);

    }

    @Test
    void addProfileToPaymentCard_ErrorScenario() throws Exception {
        when(apiClient.escapeString(anyString())).thenReturn("randomString");

        when(apiClient.execute(any(Call.class), any(Type.class))).thenThrow(new ApiException(404, new HashMap<>(),
                getErrorResponseBody(
                        "ACCOUNT_NOT_FOUND",
                        "We cannot find the account which you are using to access this service. Kindly register your account or contact your Mastercard associate if you have already registered with us earlier.",
                        false,
                        "")));

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> environmentalImpactService.addProfileToPaymentCard("73c0711e-1851-4771-950a-055dded7f162", getMockPaymentCardProfilesRequest()));

        verify(apiClient, atMostOnce()).buildCall(anyString(), anyString(), anyList(), anyList(), any(), anyMap(),
                anyMap(), anyMap(), any(), any());
        verify(apiClient, atMostOnce()).execute(any(Call.class), any(Type.class));

        Assertions.assertNotNull(apiException.getResponseBody());
    }
}