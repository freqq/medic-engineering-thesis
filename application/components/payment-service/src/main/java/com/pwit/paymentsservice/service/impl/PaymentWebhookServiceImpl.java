package com.pwit.paymentsservice.service.impl;

import com.pwit.common.utils.Logger;
import com.pwit.paymentsservice.entity.SessionEntity;
import com.pwit.paymentsservice.repository.SessionRepository;
import com.pwit.paymentsservice.service.PaymentHistoryService;
import com.pwit.paymentsservice.service.PaymentWebhookService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentWebhookServiceImpl implements PaymentWebhookService {
    private static final Logger LOGGER = new Logger();

    private final SessionRepository sessionRepository;
    private final PaymentHistoryService paymentHistoryService;

    @Override
    public ResponseEntity<?> handleCheckoutSessionEvent(Event event) {
        if (event.getType().equals("checkout.session.completed")) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();

            if (session.getPaymentStatus().equals("paid")) {
                fulfillOrder(session);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.badRequest().build();
    }

    private void fulfillOrder(Session session) {
        if (!sessionRepository.existsByStripeIdAndHandledTrue(session.getId())) {

            SessionEntity sessionEntity = new SessionEntity().toBuilder()
                    .isSuccessful(true)
                    .isHandled(false)
                    .receivedAt(Instant.now())
                    .handledAt(null)
                    .stripeId(session.getId())
                    .clientReferenceId(session.getClientReferenceId())
                    .amountTotal(session.getAmountTotal())
                    .customer(session.getCustomer())
                    .customerEmail(session.getCustomerEmail())
                    .build();

            SessionEntity savedSession = sessionRepository.save(sessionEntity);
            paymentHistoryService.handlePaymentRecord(session);

            savedSession.setHandled(true);
            savedSession.setHandledAt(Instant.now());

            sessionRepository.save(sessionEntity);

            LOGGER.info("Payment succeded.");
        }
    }
}
