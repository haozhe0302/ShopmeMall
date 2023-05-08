package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
    public Product findByName(String name);
}
