package com.vn.thu.services;

import org.springframework.data.domain.Page;

import com.vn.thu.dto.ProductDTO;

public interface ProductService {

	Page<ProductDTO> findAll(String keyword, int page, int size);

	ProductDTO findById(Long id);

	ProductDTO create(ProductDTO dto);

	ProductDTO update(Long id, ProductDTO dto);

	void delete(Long id);
}
