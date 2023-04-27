package com.shopme.admin.brand;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.admin.category.CategoryService;

@Controller
public class BrandController {
    @Autowired
    private BrandService service;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listAll(Model model) {
        List<Brand> listBrands = service.listAll();
        model.addAttribute("listBrands", listBrands);

        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model) {
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");

        return "brands/brand_form";
    }
}
