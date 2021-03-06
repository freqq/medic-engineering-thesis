package com.pwit.paymentservice.initializer;

import com.pwit.common.utils.Logger;
import com.pwit.paymentservice.properties.PaymentProperties;
import com.stripe.Stripe;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@RequiredArgsConstructor
public class StripeInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = new Logger();

    private final PaymentProperties paymentProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Setting Stripe API key ...");
        setStripeApiKey();
    }

    private void setStripeApiKey() {
        Stripe.apiKey = paymentProperties.getStripeApiKey();
        LOGGER.info("Stripe API key successfully set.");
    }
}
