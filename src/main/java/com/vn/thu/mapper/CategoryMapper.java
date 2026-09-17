package com.vn.thu.mapper;

import org.springframework.stereotype.Component;

import com.vn.thu.dto.CategoryDTO;
import com.vn.thu.entity.Category;

@Component
public class CategoryMapper {

	public CategoryDTO toDTO(Category entity) {
		if (entity == null) {
			return null;
		}
		return CategoryDTO.builder()
				.id(entity.getId())
				.name(entity.getName())
				.icon(entity.getIcon())
				.build();
	}

	public Category toEntity(CategoryDTO dto) {
		if (dto == null) {
			return null;
		}
		return Category.builder()
				.id(dto.getId())
				.name(dto.getName())
				.icon(dto.getIcon())
				.build();
	}

	public void updateEntity(CategoryDTO dto, Category entity) {
		entity.setName(dto.getName());
		// Chi cap nhat icon neu Service da xu ly file moi (hoac giu icon cu)
		if (dto.getIcon() != null) {
			entity.setIcon(dto.getIcon());
		}
	}
}
