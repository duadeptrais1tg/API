package com.vn.thu.controllers.api;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vn.thu.dto.ProductDTO;
import com.vn.thu.model.ApiResponse;
import com.vn.thu.services.ProductService;

import lombok.RequiredArgsConstructor;

/**
 * REST API cho Product - dung cho trang quan ly AJAX (/ajax).
 */
@RestController
@RequestMapping(path = "/api/products")
@RequiredArgsConstructor
public class ProductApiController {

	private final ProductService productService;

	// Cho phep categoryId rong ("") map thanh null thay vi loi convert
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Long.class, "categoryId", new CustomNumberEditor(Long.class, true));
	}

	// GET /api/products?keyword=&page=0&size=100
	@GetMapping
	public ResponseEntity<ApiResponse> getAll(
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size) {

		Page<ProductDTO> products = productService.findAll(keyword, page, size);
		return ResponseEntity.ok(ApiResponse.ok("Thành công", products));
	}

	// GET /api/products/{id}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getOne(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(ApiResponse.ok("Thành công", productService.findById(id)));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(e.getMessage()));
		}
	}

	// POST /api/products (multipart/form-data)
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse> create(@ModelAttribute ProductDTO dto) {
		try {
			ProductDTO created = productService.create(dto);
			return ResponseEntity.ok(ApiResponse.ok("Thêm sản phẩm thành công", created));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));
		}
	}

	// PUT /api/products/{id} (multipart/form-data)
	@PutMapping(path = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse> update(@PathVariable Long id, @ModelAttribute ProductDTO dto) {
		try {
			ProductDTO updated = productService.update(id, dto);
			return ResponseEntity.ok(ApiResponse.ok("Cập nhật sản phẩm thành công", updated));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));
		}
	}

	// DELETE /api/products/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
		try {
			productService.delete(id);
			return ResponseEntity.ok(ApiResponse.ok("Xóa sản phẩm thành công", null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));
		}
	}
}
