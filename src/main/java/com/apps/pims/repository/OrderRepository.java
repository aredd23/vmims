package com.apps.pims.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.pims.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Page<Order> findOrderBySupplierId(Long id, Pageable pageable);


}
