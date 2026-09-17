package com.vn.thu.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

	private Long id;

	@NotBlank(message = "Tên category không được để trống")
	@Size(max = 200, message = "Tên category tối đa 200 ký tự")
	private String name;

	// Ten file da luu trong DB
	private String icon;

	// File upload tu form (khong bat buoc khi sua)
	private MultipartFile iconFile;
}
