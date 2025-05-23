package com.techshare.service.stripe;

import com.stripe.exception.StripeException;
import com.techshare.DTO.PaymentIntentDTO;

public interface StripeService {
    PaymentIntentDTO createPaymentIntent(Double amount) throws StripeException;
    PaymentIntentDTO confirmPayment(String paymentIntentId) throws StripeException;
} 