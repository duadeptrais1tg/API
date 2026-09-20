package com.vn.thu.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.vn.thu.dto.ProductDTO;

public interface ProductService {

	Page<ProductDTO> findAll(String keyword, int page, int size);

	// Tat ca product, sap xep gia tang dan (dung cho trang Home)
	List<ProductDTO> findAllSortedByPriceAsc();

	// Tat ca product thuoc 1 category (dung cho trang Home)
	List<ProductDTO> findByCategoryId(Long categoryId);

	ProductDTO findById(Long id);

	ProductDTO create(ProductDTO dto);

	ProductDTO update(Long id, ProductDTO dto);

	void delete(Long id);
}
