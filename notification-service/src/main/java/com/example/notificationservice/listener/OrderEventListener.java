package com.example.notificationservice.listener;

import com.example.notificationservice.event.OrderPlacedEvent;
import com.example.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final EmailService emailService;

    @KafkaListener(topics = "order-topic", groupId = "notification-group", containerFactory = "orderPlacedEventListenerFactory")
    public void handleOrderEvent(OrderPlacedEvent event) {
        System.out.println("Nhận được event từ Kafka: " + event);
        // Thực hiện gửi email ở đây
        emailService.sendOrderEmail(event);
    }
}
