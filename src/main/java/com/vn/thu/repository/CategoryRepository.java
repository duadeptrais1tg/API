package com.vn.thu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.thu.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByNameIgnoreCase(String name);
}
