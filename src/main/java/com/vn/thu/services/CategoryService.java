package com.vn.thu.services;

import java.util.List;

import com.vn.thu.dto.CategoryDTO;

public interface CategoryService {

	List<CategoryDTO> findAll();

	CategoryDTO findById(Long id);

	CategoryDTO create(CategoryDTO dto);

	CategoryDTO update(Long id, CategoryDTO dto);

	void delete(Long id);
}
