package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<Category> listAll() {
        return (List<Category>) repo.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDB =  (List<Category>) repo.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                // System.out.println(category.getName());
                categoriesUsedInForm.add(new Category(category.getName()));

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    // System.out.println("--" + subCategory.getName());
                    String categoryName = "--" + subCategory.getName();
                    categoriesUsedInForm.add(new Category(categoryName));
                    listChildren(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int curSublevel = subLevel + 1;

        for (Category subCategory : parent.getChildren()) {
            String subCategoryName = "";
            for (int i = 0; i < curSublevel; i ++){
                // System.out.print("--");
                subCategoryName += "--";
            }
            // System.out.println(subCategory.getName());
            subCategoryName += subCategory.getName();
            categoriesUsedInForm.add(new Category(subCategoryName));

            listChildren(categoriesUsedInForm, subCategory, curSublevel);
        }
    }
}
