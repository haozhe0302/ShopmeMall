package com.shopme.admin.category;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(1, sortDir, model);
    }

    @GetMapping("/categories/listAll")
    public String listAll(Model model){
        List<Category> listCategories = service.listAll();
        model.addAttribute("listCategories", listCategories);
        return "categories/categories";
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, @Param("sortDir") String sortDir, Model model) {
        if (sortDir == null || sortDir.isEmpty()) {
            // Set default sorting order as ascending.
            sortDir = "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> listCategories = service.listByPage(pageInfo, pageNum, sortDir);

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc" ;

        long startCount = (long) (pageNum - 1) * CategoryService.CATEGORIES_PRE_PAGE + 1;
        long endCount = (startCount) + CategoryService.CATEGORIES_PRE_PAGE - 1;
        if (endCount > pageInfo.getTotalElements()){
            endCount = pageInfo.getTotalElements();
        }

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startPage", Math.max(1, pageNum - 3));
        model.addAttribute("endPage", Math.min(pageInfo.getTotalPages(), pageNum + 3));
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> listCategories =  service.listCategoriesUsedInForm();
        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage")MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            category.setImage(fileName);

            Category savedCategory = service.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            service.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Category category = service.get(id);
            List<Category> listCategories = service.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id +")");

            return "categories/category_form";
        } catch (CategoryNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());

            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        service.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            String categoryDir = "category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/categories";
    }
}
