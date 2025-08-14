package com.techshare.services.stripe.impl;

import org.springframework.stereotype.Service;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.techshare.https.response.PaymentIntentDTO;
import com.techshare.services.stripe.StripeService;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeServiceImpl implements StripeService {

    @Override
    public PaymentIntentDTO createPaymentIntent(Double amount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount((long) (amount * 100)) // Stripe uses cents
            .setCurrency("usd")
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
            )
            .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        
        PaymentIntentDTO paymentIntentDTO = new PaymentIntentDTO();
        paymentIntentDTO.setClientSecret(paymentIntent.getClientSecret());
        paymentIntentDTO.setPaymentIntentId(paymentIntent.getId());
        paymentIntentDTO.setStatus(paymentIntent.getStatus());
        
        return paymentIntentDTO;
    }

    @Override
    public PaymentIntentDTO confirmPayment(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa"); // For testing purposes
        
        paymentIntent = paymentIntent.confirm(params);
        
        PaymentIntentDTO paymentIntentDTO = new PaymentIntentDTO();
        paymentIntentDTO.setClientSecret(paymentIntent.getClientSecret());
        paymentIntentDTO.setPaymentIntentId(paymentIntent.getId());
        paymentIntentDTO.setStatus(paymentIntent.getStatus());
        
        return paymentIntentDTO;
    }
} 