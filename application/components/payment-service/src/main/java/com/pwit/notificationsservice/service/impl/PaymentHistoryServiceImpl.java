package com.pwit.notificationsservice.service.impl;

import com.pwit.common.utils.Logger;
import com.pwit.notificationsservice.dto.PaymentResponse;
import com.pwit.notificationsservice.entity.Payment;
import com.pwit.notificationsservice.repository.PaymentRepository;
import com.pwit.notificationsservice.service.PaymentHistoryService;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.pwit.notificationsservice.service.util.PaymentMetada.APPOINTMENT_ID;
import static com.pwit.notificationsservice.service.util.PaymentMetada.PATIENT_ID;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
    private static final Logger LOGGER = new Logger();

    private final PaymentRepository paymentRepository;

    @Override
    public void handlePaymentRecord(Session session) {
        String appointmentId = session.getMetadata().get(APPOINTMENT_ID);
        String patientId = session.getMetadata().get(PATIENT_ID);

        Payment payment = new Payment()
                .toBuilder()
                .paymentMethod(session.getPaymentMethodTypes().get(0))
                .paidAt(Instant.now())
                .appointmentId(appointmentId)
                .price(session.getAmountTotal())
                .patientId(patientId)
                .build();

        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentResponse> getUserPaymentHistory(String currentUserId) {
        List<PaymentResponse> paymentResponses = new ArrayList<>();
        paymentRepository.findAllByPatientId(currentUserId).forEach(payment -> paymentResponses.add(createPaymentResponse(payment)));
        return paymentResponses;
    }

    private PaymentResponse createPaymentResponse(Payment payment) {
        return new PaymentResponse()
                .toBuilder()
                .paidAt(payment.getPaidAt())
                .paymentMethod(payment.getPaymentMethod())
                .price(payment.getPrice())
                .build();
    }
}