package com.oz.payment.repository;

import com.oz.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(UUID uuid);

    @Query("select p from payment p")
    Stream<Payment> streamAllPayments();
}
