package com.vn.thu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.thu.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

	List<Product> findByCategoryId(Long categoryId, Sort sort);
}
