package com.vn.thu.controllers.graphql;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.vn.thu.dto.ProductDTO;
import com.vn.thu.model.ProductInput;
import com.vn.thu.services.ProductService;

import lombok.RequiredArgsConstructor;

/**
 * GraphQL endpoint: POST /graphql (xem schema.graphqls).
 * Luu y: GraphQL khong ho tro upload file da phuong tien theo chuan, nen
 * cac mutation o day KHONG xu ly anh - anh van duoc quan ly qua REST API
 * (/api/products) o trang /ajax.
 */
@Controller
@RequiredArgsConstructor
public class ProductGraphQLController {

	private final ProductService productService;

	// =====================================================
	// QUERY
	// =====================================================
	@QueryMapping
	public Page<ProductDTO> products(
			@Argument String keyword,
			@Argument Integer page,
			@Argument Integer size) {
		return productService.findAll(keyword, page != null ? page : 0, size != null ? size : 10);
	}

	@QueryMapping
	public ProductDTO productById(@Argument Long id) {
		return productService.findById(id);
	}

	@QueryMapping
	public List<ProductDTO> productsSortedByPriceAsc() {
		return productService.findAllSortedByPriceAsc();
	}

	@QueryMapping
	public List<ProductDTO> productsByCategory(@Argument Long categoryId) {
		return productService.findByCategoryId(categoryId);
	}

	// =====================================================
	// MUTATION
	// =====================================================
	@MutationMapping
	public ProductDTO createProduct(@Argument ProductInput input) {
		return productService.create(toDTO(null, input));
	}

	@MutationMapping
	public ProductDTO updateProduct(@Argument Long id, @Argument ProductInput input) {
		return productService.update(id, toDTO(id, input));
	}

	@MutationMapping
	public boolean deleteProduct(@Argument Long id) {
		productService.delete(id);
		return true;
	}

	private ProductDTO toDTO(Long id, ProductInput input) {
		return ProductDTO.builder()
				.id(id)
				.name(input.name())
				.price(input.price() != null ? BigDecimal.valueOf(input.price()) : null)
				.quantity(input.quantity())
				.description(input.description())
				.categoryId(input.categoryId())
				.build();
	}
}
