package com.shopme.admin.product;

import java.util.*;
import com.shopme.common.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 3);   // Samsung
        Category category = entityManager.find(Category.class, 57); // Cellphone

        Product product = new Product();
        product.setName("Samsung Galaxy S23 Ultra");
        product.setAlias("samsung_galaxy_s23_ultra");
        product.setShortDescription("5G Black 512GB - 6.8\" 120 Hz AMOLED Display, 200MP+12MP+10MP+10MP Rear Camera, 12MP Selfie Camera, 8K Video, S-Pen Included, Nightography");
        product.setFullDescription("Make it a night to remember with stunning Nightography. Night Mode lets you capture brilliant selfies no matter the lighting Create crystal-clear content worth sharing with Galaxy S23 Ultra's 200MP camera — the highest camera resolution on a Galaxy smartphone Capture scribbles, strokes of genius and everything in between with the built-in S Pen. Writing your next short story or sketching on the go has never felt this easy. Capture epic selfies and beautiful photos with the click of your S Pen Game at full throttle with the fastest processor ever and impressive built-in storage Power through a packed day or a long night of gaming without worrying about your phone dying. The robust 5,000mAh battery has been fine-tuned with a smarter processor that helps manage energy usage without slowing you down.");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(1819);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct =  repo.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateProduct2() {
        Brand brand = entityManager.find(Brand.class, 3);   // Samsung
        Category category = entityManager.find(Category.class, 57); // Cellphone

        Product product = new Product();
        product.setName("Samsung Galaxy S23+");
        product.setAlias("samsung_galaxy_s23+");
        product.setShortDescription("5G Black 512GB - 6.6\" 120 Hz AMOLED Adaptive Display, 50MP+12MP+10MP Rear Camera, 10MP Selfie Camera, 8K Video, Nightography, 4700 mAH Battery");
        product.setFullDescription("Make it a night to remember with stunning Nightography. Night Mode lets you capture brilliant selfies no matter the lighting Share every detail of life’s most share-worthy moments with the impressive 50MP high-resolution camera of Galaxy S23+ Whether you’re working hard, playing hard or doing both at the same time, smoothly switch between apps with the fastest processor ever No matter your environment, Adaptive Vision Booster adjusts your screen’s brightness while keeping your content looking amazing Confidently binge-watch, game or surf without worrying about your phone dying. The impressive 4,700mAh battery will keep you going longer.");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(1524);
        product.setCost(1200);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct =  repo.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts() {
        Iterable<Product> iterableProducts = repo.findAll();

        iterableProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct() {
        Integer id = 2;
        Product product = repo.findById(id).orElse(null);
        System.out.println(product);

        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct() {
        Integer id = 2;
        Product product = repo.findById(id).orElse(null);
        product.setCost(1300);

        repo.save(product);

        Product updatedProduct = entityManager.find(Product.class, id);

        assertThat(updatedProduct.getCost()).isEqualTo(1300);
    }

    @Test
    public void testDeleteProduct() {
        Integer id = 2;
        repo.deleteById(id);

        Optional<Product> result = repo.findById(id);

        assertThat(result.isEmpty());
    }

    @Test
    public void testSaveProductWithImages() {
        Integer productId = 1;
        Product product = repo.findById(productId).orElse(null);

        product.setMainImage("main-image.jpg");
        product.addExtraImage("extra-image-1.jpg");
        product.addExtraImage("extra-image-2.jpg");
        product.addExtraImage("extra-image-3.jpg");

        Product savedProduct = repo.save(product);
        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }
}
