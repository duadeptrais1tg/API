package com.vn.thu.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.vn.thu.dto.CategoryDTO;

public interface CategoryService {

	List<CategoryDTO> findAll();

	// Tim kiem + phan trang (dung cho GraphQL / AJAX)
	Page<CategoryDTO> search(String keyword, int page, int size);

	CategoryDTO findById(Long id);

	CategoryDTO create(CategoryDTO dto);

	CategoryDTO update(Long id, CategoryDTO dto);

	void delete(Long id);
}
