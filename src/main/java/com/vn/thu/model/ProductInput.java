package com.vn.thu.model;

/**
 * Khop voi "input ProductInput" trong schema.graphqls.
 * Spring GraphQL tu dong bind cac argument object sang record theo ten field.
 */
public record ProductInput(
		String name,
		Double price,
		Integer quantity,
		String description,
		Long categoryId) {
}
