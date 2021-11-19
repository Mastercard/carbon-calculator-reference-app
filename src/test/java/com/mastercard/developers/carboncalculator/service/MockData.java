package com.mastercard.developers.carboncalculator.service;

import org.openapitools.client.model.Error;
import org.openapitools.client.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mastercard.developers.carboncalculator.util.JSON.serializeErrors;

public class MockData {

    private static final String SOURCE = "Carbon-Calculator";

    public static List<Transaction> transactions() {
        List<Transaction> mcTransactions = new ArrayList<>();
        mcTransactions.add(new Transaction().transactionId("TX-1")
                                   .mcc("3000").amount(
                        new Amount().currencyCode("EUR").value(new BigDecimal(150))));
        return mcTransactions;
    }

    public static List<TransactionFootprint> transactionFootprints() {

        TransactionFootprint transactionFootprint = new TransactionFootprint().transactionId(
                "TX-1").category(new Category().mainCategory("Transportation").subCategory(
                "Flights").sector(
                "Airlines").sectorCode(
                "505")).mcc("3000").carbonEmissionInGrams(BigDecimal.valueOf(205688.73)).carbonEmissionInOunces(
                BigDecimal.valueOf(7255.46)).carbonSocialCost(
                null).waterUseInCubicMeters(BigDecimal.valueOf(1.5)).waterUseInGallons(
                BigDecimal.valueOf(396.2)).waterUseSocialCost(null);

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
                "customerId").customerName("customerName").status("ACTIVE").supportedAccountRange("5425").callbackUrl(
                "http://www.callbackUrl.com/callback");
    }

    public static AggregateSearchCriteria aggregateSearchCriteria(String paymentCardId) {

        //Test with different Aggregate type, supported values are as follows:
        // 0=daily
        // 1=weekly
        // 2=monthly
        // 3=yearly
        List<String> paymentCardIds = Collections.singletonList(paymentCardId);
        return new AggregateSearchCriteria().paymentCardIds(paymentCardIds).aggregateType(0);
    }

    public static List<AggregateTransactionFootprint> aggregateTransactionFootprint() {

        FootprintAggregation footprintAggregation = new FootprintAggregation().aggregateValue("2020");
        footprintAggregation.carbonEmissionInGrams(BigDecimal.valueOf(205688.73)).carbonEmissionInOunces(
                BigDecimal.valueOf(7255.46)).carbonSocialCost(
                null).waterUseInCubicMeters(BigDecimal.valueOf(1.5)).waterUseInGallons(
                BigDecimal.valueOf(396.2)).waterUseSocialCost(null);

        AggregateTransactionFootprint aggregateTransactionFootprint = new AggregateTransactionFootprint();
        aggregateTransactionFootprint.paymentCardId("testPaymentCardId").addFootprintAggregationsItem(
                footprintAggregation);

        return Collections.singletonList(aggregateTransactionFootprint);
    }

    public static HistoricalTransactionFootprints historicalTransactionFootprint() {

        HistoricalTransactionFootprint historicalTransactionFootprint = new HistoricalTransactionFootprint().transactionFootprint(
                transactionFootprints().get(0)).transactionMetadata(
                new TransactionMetadata().amount(150.0).currencyCode("USD").indicator("RFT").retrievalRefNumber(
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

    public static List<Transaction> invalidTransactionRequest() {
        List<Transaction> mcTransactions = new ArrayList<>();
        mcTransactions.add(new Transaction().transactionId("TX-1")
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
}
