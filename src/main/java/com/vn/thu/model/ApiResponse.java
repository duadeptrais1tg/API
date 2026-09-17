package com.vn.thu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Response chuan cho REST API, dung chung cho ca Category va Product.
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {

	private boolean success;
	private String message;
	private Object data;

	public static ApiResponse ok(String message, Object data) {
		return new ApiResponse(true, message, data);
	}

	public static ApiResponse fail(String message) {
		return new ApiResponse(false, message, null);
	}
}
