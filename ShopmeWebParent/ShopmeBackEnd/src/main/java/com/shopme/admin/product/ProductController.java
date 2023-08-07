package com.shopme.admin.product;

import java.io.IOException;
import java.util.*;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import com.shopme.admin.brand.BrandService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

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

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("pageTitle", "Create New Product");

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes ra) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            product.setMainImage(fileName);

            Product savedProduct = productService.save(product);
            String uploadDir = "product-images/" + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            productService.save(product);
        }

        ra.addFlashAttribute("message", "The product has been saved successfully.");

        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled? "enabled" : "disenabled";
        String message = "The Product ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);

            redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/products";
    }
}
