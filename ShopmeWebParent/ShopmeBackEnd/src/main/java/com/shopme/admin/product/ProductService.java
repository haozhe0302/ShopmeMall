package com.shopme.admin.product;

import com.shopme.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> listAll(){
        return (List<Product>) repo.findAll(Sort.by("name").ascending());
    }


}
