package com.vn.thu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String root() {
		return "redirect:/home";
	}

	// Trang gioi thieu san pham cho khach: sap xep gia + loc theo danh muc.
	// Du lieu duoc nap qua AJAX goi GraphQL (/graphql), khong server-render.
	@GetMapping("/home")
	public String home() {
		return "home";
	}
}
