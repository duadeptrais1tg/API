package com.vn.thu.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vn.thu.dto.ProductDTO;
import com.vn.thu.entity.Category;
import com.vn.thu.entity.Product;
import com.vn.thu.mapper.ProductMapper;
import com.vn.thu.repository.CategoryRepository;
import com.vn.thu.repository.ProductRepository;
import com.vn.thu.services.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final CategoryRepository categoryRepository;

	private final Path uploadDir = Paths.get("uploads/products");

	// =====================================================
	// SEARCH + PAGINATION
	// =====================================================
	@Override
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(String keyword, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

		Page<Product> products;

		if (keyword == null || keyword.isBlank()) {
			products = productRepository.findAll(pageable);
		} else {
			products = productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
		}

		return products.map(productMapper::toDTO);
	}

	// =====================================================
	// TAT CA, SAP XEP GIA TANG DAN (trang Home)
	// =====================================================
	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> findAllSortedByPriceAsc() {
		return productRepository.findAll(Sort.by("price").ascending()).stream()
				.map(productMapper::toDTO)
				.collect(Collectors.toList());
	}

	// =====================================================
	// TAT CA PRODUCT THUOC 1 CATEGORY (trang Home)
	// =====================================================
	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> findByCategoryId(Long categoryId) {
		return productRepository.findByCategoryId(categoryId, Sort.by("id").descending()).stream()
				.map(productMapper::toDTO)
				.collect(Collectors.toList());
	}

	// =====================================================
	// FIND BY ID
	// =====================================================
	@Override
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm: " + id));
		return productMapper.toDTO(product);
	}

	// =====================================================
	// CREATE
	// =====================================================
	@Override
	public ProductDTO create(ProductDTO dto) {
		try {
			String fileName = saveImage(dto.getImage());
			dto.setImages(fileName);

			Product product = productMapper.toEntity(dto);
			product.setCreatedAt(LocalDateTime.now());
			product.setCategory(resolveCategory(dto.getCategoryId()));

			Product saved = productRepository.save(product);
			return productMapper.toDTO(saved);
		} catch (IOException e) {
			throw new RuntimeException("Không thể upload ảnh", e);
		}
	}

	// =====================================================
	// UPDATE
	// =====================================================
	@Override
	public ProductDTO update(Long id, ProductDTO dto) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		try {
			MultipartFile image = dto.getImage();

			if (image != null && !image.isEmpty()) {
				// Co upload anh moi -> xoa anh cu, luu anh moi
				String oldImage = product.getImages();
				String newImage = saveImage(image);
				dto.setImages(newImage);
				deleteImage(oldImage);
			} else {
				// Khong upload anh moi -> giu nguyen anh cu
				dto.setImages(product.getImages());
			}

			productMapper.updateEntity(dto, product);
			product.setCategory(resolveCategory(dto.getCategoryId()));
			Product updated = productRepository.save(product);
			return productMapper.toDTO(updated);
		} catch (IOException e) {
			throw new RuntimeException("Không thể upload ảnh", e);
		}
	}

	// =====================================================
	// DELETE
	// =====================================================
	@Override
	public void delete(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

		deleteImage(product.getImages());
		productRepository.delete(product);
	}

	// =====================================================
	// RESOLVE CATEGORY
	// =====================================================
	private Category resolveCategory(Long categoryId) {
		if (categoryId == null) {
			return null;
		}
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy category: " + categoryId));
	}

	// =====================================================
	// SAVE IMAGE
	// =====================================================
	private String saveImage(MultipartFile file) throws IOException {
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

	// =====================================================
	// DELETE IMAGE
	// =====================================================
	private void deleteImage(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return;
		}
		try {
			Path file = uploadDir.resolve(fileName).normalize();
			// Chong Path Traversal
			if (!file.toAbsolutePath().normalize().startsWith(uploadDir.toAbsolutePath().normalize())) {
				return;
			}
			Files.deleteIfExists(file);
		} catch (IOException e) {
			System.err.println("Không thể xóa ảnh: " + fileName);
		}
	}
}
