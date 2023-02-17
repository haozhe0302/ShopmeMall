package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");
        if (sortDir == null || sortDir.isEmpty()) {
            // Set default sorting order as ascending.
            sort = sort.ascending();
        } else if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")){
            sort = sort.descending();
        }

        List<Category> rootCategories = repo.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
        int curSublevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);

        for (Category subCategory : children) {
            String subCategoryName = "--".repeat(Math.max(0, curSublevel)) + subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, subCategoryName));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, curSublevel, sortDir);
        }
    }

    public Category save(Category category) {
        return repo.save(category);
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                // System.out.println(category.getName());
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());

                for (Category subCategory : children) {
                    // System.out.println("--" + subCategory.getName());
                    String categoryName = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), categoryName));
                    listSubCategories(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listSubCategories(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int curSublevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category subCategory : children) {
            // System.out.print("--");
            String subCategoryName = "--".repeat(Math.max(0, curSublevel)) +
                    // System.out.println(subCategory.getName());
                    subCategory.getName();
            categoriesUsedInForm.add(new Category(subCategoryName));

            listSubCategories(categoriesUsedInForm, subCategory, curSublevel);
        }
    }

    public Category get(Integer id) throws CategoryNotFoundException{
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName =  repo.findByName(name);

        if(isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicateName";
            } else {
                Category categoryByAlias = repo.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByName != null && !Objects.equals(categoryByName.getId(), id)) {
                return "DuplicateName";
            }

            Category categoryByAlias = repo.findByAlias(alias);
            if (categoryByAlias != null && !Objects.equals(categoryByAlias.getId(), id)) {
                return "DuplicateAlias";
            }
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }
}
