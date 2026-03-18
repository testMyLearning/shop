package com.oz.order.repository;

import com.oz.order.dto.Order;
import com.oz.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") OrderStatus status);

    @Modifying
    @Query("delete from Order o WHERE o.id = :id")
    void deleteOrderById(@Param("id") UUID id);
}
