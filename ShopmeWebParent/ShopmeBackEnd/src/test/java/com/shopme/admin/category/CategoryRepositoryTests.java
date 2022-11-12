package com.shopme.admin.category;

import java.util.*;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics");
        Category savedCategory = repo.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory() {
        Category parent = new Category(4);
        Category subCategory = new Category("Gaming Laptops", parent);

         Category savedCategory = repo.save(subCategory);
         assertThat(savedCategory.getId()).isGreaterThan(0);

        //repo.saveAll(List.of(laptops, components));
    }

    @Test
    public void testGetCategory() {
        Category category = repo.findById(1).orElse(null);
        assert category != null;
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();
        for (Category subCategory : children) {
            System.out.println(subCategory.getName());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories() {
        Iterable<Category> categories = repo.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel) {
        int curSublevel = subLevel + 1;

        for (Category subCategory : parent.getChildren()) {
            for (int i = 0; i < curSublevel; i ++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());

            printChildren(subCategory, curSublevel);
        }
    }
}
