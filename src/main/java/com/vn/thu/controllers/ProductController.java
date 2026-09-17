package com.vn.thu.controllers;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vn.thu.dto.ProductDTO;
import com.vn.thu.services.CategoryService;
import com.vn.thu.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final CategoryService categoryService;

	// Cho phep option "-- Khong thuoc danh muc --" (value="") map thanh null
	// thay vi loi convert "" -> Long
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Long.class, "categoryId", new CustomNumberEditor(Long.class, true));
	}

	// =========================
	// LIST + SEARCH + PAGINATION
	// =========================
	@GetMapping
	public String listProducts(
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			Model model) {

		Page<ProductDTO> productPage = productService.findAll(keyword, page, size);

		model.addAttribute("products", productPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("size", size);

		return "products/list";
	}

	// =========================
	// CREATE FORM
	// =========================
	@GetMapping("/create")
	public String createForm(Model model) {
		model.addAttribute("product", new ProductDTO());
		model.addAttribute("formTitle", "Thêm sản phẩm");
		model.addAttribute("categories", categoryService.findAll());
		return "products/form";
	}

	// =========================
	// CREATE
	// =========================
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public String create(
			@Valid @ModelAttribute("product") ProductDTO dto,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			model.addAttribute("formTitle", "Thêm sản phẩm");
			model.addAttribute("categories", categoryService.findAll());
			return "products/form";
		}

		try {
			productService.create(dto);
			redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công");
			return "redirect:/products";
		} catch (Exception e) {
			model.addAttribute("formTitle", "Thêm sản phẩm");
			model.addAttribute("categories", categoryService.findAll());
			model.addAttribute("error", "Không thể thêm sản phẩm: " + e.getMessage());
			return "products/form";
		}
	}

	// =========================
	// EDIT FORM
	// =========================
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		ProductDTO product = productService.findById(id);
		model.addAttribute("product", product);
		model.addAttribute("formTitle", "Cập nhật sản phẩm");
		model.addAttribute("categories", categoryService.findAll());
		return "products/form";
	}

	// =========================
	// UPDATE
	// =========================
	@PostMapping(value = "/edit/{id}", consumes = "multipart/form-data")
	public String update(
			@PathVariable Long id,
			@Valid @ModelAttribute("product") ProductDTO dto,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			model.addAttribute("formTitle", "Cập nhật sản phẩm");
			model.addAttribute("categories", categoryService.findAll());
			return "products/form";
		}

		try {
			productService.update(id, dto);
			redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công");
			return "redirect:/products";
		} catch (Exception e) {
			model.addAttribute("formTitle", "Cập nhật sản phẩm");
			model.addAttribute("categories", categoryService.findAll());
			model.addAttribute("error", "Không thể cập nhật sản phẩm: " + e.getMessage());
			return "products/form";
		}
	}

	// =========================
	// DELETE
	// =========================
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm");
		}
		return "redirect:/products";
	}
}
