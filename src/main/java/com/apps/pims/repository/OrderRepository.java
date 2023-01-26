package com.apps.pims.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.pims.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Page<Order> findOrderBySupplierId(Long id, Pageable pageable);

	List<Order> findOrderByProductName(String value);

	List<Order> findOrderByGGCode(String value);

	List<Order> findOrderByProductNumber(String valueOf);

}
