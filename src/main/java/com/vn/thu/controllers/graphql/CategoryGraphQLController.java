package com.vn.thu.controllers.graphql;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.vn.thu.dto.CategoryDTO;
import com.vn.thu.model.CategoryInput;
import com.vn.thu.services.CategoryService;

import lombok.RequiredArgsConstructor;

/**
 * GraphQL endpoint: POST /graphql (xem schema.graphqls).
 * Luu y: mutation o day KHONG xu ly upload icon - icon van duoc quan ly
 * qua REST API (/api/categories) o trang /ajax.
 */
@Controller
@RequiredArgsConstructor
public class CategoryGraphQLController {

	private final CategoryService categoryService;

	// =====================================================
	// QUERY
	// =====================================================
	@QueryMapping
	public Page<CategoryDTO> categories(
			@Argument String keyword,
			@Argument Integer page,
			@Argument Integer size) {
		return categoryService.search(keyword, page != null ? page : 0, size != null ? size : 10);
	}

	@QueryMapping
	public CategoryDTO categoryById(@Argument Long id) {
		return categoryService.findById(id);
	}

	@QueryMapping
	public List<CategoryDTO> allCategories() {
		return categoryService.findAll();
	}

	// =====================================================
	// MUTATION
	// =====================================================
	@MutationMapping
	public CategoryDTO createCategory(@Argument CategoryInput input) {
		return categoryService.create(CategoryDTO.builder().name(input.name()).build());
	}

	@MutationMapping
	public CategoryDTO updateCategory(@Argument Long id, @Argument CategoryInput input) {
		return categoryService.update(id, CategoryDTO.builder().id(id).name(input.name()).build());
	}

	@MutationMapping
	public boolean deleteCategory(@Argument Long id) {
		categoryService.delete(id);
		return true;
	}
}
