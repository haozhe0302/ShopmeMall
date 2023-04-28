package com.shopme.admin.brand;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
    @Autowired
    private BrandRepository repo;

    @Test
    public void testCreateBrand1() {
        Category laptops = new Category(4);
        Brand lenovo = new Brand("Lenovo");
        lenovo.getCategories().add(laptops);

        Brand savedBrand = repo.save(lenovo);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand2() {
        Category cellphones = new Category(57);
        Category tablets = new Category(43);

        Brand apple = new Brand("Apple");
        apple.getCategories().add(cellphones);
        apple.getCategories().add(tablets);

        Brand savedBrand = repo.save(apple);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrand3() {
        Brand samsung = new Brand("Samsung");
        samsung.getCategories().add(new Category(6));   // memory
        samsung.getCategories().add(new Category(9));   // hard drive

        Brand savedBrand = repo.save(samsung);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAll() {
        Iterable<Brand> brands = repo.findAll();
        brands.forEach(System.out::println);

        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetById() {
        Brand brand = repo.findById(1).get();

        assertThat(brand.getName()).isEqualTo("Lenovo");
    }

    @Test
    public void testUpdateName() {
        String newName = "Samsung Electronics";
        Brand samsung = repo.findById(3).get();
        samsung.setName(newName);

        Brand savedBrand = repo.save(samsung);

        assertThat(savedBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete() {
        Integer id = 2;
        repo.deleteById(id);

        Optional<Brand> result = repo.findById(id);

        assertThat(result.isEmpty());
    }
}
