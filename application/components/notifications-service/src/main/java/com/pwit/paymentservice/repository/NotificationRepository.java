package com.pwit.paymentservice.repository;

import com.pwit.paymentservice.dto.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByIdNotNullAndReceiverIdEqualsOrderByDateTimeDesc(String receiverId);
    Long countAllByIdNotNullAndReadFalseAndReceiverIdEquals(String receiverId);
}
