package com.vn.thu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Trang quan ly CRUD + tim kiem + phan trang Product/Category
 * hoan toan qua GraphQL (POST /graphql), khac voi /ajax dung RESTful API.
 *
 * Luu y: GraphQL khong ho tro upload file chuan, nen trang nay chi quan ly
 * cac truong van ban/so; upload anh van thuc hien o trang /ajax.
 */
@Controller
public class GraphQLManageController {

	@GetMapping("/graphql-manage")
	public String manage() {
		return "graphql/manage";
	}
}
