package com.vn.thu.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.thu.dto.CategoryDTO;
import com.vn.thu.model.ApiResponse;
import com.vn.thu.services.CategoryService;

import lombok.RequiredArgsConstructor;

/**
 * REST API cho Category - dung cho trang quan ly AJAX (/ajax).
 */
@RestController
@RequestMapping(path = "/api/categories")
@RequiredArgsConstructor
public class CategoryApiController {

	private final CategoryService categoryService;

	// GET /api/categories
	@GetMapping
	public ResponseEntity<ApiResponse> getAll() {
		return ResponseEntity.ok(ApiResponse.ok("Thành công", categoryService.findAll()));
	}

	// GET /api/categories/{id}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getOne(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(ApiResponse.ok("Thành công", categoryService.findById(id)));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(e.getMessage()));
		}
	}

	// POST /api/categories (multipart/form-data: name, iconFile)
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse> create(@ModelAttribute CategoryDTO dto) {
		try {
			CategoryDTO created = categoryService.create(dto);
			return ResponseEntity.ok(ApiResponse.ok("Thêm category thành công", created));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));
		}
	}

	// PUT /api/categories/{id} (multipart/form-data: name, iconFile - optional)
	@PutMapping(path = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse> update(@PathVariable Long id, @ModelAttribute CategoryDTO dto) {
		try {
			CategoryDTO updated = categoryService.update(id, dto);
			return ResponseEntity.ok(ApiResponse.ok("Cập nhật category thành công", updated));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));
		}
	}

	// DELETE /api/categories/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
		try {
			categoryService.delete(id);
			return ResponseEntity.ok(ApiResponse.ok("Xóa category thành công", null));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(e.getMessage()));
		}
	}
}
