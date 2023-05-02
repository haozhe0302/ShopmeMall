package com.shopme.admin.product;

import com.shopme.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String listFirstPage(Model model) {
        return listAll(model);
    }

    @GetMapping("/products/listAll")
    public String listAll(Model model){
        List<Product> listProducts = productService.listAll();
        model.addAttribute("listProducts", listProducts);

        return "products/products";
    }
}
