package com.oz.product.repository;

import com.oz.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id")UUID id);

    @Query("select COUNT(*) From Products p where order_id = :id")
    Integer countByOrderId(@Param("id") UUID id);


    @Modifying
    @Query("UPDATE Product p SET p.count = p.count - :quantity WHERE p.id = :id AND p.count >= :quantity")
    int decrementStock(@Param("id") UUID productId, @Param("quantity") Integer quantity);


}
