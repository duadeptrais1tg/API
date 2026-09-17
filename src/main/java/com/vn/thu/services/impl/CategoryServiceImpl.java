package com.vn.thu.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vn.thu.dto.CategoryDTO;
import com.vn.thu.entity.Category;
import com.vn.thu.mapper.CategoryMapper;
import com.vn.thu.repository.CategoryRepository;
import com.vn.thu.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	private final Path uploadDir = Paths.get("uploads/categories");

	// =====================================================
	// FIND ALL
	// =====================================================
	@Override
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		return categoryRepository.findAll().stream()
				.map(categoryMapper::toDTO)
				.collect(Collectors.toList());
	}

	// =====================================================
	// FIND BY ID
	// =====================================================
	@Override
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy category"));
		return categoryMapper.toDTO(category);
	}

	// =====================================================
	// CREATE
	// =====================================================
	@Override
	public CategoryDTO create(CategoryDTO dto) {
		categoryRepository.findByNameIgnoreCase(dto.getName()).ifPresent(c -> {
			throw new RuntimeException("Category '" + dto.getName() + "' đã tồn tại");
		});

		try {
			String fileName = saveIcon(dto.getIconFile());
			dto.setIcon(fileName);

			Category category = categoryMapper.toEntity(dto);
			Category saved = categoryRepository.save(category);
			return categoryMapper.toDTO(saved);
		} catch (IOException e) {
			throw new RuntimeException("Không thể upload icon", e);
		}
	}

	// =====================================================
	// UPDATE
	// =====================================================
	@Override
	public CategoryDTO update(Long id, CategoryDTO dto) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

		categoryRepository.findByNameIgnoreCase(dto.getName())
				.filter(c -> !c.getId().equals(id))
				.ifPresent(c -> {
					throw new RuntimeException("Category '" + dto.getName() + "' đã tồn tại");
				});

		try {
			MultipartFile iconFile = dto.getIconFile();

			if (iconFile != null && !iconFile.isEmpty()) {
				String oldIcon = category.getIcon();
				String newIcon = saveIcon(iconFile);
				dto.setIcon(newIcon);
				deleteIcon(oldIcon);
			} else {
				dto.setIcon(category.getIcon());
			}

			categoryMapper.updateEntity(dto, category);
			Category updated = categoryRepository.save(category);
			return categoryMapper.toDTO(updated);
		} catch (IOException e) {
			throw new RuntimeException("Không thể upload icon", e);
		}
	}

	// =====================================================
	// DELETE
	// =====================================================
	@Override
	public void delete(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

		if (!category.getProducts().isEmpty()) {
			throw new RuntimeException(
					"Không thể xóa: category đang được " + category.getProducts().size() + " sản phẩm sử dụng");
		}

		deleteIcon(category.getIcon());
		categoryRepository.delete(category);
	}

	// =====================================================
	// SAVE / DELETE ICON FILE
	// =====================================================
	private String saveIcon(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty()) {
			return null;
		}

		Files.createDirectories(uploadDir);

		String originalName = file.getOriginalFilename();
		String extension = "";
		if (originalName != null && originalName.contains(".")) {
			extension = originalName.substring(originalName.lastIndexOf("."));
		}

		String fileName = UUID.randomUUID().toString() + extension;
		Path target = uploadDir.resolve(fileName);

		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
		}

		return fileName;
	}

	private void deleteIcon(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return;
		}
		try {
			Path file = uploadDir.resolve(fileName).normalize();
			if (!file.toAbsolutePath().normalize().startsWith(uploadDir.toAbsolutePath().normalize())) {
				return;
			}
			Files.deleteIfExists(file);
		} catch (IOException e) {
			System.err.println("Không thể xóa icon: " + fileName);
		}
	}
}
