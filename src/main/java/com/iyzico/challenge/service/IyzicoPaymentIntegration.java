package com.iyzico.challenge.service;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class IyzicoPaymentIntegration {

    @Value("${iyzico.api-key}")
    private String apiKey;
    @Value("${iyzico.secret-key}")
    private String secretKey;
    @Value("${iyzico.base-url}")
    private String baseUrl;
    public Payment pay(){
        CreatePaymentRequest request = getCreatePaymentRequest();

        request.setPaymentCard(getPaymentCard(request));
        request.setBuyer(getBuyer(request));
        request.setShippingAddress(getAddress(request));
        request.setBillingAddress(getBillingAddress(request));
        request.setBasketItems(getBasketItems());

        Payment payment = Payment.create(request, this.connect());

        return payment;
    }

    private static List<BasketItem> getBasketItems() {
        List<BasketItem> basketItems = new ArrayList<>();
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("BI101");
        firstBasketItem.setName("Binocular");
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setCategory2("Accessories");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        firstBasketItem.setPrice(new BigDecimal("0.3"));
        basketItems.add(firstBasketItem);
        return basketItems;
    }

    private static Address getBillingAddress(CreatePaymentRequest request) {
        Address billingAddress = new Address();
        billingAddress.setContactName("Jane Doe");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");
        request.setBillingAddress(billingAddress);
        return billingAddress;
    }

    private static Address getAddress(CreatePaymentRequest request) {
        Address shippingAddress = new Address();
        shippingAddress.setContactName("Jane Doe");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        shippingAddress.setZipCode("34742");
        request.setShippingAddress(shippingAddress);
        return shippingAddress;
    }

    private static Buyer getBuyer(CreatePaymentRequest request) {
        Buyer buyer = new Buyer();
        buyer.setId("BY789");
        buyer.setName("John");
        buyer.setSurname("Doe");
        buyer.setGsmNumber("+905350000000");
        buyer.setEmail("email@email.com");
        buyer.setIdentityNumber("74300864791");
        buyer.setLastLoginDate("2015-10-05 12:43:35");
        buyer.setRegistrationDate("2013-04-21 15:12:09");
        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        buyer.setIp("85.34.78.112");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34732");
        request.setBuyer(buyer);
        return buyer;
    }

    private static PaymentCard getPaymentCard(CreatePaymentRequest request) {
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName("John Doe");
        paymentCard.setCardNumber("5528790000000008");
        paymentCard.setExpireMonth("12");
        paymentCard.setExpireYear("2030");
        paymentCard.setCvc("123");
        paymentCard.setRegisterCard(0);
        request.setPaymentCard(paymentCard);
        return paymentCard;
    }

    private static CreatePaymentRequest getCreatePaymentRequest() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId("123456789");
        request.setPrice(new BigDecimal("1"));
        request.setPaidPrice(new BigDecimal("1.2"));
        request.setCurrency(Currency.TRY.name());
        request.setInstallment(1);
        request.setBasketId("B67832");
        request.setPaymentChannel(PaymentChannel.WEB.name());
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());
        return request;
    }

    private Options connect(){
        Options options = new Options();
        options.setApiKey(apiKey);
        options.setBaseUrl(baseUrl);
        options.setSecretKey(secretKey);
        return options;
    }
}
