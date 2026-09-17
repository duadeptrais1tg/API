package com.vn.thu.mapper;

import org.springframework.stereotype.Component;

import com.vn.thu.dto.ProductDTO;
import com.vn.thu.entity.Product;

@Component
public class ProductMapper {

	public ProductDTO toDTO(Product entity) {
		if (entity == null) {
			return null;
		}
		return ProductDTO.builder()
				.id(entity.getId())
				.name(entity.getName())
				.price(entity.getPrice())
				.quantity(entity.getQuantity())
				.description(entity.getDescription())
				.images(entity.getImages())
				.categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
				.categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
				.build();
	}

	public Product toEntity(ProductDTO dto) {
		if (dto == null) {
			return null;
		}
		return Product.builder()
				.id(dto.getId())
				.name(dto.getName())
				.price(dto.getPrice())
				.quantity(dto.getQuantity())
				.description(dto.getDescription())
				.images(dto.getImages())
				.build();
	}

	public void updateEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setQuantity(dto.getQuantity());
		entity.setDescription(dto.getDescription());

		// Chi cap nhat images neu Service da xu ly file moi
		// (hoac giu anh cu khi khong upload anh moi)
		if (dto.getImages() != null) {
			entity.setImages(dto.getImages());
		}
	}
}
