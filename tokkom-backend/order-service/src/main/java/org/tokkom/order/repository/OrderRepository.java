package org.tokkom.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tokkom.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
