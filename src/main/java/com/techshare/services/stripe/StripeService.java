package com.techshare.services.stripe;

import com.stripe.exception.StripeException;
import com.techshare.dto.PaymentIntentDTO;
 
public interface StripeService {
    PaymentIntentDTO createPaymentIntent(Double amount) throws StripeException;
    PaymentIntentDTO confirmPayment(String paymentIntentId) throws StripeException;
} 