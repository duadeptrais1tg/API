package com.vn.thu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Trang quan ly CRUD Product + Category hoan toan bang AJAX/RESTful API
 * (khong reload trang, khac voi /products dung server-render Thymeleaf).
 */
@Controller
public class AjaxController {

	@GetMapping("/ajax")
	public String manage() {
		return "ajax/manage";
	}
}
