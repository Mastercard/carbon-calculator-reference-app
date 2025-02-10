package com.mastercard.developers.carboncalculator.service;

import org.openapitools.client.model.Currency;
import org.openapitools.client.model.Error;
import org.openapitools.client.model.*;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static com.mastercard.developers.carboncalculator.util.JSON.serializeErrors;

public class MockData {

    private static final String SOURCE = "Carbon-Calculator";

    public static List<TransactionData> transactions() {
//        List<TransactionData> mcTransactions = new ArrayList<>();
        TransactionData transactionFootprint = (TransactionData) new TransactionData().transactionId("TX-1")
                .mcc("3000").amount(
                        new Amount().currencyCode("EUR").value(new BigDecimal(150)));
        return Collections.singletonList(transactionFootprint);
    }

    public static ScoreRequestDetails carbonScoreRequest() {
        ScoreRequestDetails scoreRequestDetails = new ScoreRequestDetails();
        TransactionDetails transactionDetails  = new TransactionDetails();
        transactionDetails.mcc("3000").id("DVsJNvdSMX").amount(
                new Amount().currencyCode("USD").value(new BigDecimal(150)));
        scoreRequestDetails.addTransactionsItem(transactionDetails);
        return scoreRequestDetails;
    }

    public static CarbonScoreDetails carbonScoreResponse(){
        CarbonScoreDetails scoreResponse = new CarbonScoreDetails();

        List<TransactionFootprintDetails> transactionDetailList = new ArrayList<>();
        TransactionFootprintDetails carbonScore = new TransactionFootprintDetails();
        carbonScore.setCarbonEmissionInGrams(new BigDecimal(2582.11));
        carbonScore.setCarbonEmissionInOunces(new BigDecimal(91.08));
        carbonScore.setId("1");
        carbonScore.setMcc("3000");
        carbonScore.setCardBrand("MA");
        carbonScore.setScoreStatus("SUCCESS");
        carbonScore.setScoreReference("MCC");
        transactionDetailList.add(carbonScore);
        scoreResponse.setTransactionFootprints(transactionDetailList);
        return scoreResponse;
    }
    
	public static List<TransactionData> aiiaBasedRequest() {
		List<TransactionData> mcTransactions = new ArrayList<>();
		mcTransactions.add(
                (TransactionData) new TransactionData().type("AIIA").transactionId("TX-1").additionalInformation(getAiiaAdditionalInfo())
                        .mcc("3000").amount(new Amount().currencyCode("EUR").value(new BigDecimal(150))));
		return mcTransactions;
	}

    public static List<TransactionFootprintData> transactionFootprints() {

        TransactionFootprintData transactionFootprint = (TransactionFootprintData) new TransactionFootprintData().transactionId(
                "TX-1").category(new Category().mainCategory("Transportation").subCategory(
                "Flights").sector(
                "Airlines").sectorCode(
                "505")).mcc("3000").scoreReference("MCC").carbonEmissionInGrams(BigDecimal.valueOf(205688.73)).carbonEmissionInOunces(
                BigDecimal.valueOf(7255.46));

        return Collections.singletonList(transactionFootprint);

    }
    
	public static List<TransactionFootprintData> aiiaBasedTransactionFootprints() {

		TransactionFootprintData transactionFootprint = (TransactionFootprintData) new TransactionFootprintData().transactionId("TX-1")
				.category(new Category().mainCategory("Transportation").subCategory("Flights").sector("Airlines")
						.sectorCode("505"))
				.scoreReference("AIIA").mcc("3000").carbonEmissionInGrams(BigDecimal.valueOf(205688.73))
				.carbonEmissionInOunces(BigDecimal.valueOf(7255.46));

		return Collections.singletonList(transactionFootprint);

	}

    public static List<TransactionFootprint> historicalTransactionFootprints() {

        TransactionFootprint transactionFootprint = new TransactionFootprint().transactionId(
                "TX-1").category(new Category().mainCategory("Transportation").subCategory(
                "Flights").sector(
                "Airlines").sectorCode(
                "505")).mcc("3000").carbonEmissionInGrams(BigDecimal.valueOf(205688.73)).carbonEmissionInOunces(
                BigDecimal.valueOf(7255.46));

        return Collections.singletonList(transactionFootprint);

    }

    public static List<MerchantCategory> merchantCategories() {

        MerchantCategory category = new MerchantCategory();
        category.mcc("3000").category(
                new Category().mainCategory("Transportation").subCategory(
                        "Flights").sector(
                        "Airlines").sectorCode("505"));
        return Collections.singletonList(category);

    }


    public static List<Currency> currencies() {
        Currency mcCurrency = new Currency().currencyCode("USD");
        return Collections.singletonList(mcCurrency);
    }

    public static ServiceProvider serviceProvider() {
        return new ServiceProvider().clientId("clientId").customerId(
                "customerId").customerName("customerName").status("ACTIVE").supportedAccountRange("5425");
    }

    public static AggregateSearchCriteria aggregateSearchCriteria(String paymentCardId) {

        //Test with different Aggregate type, supported values are as follows:
        // 1=weekly
        // 2=monthly
        // 3=monthly with category wise
        List<String> paymentCardIds = Collections.singletonList(paymentCardId);
        return new AggregateSearchCriteria().paymentCardIds(paymentCardIds).aggregateType(0);
    }

    public static AggregateTransactionFootprints aggregateTransactionFootprint() {

        FootprintAggregation footprintAggregation = new FootprintAggregation().aggregateValue("2");
        footprintAggregation.carbonEmissionInGrams(BigDecimal.valueOf(205688.73)).carbonEmissionInOunces(
                BigDecimal.valueOf(7255.46));

        AggregateTransactionFootprints aggregateTransactionFootprints= new AggregateTransactionFootprints();
        List<AggregateTransactionFootprint> listAggregateTransactionFootprint = new ArrayList<>();
        AggregateTransactionFootprint aggregateTransactionFootprint = new AggregateTransactionFootprint();
        aggregateTransactionFootprint.paymentCardId("testPaymentCardId").addFootprintAggregationsItem(
                footprintAggregation);
        listAggregateTransactionFootprint.add(aggregateTransactionFootprint);
        aggregateTransactionFootprints.setAggregateTransactionFootprint(listAggregateTransactionFootprint);
        return aggregateTransactionFootprints;
    }

    public static HistoricalTransactionFootprints historicalTransactionFootprint() {

        HistoricalTransactionFootprint historicalTransactionFootprint = new HistoricalTransactionFootprint().transactionFootprint(
                historicalTransactionFootprints().get(0)).transactionMetadata(
                new TransactionMetadata().amount(BigDecimal.valueOf(150.0)).currencyCode("USD").indicator("RFT").retrievalRefNumber(
                        "83Y071x35").processingCode("16"));

        return new HistoricalTransactionFootprints().count(1).limit(1).offset(0).total(1).addItemsItem(
                historicalTransactionFootprint);
    }

    public static PaymentCard paymentCard() {
        return new PaymentCard().fpan("5425390771995306").cardBaseCurrency("USD");
    }

    public static PaymentCardReference paymentCardReference() {
        return new PaymentCardReference().bin("5425").last4fpan("5306").paymentCardId(
                "paymentCardId").status("ACTIVE");
    }

    public static List<TransactionData> invalidTransactionRequest() {
        List<TransactionData> mcTransactions = new ArrayList<>();
        mcTransactions.add((TransactionData) new TransactionData().transactionId("TX-1")
                .mcc("12345").amount(
                        new Amount().currencyCode("EUR").value(new BigDecimal(150))));
        return mcTransactions;
    }

    public static String getErrorResponseBody(String reasonCode, String description, boolean recoverable, String details) {
        Error error = new Error().source(SOURCE).reasonCode(reasonCode).description(description).recoverable(
                recoverable).details(details);
        ErrorWrapper response = new ErrorWrapper().errors(new Errors().addErrorItem(error));
        return serializeErrors(response);
    }

    public static List<String> listPaymentCards(){
        List<String> paymentCards = new ArrayList<>();
        paymentCards.add("c5d88571-ac15-465a-a0d8-1ad1327b9a06");
        return paymentCards;
    }

    public static List<PaymentCardEnrolment> batchPaymentEnrollment() {

        List <PaymentCardEnrolment> paymentCardEnrolment = new ArrayList<>();
        paymentCardEnrolment.add(new PaymentCardEnrolment().bin("5425").last4fpan("5306").paymentCardId(
                "paymentCardId").status("ACTIVE"));
        return paymentCardEnrolment;
    }

    public static List<PaymentCard> listPaymentCardReference() {
        List<PaymentCard> paymentCards = new ArrayList<>();
        paymentCards.add(new PaymentCard().fpan("5344035171229750").cardBaseCurrency("EUR"));
        return paymentCards;
    }
    


	public static List<AdditionalInformation> getAiiaAdditionalInfo(){
    	List<AdditionalInformation> addInfo = new ArrayList<>();
    	AdditionalInformation info = new AdditionalInformation();
    	info.setKey("aiiaCode");
    	info.setValue("115");
    	addInfo.add(info);
    	return addInfo;
    }

    public static Surveys surveys() {
        Surveys mockSurveyResponse = new Surveys();
        mockSurveyResponse.setId("mockId");
        mockSurveyResponse.setLanguage("English");
        mockSurveyResponse.setType("mockType");
        SurveyJs surveyJs = new SurveyJs();
        surveyJs.setShowProgressBar("showBar");
        surveyJs.setShowQuestionNumbers("show Questions");

        Page page = new Page();
        page.setName("Mock Page");
        page.setQuestions(List.of(new Question()));
        surveyJs.setPages(List.of(page));
        mockSurveyResponse.setSurvey(surveyJs);
        return mockSurveyResponse;
    }

    public static Profiles getMockProfilesRequest() {
        ProfilesSurveyioResults surveyioResults = new ProfilesSurveyioResults();

        surveyioResults.setSex("demo.f");
        surveyioResults.setAge("12");
        surveyioResults.setCountry("AX");
        surveyioResults.setInterest("stronglyDisagree");
        surveyioResults.setKnowledge("stronglyDisagree");
        surveyioResults.setDiet("vegan");
        surveyioResults.setRenewableElectricity("energy.idk");
        surveyioResults.setHaveCar("Yes");
        surveyioResults.setFlyFrequency("Yes");
        surveyioResults.setShopFrequency("often");
        surveyioResults.setDifference("disagree");
        surveyioResults.setConsciousPurchasingDecision("disagree");
        surveyioResults.setExplainCarbonFootprint("stronglyAgree");
        Profiles profiles = new Profiles();
        profiles.setSurveyioResults(surveyioResults);
        return profiles;
    }


    public static Profile getMockProfilesResponse() {
        ClimateProfile climateProfile = new ClimateProfile();
        ClimateProfileUserAttributes userAttributes = new ClimateProfileUserAttributes();
        userAttributes.setCountry("AX");
        userAttributes.setDemo(new BigDecimal("34"));
        userAttributes.setEnergy(new BigDecimal("34"));
        userAttributes.setDiet(new BigDecimal("34"));
        userAttributes.setTransport(new BigDecimal("34"));
        userAttributes.setLifestyle(new BigDecimal("34"));

        climateProfile.setUserAttributes(userAttributes);
        climateProfile.setBenchmarks(Map.of("Benchmark", "Test"));
        climateProfile.setCreated(OffsetDateTime.parse("2024-05-04T18:45:35.943Z"));
        climateProfile.setTraits(new ClimateProfileTraits());
        climateProfile.setPersona("2");
        climateProfile.setSurveys(new HashMap<String, Object>());

        Profile response = new Profile();
        response.setProfile(climateProfile);
        return response;
    }
    public static CardClimateProfile getMockPaymentCardProfilesRequest() {
        CardClimateProfileSurveyioResults surveyioResults = new CardClimateProfileSurveyioResults();

        surveyioResults.setSex("demo.f");
        surveyioResults.setAge("12");
        surveyioResults.setCountry("AX");
        surveyioResults.setInterest("stronglyDisagree");
        surveyioResults.setKnowledge("stronglyDisagree");
        surveyioResults.setDiet("vegan");
        surveyioResults.setRenewableElectricity("energy.idk");
        surveyioResults.setHaveCar("Yes");
        surveyioResults.setFlyFrequency("Yes");
        surveyioResults.setShopFrequency("often");
        surveyioResults.setDifference("disagree");
        surveyioResults.setConsciousPurchasingDecision("disagree");
        surveyioResults.setExplainCarbonFootprint("stronglyAgree");
        CardClimateProfile profiles = new CardClimateProfile();
        profiles.setSurveyioResults(surveyioResults);
        return profiles;
    }

    public static InsightsRequestPayload getMockInsightsRequest() {
        InsightsRequestPayload insightsRequestPayload = new InsightsRequestPayload();
        insightsRequestPayload.setDocc("");
        insightsRequestPayload.setMain("shopping");
        insightsRequestPayload.setMainCategory("shopping");
        insightsRequestPayload.setSubCategory("clothes");
        insightsRequestPayload.setSpendingAreaId("10");
        ClimateProfile climateProfile = new ClimateProfile();
        climateProfile.setCreated(OffsetDateTime.parse("2024-09-11T17:49:25.025125100Z"));
        climateProfile.setTraits(new ClimateProfileTraits().knowledge(BigDecimal.ONE).lifestyle(BigDecimal.ONE)
                .motivated(BigDecimal.ONE));
        climateProfile.setPersona("2");
        ClimateProfileUserAttributes attributes = new ClimateProfileUserAttributes().country("IN").demo(BigDecimal.ONE)
                .diet(BigDecimal.ONE).energy(BigDecimal.ONE).lifestyle(BigDecimal.ONE).transport(BigDecimal.ONE);
        climateProfile.setUserAttributes(attributes);
        insightsRequestPayload.setProfile(climateProfile);
        return insightsRequestPayload;
    }

    public static InsightsResponseById getMockInsightsByIdResponse() {
        InsightsResponseById insightsResponseById = new InsightsResponseById();
        insightsResponseById.setId("T101");

        insightsResponseById.setLanguage("en");
        insightsResponseById.setHeading("Recycle your e-waste");
        insightsResponseById.setContent("Voluptate eiusmod culpa incididunt excepteur anim ipsum et ex " +
                "elit laboris esse. Nostrud proident culpa exercitation eu dolore consectetur sit amet cillum veniam enim duis ut. " +
                "Qui mollit fugiat tempor deserunt occaecat fugiat adipisicing ex cupidatat duis commodo id ea. Dolor laboris eu commodo " +
                "ea reprehenderit in nisi proident. Fugiat cupidatat excepteur et ullamco anim. Cillum fugiat commodo ipsum consequat irure proident tempor nisi tempor ad." +
                "\nOccaecat est pariatur cillum commodo incididunt exercitation laboris consectetur eu mollit incididunt. " +
                "Adipisicing quis incididunt mollit esse officia dolore fugiat reprehenderit nisi do velit ea. Id cillum consectetur mollit eu laborum nostrud nulla sint Lorem voluptate elit id.");
        insightsResponseById.setNotification("");
        insightsResponseById.setMainCategory("home");
        insightsResponseById.setSubCategory("electronics");
        insightsResponseById.setSpendingAreaId("10");
        insightsResponseById.setSource("The Global E-waste Monitor 2020");
        insightsResponseById.setSourceDescription("The Global E-waste Monitor 2020");
        insightsResponseById.setSourceLink("https://ewastemonitor.info/wp-content/uploads/2020/11/GEM_2020_def_july1_low.pdf");
        insightsResponseById.setSourceDate("2020-07-01");

        return insightsResponseById;
    }
    public static LinkedMultiValueMap<Object, String> prepareRequestParams() {
        LinkedMultiValueMap<Object, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("from_date", "2020-09-19");
        requestParams.add("to_date", "2020-10-01");
        requestParams.add("offset", "0");
        requestParams.add("limit", "1");
        return requestParams;
    }

    public static List<InsightItem> getInsightItems() {
        InsightItem insightItem = new InsightItem();
        insightItem.setId("T100");
        insightItem.setSeen(false);
        insightItem.setMainCategory("shopping");
        insightItem.setSpendingAreaId("30");
        insightItem.setSubCategory("electricUtilities");
        InsightItem insightItem1 = new InsightItem();
        insightItem1.setId("T101");

        insightItem1.setSeen(false);
        insightItem1.setMainCategory("home");
        insightItem1.setSpendingAreaId("10");
        insightItem1.setSubCategory("electronics");
        InsightItem insightItem2 = new InsightItem();
        insightItem2.setId("T102");
        insightItem2.setSeen(false);
        insightItem2.setMainCategory("home");
        insightItem2.setSpendingAreaId("10");
        insightItem2.setSubCategory("electronics");

        List<InsightItem> listItem = new ArrayList<>();
        listItem.add(insightItem);
        listItem.add(insightItem1);
        listItem.add(insightItem2);
        return listItem;
    }

    public static InsightsData getMockInsightsResponse() {
        InsightsData insightsData = new InsightsData();
        List<InsightItem> listItem = getInsightItems();
        insightsData.setTotal(100);
        insightsData.setLanguage("es");

        ClimateProfile profileResponse = new ClimateProfile();

        ClimateProfileTraits traits = new ClimateProfileTraits();
        traits.setKnowledge(BigDecimal.valueOf(0.75));
        traits.setLifestyle(BigDecimal.valueOf(0.246));
        traits.setMotivated(BigDecimal.valueOf(0.675));

        ClimateProfileUserAttributes userAttributes = new ClimateProfileUserAttributes();
        userAttributes.setDemo(BigDecimal.valueOf(257));
        userAttributes.setDiet(BigDecimal.valueOf(1024));
        userAttributes.setLifestyle(BigDecimal.valueOf(2));
        userAttributes.setEnergy(BigDecimal.valueOf(0));
        userAttributes.setTransport(BigDecimal.valueOf(20517));
        userAttributes.setCountry("US");
        profileResponse.setTraits(traits);
        profileResponse.setUserAttributes(userAttributes);
        profileResponse.setCreated(OffsetDateTime.parse("2024-03-27T09:25:27.539Z"));
        profileResponse.setPersona("2");
        profileResponse.setSurveys(null);

        insightsData.setInsights(listItem);
        insightsData.setProfile(profileResponse);
        return insightsData;
    }
    public static PaymentCardProfile getMockPaymentCardProfilesResponse() {
        PaymentCardProfile paymentCardProfile = new PaymentCardProfile();
        org.openapitools.client.model.ClimateProfile climateProfile = new org.openapitools.client.model.ClimateProfile();
        ClimateProfileUserAttributes userAttributes = new ClimateProfileUserAttributes();
        userAttributes.setCountry("AX");
        userAttributes.setDemo(new BigDecimal("34"));
        userAttributes.setEnergy(new BigDecimal("34"));
        userAttributes.setDiet(new BigDecimal("34"));
        userAttributes.setTransport(new BigDecimal("34"));
        userAttributes.setLifestyle(new BigDecimal("34"));

        climateProfile.setUserAttributes(userAttributes);
        climateProfile.setBenchmarks(Map.of("Benchmark", "Test"));
        climateProfile.setCreated(OffsetDateTime.parse("2024-05-04T18:45:35.943Z"));
        climateProfile.setTraits(new ClimateProfileTraits());
        climateProfile.setPersona("2");
        climateProfile.setSurveys(new HashMap<String, Object>());

        paymentCardProfile.setPaymentCardId("73c0711e-1851-4771-950a-055dded7f162");
        paymentCardProfile.setProfile(climateProfile);
        return paymentCardProfile;
    }
}
